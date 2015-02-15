package z.cube.param.handler;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.PropertiesBuilderParametersImpl;
import org.apache.commons.configuration2.builder.combined.MultiFileConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z.cube.param.config.InitConfig;

import java.nio.charset.Charset;

public class PropertiesConfigHandler implements ConfigHandler {
    /** 默认配置文件加载路径，自动加载该路径下的所有properties文件 */
    private static final String DEFAULT_CONFIG_PATH = "/config";
    private static final String DEFAULT_SPLIT_CHAR  = ",";
    /** 线程安全，设置为static并且已实例化则不再处理 */
    private static          ConfigurationBuilder configurationBuilder;
    private transient final Logger               log;
    private                 String[]             propertiesFileNames;

    public PropertiesConfigHandler() {
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void init(InitConfig initConfig) {
        String fileNames = initConfig.getPropertiesFileNames();
        this.propertiesFileNames = StringUtils.split(fileNames, DEFAULT_SPLIT_CHAR);
        buildConfiguration();
    }

    private void buildConfiguration() {
        if (configurationBuilder == null) {
            //TODO:bug 多个属性文件时只会取最后一个属性文件，通过在属性文件中以`include`的形式加载多个
            PropertiesBuilderParameters propertiesBuilderParameters = new Parameters().properties();
            propertiesBuilderParameters.setEncoding(CharEncoding.UTF_8);
            for (String p : propertiesFileNames) {
                propertiesBuilderParameters.setFileName(p);
            }
            FileBasedConfigurationBuilder builder =
                    new FileBasedConfigurationBuilder(PropertiesConfiguration.class)
                            .configure(propertiesBuilderParameters);
            configurationBuilder = builder;
        }
    }

    @Override
    public Object getValue(String key) {
        try {
            return configurationBuilder.getConfiguration().getString(key);
        } catch (ConfigurationException e) {
            log.warn("{}未取到对应的值!(异常:{})", key, e.getMessage());
        }
        return null;
    }
}
