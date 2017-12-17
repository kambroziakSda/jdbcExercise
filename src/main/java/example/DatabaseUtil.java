package example;

import example.control.StudentGradeDAO;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by krzysztof on 16.12.17.
 */
public class DatabaseUtil {

    public static ScriptRunner prepareDatabase() throws SQLException, IOException {
        Connection connection = getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(new PrintWriter(new FileWriter(new File("log.txt"))));
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/data.sql")));
        scriptRunner.setDelimiter("//");
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/trigger.sql")));
        return scriptRunner;
    }

    private static Connection getConnection() throws IOException, SQLException {
        InputStream inputStream = DatabaseConnectionProvider.class.getClassLoader().getResourceAsStream("database_test.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

}
