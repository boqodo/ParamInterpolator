package z.cube.param.handler;

import org.springframework.beans.BeanUtils;
import z.cube.param.config.InitConfig;
import z.cube.param.config.SourceType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigHandlerFactory {
    private static final String              INSTANCE_SUFFIX = "#Instance";
    private static final Map<Object, Object> HANDLER_MAP     = new ConcurrentHashMap<Object, Object>();

    static {
        HANDLER_MAP.put(SourceType.DATABASE, DatabaseConfigHandler.class);
        HANDLER_MAP.put(SourceType.PROPERTIES, PropertiesConfigHandler.class);
        HANDLER_MAP.put(SourceType.XML,XmlConfigHandler.class);
        HANDLER_MAP.put(SourceType.FLEX,FlexSessionConfigHandler.class);
    }

    public static void registerHandler(SourceType sourceType, Class<? extends ConfigHandler> clazz) {
        if (!HANDLER_MAP.containsKey(sourceType)) {
            HANDLER_MAP.put(sourceType, clazz);
        }
    }
    public static void overrideHandler(SourceType sourceType,Class<? extends ConfigHandler> clazz){
        HANDLER_MAP.put(sourceType,clazz);
    }

    @SuppressWarnings("unchecked")
    public static ConfigHandler getConfigHandler(SourceType sourceType, InitConfig initConfig) {
        String instanceKey = sourceType.toString() + INSTANCE_SUFFIX;
        ConfigHandler ch = null;
        if (HANDLER_MAP.containsKey(instanceKey)) {
            ch = (ConfigHandler) HANDLER_MAP.get(instanceKey);
        } else {
            Class<? extends ConfigHandler> clazz = (Class<? extends ConfigHandler>) HANDLER_MAP.get(sourceType);
            ch = BeanUtils.instantiate(clazz);
            ch.init(initConfig);
            HANDLER_MAP.put(instanceKey, ch);
        }
        return ch;
    }
}
