package z.cube.param.handler;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z.cube.param.config.InitConfig;

public class XmlConfigHandler implements ConfigHandler {
    private static final String DEFAULT_SPLIT_CHAR = ",";
    private transient final Logger               log;
    private                 String[]             xmlFileNames;
    private static          ConfigurationBuilder configurationBuilder;

    public XmlConfigHandler() {
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void init(InitConfig initConfig) {
        String fileNames = initConfig.getXmlFileNames();
        this.xmlFileNames = StringUtils.split(fileNames, DEFAULT_SPLIT_CHAR);
        buildConfiguration();
    }

    private void buildConfiguration() {
        if (configurationBuilder == null) {
            XMLBuilderParameters xmlBuilderParameters = new Parameters().xml();
            xmlBuilderParameters.setEncoding(CharEncoding.UTF_8).setValidating(false);

            for (String p : xmlFileNames) {
                xmlBuilderParameters.setFileName(p);
            }
            FileBasedConfigurationBuilder builder =
                    new FileBasedConfigurationBuilder(XMLConfiguration.class)
                            .configure(xmlBuilderParameters);
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
