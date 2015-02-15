package z.cube.param.util;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceUtils {

    public static DataSource getH2DataSource(){
        JdbcDataSource dataSource=new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_mem");
        dataSource.setUser("sa");
        return dataSource;
    }
    public static void executeSql(DataSource dataSource,String sql){
        try {
            Statement statement = dataSource.getConnection().createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
