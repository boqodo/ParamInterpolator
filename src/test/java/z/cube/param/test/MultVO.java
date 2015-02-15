package z.cube.param.test;

import z.cube.param.config.Config;
import z.cube.param.config.SourceType;

@Config(source =SourceType.XML)
public class MultVO {
    @Config(key="msgnumber",source = SourceType.XML)
    private Integer xml;
    @Config(key="colors.background",source = SourceType.PROPERTIES)
    private String properties;
    @Config(key="Third")
    private String database;
    @Config(key="number",source = SourceType.FLEX)
    private Long flexsession;

    private Integer townname;

    public Integer getXml() {
        return xml;
    }

    public void setXml(Integer xml) {
        this.xml = xml;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Long getFlexsession() {
        return flexsession;
    }

    public void setFlexsession(Long flexsession) {
        this.flexsession = flexsession;
    }

    public Integer getTownname() {
        return townname;
    }

    public void setTownname(Integer townname) {
        this.townname = townname;
    }
}
