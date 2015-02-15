package z.cube.param.handler;


import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z.cube.param.config.InitConfig;

public class FlexSessionConfigHandler implements ConfigHandler {
    private static final String REF_CHAR = ".";
    private transient final Logger log;

    public FlexSessionConfigHandler() {
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void init(InitConfig initConfig) {

    }

    @Override
    public Object getValue(String key) {
        FlexSession session = FlexContext.getFlexSession();
        if (StringUtils.contains(key, REF_CHAR)) {
            int firstIndex = StringUtils.indexOf(key, REF_CHAR);
            String prefix = StringUtils.substring(key, 0, firstIndex);
            Object holder = session.getAttribute(prefix);
            if (holder != null) {
                String suffix = StringUtils.substring(key, firstIndex + 1);
                try {
                    return PropertyUtils.getProperty(holder, suffix);
                } catch (Exception e) {
                    log.warn("{}未取到对应的值!(异常:{})", key, e.getMessage());
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return session.getAttribute(key);
        }
    }
}
