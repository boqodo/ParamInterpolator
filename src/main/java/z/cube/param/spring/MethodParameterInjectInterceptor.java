package z.cube.param.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import z.cube.param.config.Config;
import z.cube.param.config.InitConfig;
import z.cube.param.config.SourceType;
import z.cube.param.handler.ConfigHandler;
import z.cube.param.handler.ConfigHandlerFactory;
import z.cube.utils.AT;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.joor.Reflect.on;
import static z.cube.utils.AT.at;


public class MethodParameterInjectInterceptor implements MethodInterceptor {

    //spring4.1 使用DefaultParameterNameDiscoverer,针对jdk8的时候会自动使用jdk8自带的api
    private static final ParameterNameDiscoverer PND = new LocalVariableTableParameterNameDiscoverer();

    private final InitConfig initConfig;

    public MethodParameterInjectInterceptor(InitConfig initConfig) {
        this.initConfig = initConfig;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method invokedMethod = invocation.getMethod();
        Annotation[][] paramAnns = invokedMethod.getParameterAnnotations();

        //传入参数的对象数组
        Object[] parameters = invocation.getArguments();


        //传入参数的名称数组
        String[] parameterNames = PND.getParameterNames(invokedMethod);

        //传入参数的对象类型
        Type[] paramTypes = invokedMethod.getGenericParameterTypes();

        //逐个基本类型参数处理
        for (int i = 0; i < paramAnns.length; i++) {
            Config config = findConfig(paramAnns[i]);
            Type paramType = paramTypes[i];
            //参数值不为空，则不注入
            if (config != null && parameters[i] == null) {
                String key = StringUtils.isNotBlank(config.key()) ? config.key() : parameterNames[i];
                parameters[i] = findInjectValue(config.source(), key, paramType);
                break;
            }

            //针对处理方法参数没有注解Config，但是参数对应类型上有注解的处理
            Class<?> paramClazz = (Class<?>) paramType;
            Config typeConfig = (Config) paramClazz.getAnnotation(Config.class);
            boolean isPresent = typeConfig != null;
            if (isPresent && parameters[i] == null) {
                parameters[i] = injectObject(paramClazz, typeConfig.source());
            }
        }

        return invocation.proceed();
    }

    private Object findInjectValue(SourceType sourceType, String key, Type type) {
        Class<?> clazz = (Class<?>) type;
        ConfigHandler handler = getConfigHandler(sourceType);

        if (BeanUtils.isSimpleProperty(clazz)) {
            Object val = handler.getValue(key);
            val = ConvertUtils.convert(val, clazz);
            return val;
        } else {
            return injectObject(clazz, handler);

        }
    }

    private Object injectObject(Class<?> clazz, SourceType sourceType) {
        ConfigHandler handler = getConfigHandler(sourceType);
        return injectObject(clazz, handler);
    }

    private ConfigHandler getConfigHandler(SourceType sourceType) {
        return ConfigHandlerFactory.getConfigHandler(sourceType, initConfig);
    }

    private Object getValue(Config config){
        return getConfigHandler(config.source()).getValue(config.key());
    }

    private Object injectObject(Class<?> clazz, ConfigHandler handler) {
        //复杂类型处理
        //TODO:当前取默认来源，需要处理成同一个来源中取得所有字段对应值情况
        //TODO:针对大型复杂类考虑做缓存处理，各类资源属性变更的时候通过事件监听派发处理更新
        Object vo = BeanUtils.instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();

            if (BeanUtils.isSimpleProperty(fieldType)) {
                //获取字段上的Config注解,未设Config注解，则默认用字段名作为key取值

                String name=field.getName();
                Object val;
                if(at(field).isPresent(Config.class)){
                    val=getValue(at(field).ai(Config.class));
                }else{
                    val=handler.getValue(name);
                }
                val = ConvertUtils.convert(val, fieldType);
                on(vo).set(name, val);
            } else {
                //TODO: 非简单属性暂不考虑
            }
        }
        return vo;
    }

    private Config findConfig(Annotation[] anns) {
        Config temp = null;
        //编译器自动校验是否使用了重复注解
        //为此代码不处理，匹配后直接跳出
        for (Annotation ann : anns) {
            if (ann instanceof Config) {
                temp = (Config) ann;
                break;
            }
        }
        return temp;
    }
}
