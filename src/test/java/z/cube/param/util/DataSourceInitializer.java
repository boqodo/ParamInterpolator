package z.cube.param.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataSourceInitializer {

    public static final DataSourceInitializer H2MEM_DATASOURCE = h2MemDataSource();
    private DataSource dataSource;

    public DataSourceInitializer(DataSource dataSource) {
        this.dataSource=dataSource;
    }


    private static final DataSourceInitializer h2MemDataSource(){
        JdbcDataSource dataSource=new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_mem");
        dataSource.setUser("sa");
        DataSourceInitializer dsi=new DataSourceInitializer(dataSource);
        return dsi;
    }

    public DataSource initScript(String fileName) {
        try {
            List<String> sqls=readFile(fileName);
            Statement statement = this.dataSource.getConnection().createStatement();
            for (String sql:sqls){
                if(StringUtils.isNotBlank(sql)){
                    statement.addBatch(sql);
                }
            }
            int[] count=statement.executeBatch();
            statement.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(),e);
        }

        return this.dataSource;
    }

    private List<String> readFile(String fileName) throws IOException {
        InputStream input=DataSourceInitializer.class.getClassLoader().getResourceAsStream(fileName);
        return IOUtils.readLines(input);
    }
}
