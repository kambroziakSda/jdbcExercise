package example.boundary;

import example.DatabaseConnectionProvider;

import java.io.IOException;
import java.sql.*;

/**
 * Created by krzysztof on 13.12.17.
 */
public class JdbcExercise {

    public static void main(String args[]) throws SQLException, IOException {

        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("show databases");
            while (resultSet.next()) {
                String database = resultSet.getString("Database");
                System.out.println(database);
            }

        }
    }


}



