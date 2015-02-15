package z.cube.param.handler;

import org.junit.Before;
import org.junit.Test;
import z.cube.param.config.InitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class XmlConfigHandlerTest {
    private XmlConfigHandler xmlConfigHandler;
    @Before
    public void setUp() throws Exception {
        xmlConfigHandler=new XmlConfigHandler();
        xmlConfigHandler.init(buildInitConfig());
    }
    private InitConfig buildInitConfig(){
        InitConfig ic=new InitConfig();
        ic.setXmlFileNames("config/test.xml");
        return ic;
    }

    @Test
    public void testGetValue() throws Exception {
        Object value= this.xmlConfigHandler.getValue("datetime");
        assertThat(value).isNotNull().isEqualTo("20140715");

        Object value2= this.xmlConfigHandler.getValue("townname");
        assertThat(value2).isNotNull().isEqualTo("330502201");

        Object value3= this.xmlConfigHandler.getValue("req.datetime");
        assertThat(value3).isNotNull().isEqualTo("20150125");

        //不支持取属性上的值
        Object value4= this.xmlConfigHandler.getValue("req.sender");
        assertThat(value4).isNull();
    }
}