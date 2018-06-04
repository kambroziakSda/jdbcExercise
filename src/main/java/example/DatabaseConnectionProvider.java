package example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by krzysztof on 13.12.17.
 */
public class DatabaseConnectionProvider {

    public static Connection getConnection() throws SQLException, IOException {
        InputStream inputStream = DatabaseConnectionProvider.class.getClassLoader().getResourceAsStream("mysql/mysql.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

}
