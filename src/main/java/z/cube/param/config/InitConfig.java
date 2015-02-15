package z.cube.param.config;

import javax.sql.DataSource;

public class InitConfig {
    private String     propertiesFileNames;
    private String     xmlFileNames;
    private String     tableName;
    private String     keyColumn;
    private String     valueColumn;
    private DataSource dataSource;

    private String configPath;

    public InitConfig() {
    }

    public InitConfig(String propertiesFileNames, String xmlFileNames, String tableName, String keyColumn, String valueColumn, DataSource dataSource, String configPath) {
        this.propertiesFileNames = propertiesFileNames;
        this.xmlFileNames = xmlFileNames;
        this.tableName = tableName;
        this.keyColumn = keyColumn;
        this.valueColumn = valueColumn;
        this.dataSource = dataSource;
        this.configPath = configPath;
    }

    public String getPropertiesFileNames() {
        return propertiesFileNames;
    }

    public void setPropertiesFileNames(String propertiesFileNames) {
        this.propertiesFileNames = propertiesFileNames;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getXmlFileNames() {
        return xmlFileNames;
    }

    public void setXmlFileNames(String xmlFileNames) {
        this.xmlFileNames = xmlFileNames;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
