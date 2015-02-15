package z.cube.param.handler;

import org.apache.commons.configuration2.DatabaseConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.DatabaseBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z.cube.param.config.InitConfig;

import javax.sql.DataSource;

public class DatabaseConfigHandler implements ConfigHandler {
    /** 线程安全，设置为static并且已实例化则不再处理 */
    private static          ConfigurationBuilder configurationBuilder;
    private transient final Logger               log;
    private                 DataSource           dataSource;
    private                 String               tableName;
    private                 String               keyColumn;
    private                 String               valueColumn;

    public DatabaseConfigHandler() {
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void init(InitConfig initConfig) {
        this.dataSource = initConfig.getDataSource();
        this.tableName = initConfig.getTableName();
        this.keyColumn = initConfig.getKeyColumn();
        this.valueColumn = initConfig.getValueColumn();
        buildConfiguration();
    }

    private void buildConfiguration() {
        if (configurationBuilder == null) {
            DatabaseBuilderParameters builderParameters = new Parameters().database()
                    .setDataSource(dataSource)
                    .setTable(tableName)
                    .setKeyColumn(keyColumn)
                    .setValueColumn(valueColumn)
                    .setAutoCommit(true);
            BasicConfigurationBuilder builder = new BasicConfigurationBuilder(DatabaseConfiguration.class)
                    .configure(builderParameters);
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
