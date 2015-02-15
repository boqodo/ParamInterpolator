package z.cube.param.handler;

import org.junit.Before;
import org.junit.Test;
import z.cube.param.config.InitConfig;
import z.cube.param.util.DataSourceInitializer;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseConfigHandlerTest {
    private DatabaseConfigHandler databaseConfigHandler;

    @Before
    public void setUp() throws Exception {
        databaseConfigHandler=new DatabaseConfigHandler();
        InitConfig initConfig = buildInitConfig();

        String fileName="script/config-init.sql";

        DataSourceInitializer dsi=DataSourceInitializer.H2MEM_DATASOURCE;
        DataSource ds=dsi.initScript(fileName);


        initConfig.setDataSource(ds);
        databaseConfigHandler.init(initConfig);
    }



    private InitConfig buildInitConfig(){
        InitConfig ic=new InitConfig();
        ic.setTableName("CONFIGURATION");
        ic.setKeyColumn("KEY");
        ic.setValueColumn("value");
        return ic;
    }

    @Test
    public void testGetValue() throws Exception {
        Object value= this.databaseConfigHandler.getValue("First");
        assertThat(value).isNotNull().isEqualTo("1");

        Object value2= this.databaseConfigHandler.getValue("Second");
        assertThat(value2).isNotNull().isEqualTo("2");

        Object value3= this.databaseConfigHandler.getValue("Third");
        assertThat(value3).isNotNull().isEqualTo("3");
        Object value4= this.databaseConfigHandler.getValue("Four");
        assertThat(value4).isNull();
    }
}