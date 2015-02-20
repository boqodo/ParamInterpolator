package z.cube.param.handler;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import z.cube.param.config.Config;
import z.cube.param.config.InitConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CacheRegister {
    public static final Map<Method, Annotation> CACHE = new ConcurrentHashMap<Method, Annotation>(64);
    public static final Map<Annotation, Object> REGISTER = new ConcurrentHashMap<Annotation, Object>(64);

    public Object getValue(Config config, InitConfig initConfig) {
        Object value = REGISTER.get(config);

        if (value == null) {
            value = ConfigHandlerFactory.getValue(config, initConfig);
            REGISTER.put(config, value);
        }
        return value;
    }

    public void saveOrUpdate(Config config, Object value) {
        Validate.notNull(config);
        Validate.notNull(value);
        if (REGISTER.containsKey(config)) {
            Object temp = REGISTER.get(config);
            if (ObjectUtils.notEqual(temp, value)) {
                REGISTER.put(config, value);
            }
        } else {
            REGISTER.put(config, value);
        }
    }

    private void updateRealValue(Config config, Object value) {
        //更新具体的xml properties database的值信息
    }

    public boolean clear() {
        REGISTER.clear();
        return REGISTER.isEmpty();
    }
}
