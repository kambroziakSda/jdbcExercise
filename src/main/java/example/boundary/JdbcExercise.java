package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.sql.*;

/**
 * Created by krzysztof on 13.12.17.
 */
public class JdbcExercise {

    public static void main(String args[]) throws SQLException, IOException {
        ScriptRunner scriptRunner = DatabaseUtil.prepareDatabase();

        Connection connection = DatabaseConnectionProvider.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM animal");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }

        scriptRunner.closeConnection();

    }
}



