package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcExerciseTest {

    @Test
    public void test() throws IOException, SQLException {
        DatabaseUtil.prepareDatabase();
        Connection connection = DatabaseConnectionProvider.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from example_users where name ='Jan' AND password=''");

        while (resultSet.next()){
            System.out.println(resultSet.getString(0));
        }
    }

}