package z.cube.param.handler;

import org.junit.Before;
import org.junit.Test;
import z.cube.param.config.InitConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesConfigHandlerTest {
    private PropertiesConfigHandler propertiesConfigHandler;

    private InitConfig buildInitConfig(){
        InitConfig ic=new InitConfig();
        ic.setPropertiesFileNames("config/merge.properties");
        return ic;
    }
    @Before
    public void setUp() throws Exception {

        this.propertiesConfigHandler=new PropertiesConfigHandler();
        this.propertiesConfigHandler.init(buildInitConfig());
    }


    @Test
    public void testGetValue() throws Exception {
        Object value= this.propertiesConfigHandler.getValue("window.width");
        assertThat(value).isNotNull().isEqualTo("500");

        Object value2= this.propertiesConfigHandler.getValue("colors.background");
        assertThat(value2).isNotNull().isEqualTo("#FFFFFF");

        Object value3= this.propertiesConfigHandler.getValue("name.length");
        assertThat(value3).isNull();
    }
}