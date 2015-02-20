package z.cube.param.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import z.cube.param.config.Config;
import z.cube.param.config.InitConfig;
import z.cube.param.config.SourceType;
import z.cube.param.handler.CacheRegister;
import z.cube.param.handler.ConfigHandlerFactory;
import z.cube.param.handler.ConfigValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import static z.cube.utils.AT.at;

public class AnnotationScannerPostProcessor implements BeanPostProcessor {

    public static final Map<Method, Annotation> CACHE = CacheRegister.CACHE;
    public static final Map<Annotation, Object> REGISTER = CacheRegister.REGISTER;
    public static final Set<Annotation> INVALID = ConfigValidator.INVALID;

    private static boolean isChecked = false;

    @Autowired
    private InitConfig initConfig;

    /*
    * 此处不注入，由于 DataSourceInitializer.afterPropertiesSet 是在初始化之后调用，
    * 所以在postProcessAfterInitialization 中数据库初始化脚本未执行过，则无法查询到数据；
    * 
    * 此处注入，则是会先执行afterPropertiesSet后，再注入，这样保证了数据库的初始化脚本的执行*
    * * * */
    @Autowired
    private DataSourceInitializer initializer;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        /**
         * 1. 获取每个方法参数上的注解 
         */

        Class clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int m = 0; m < methods.length; m++) {
            Method tempMethod = methods[m];
            Type[] types = tempMethod.getGenericParameterTypes();
            for (int t = 0; t < types.length; t++) {
                Config config = at(tempMethod).arg(t).annotation(Config.class).get();
                if (config != null) {
                    CACHE.put(tempMethod, config);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!isChecked) {
            for (Map.Entry<Method, Annotation> entry : CACHE.entrySet()) {
                Annotation annotation = entry.getValue();
                if (Config.class.equals(annotation.annotationType())) {
                    Config config = (Config) annotation;

                    //FlexSession是运行时的临时存储不能启动时校验，考虑该情况getValue为null时直接抛异常
                    if (config.source().equals(SourceType.FLEX)) continue;

                    Object val = ConfigHandlerFactory.getValue(config, initConfig);
                    if (val != null) {
                        REGISTER.put(config, val);
                    } else {
                        INVALID.add(config);
                    }
                }
            }
            isChecked = true;
        }
        return bean;
    }
}
