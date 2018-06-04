package example;

import example.control.StudentGradeDAO;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Created by krzysztof on 16.12.17.
 */
public class DatabaseUtil {

    public static ScriptRunner prepareDatabase() throws SQLException, IOException {
        return prepareMySqlDatabase();
    }

    public static ScriptRunner prepareMySqlDatabase() throws SQLException, IOException {
        Connection connection = getConnection("mysql/mysql_init.properties");
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(new PrintWriter(new FileWriter(new File("log.txt"))));
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/mysql/mysql_data.sql")));
        scriptRunner.setDelimiter("//");
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/mysql/trigger.sql")));
        return scriptRunner;
    }

    private static ScriptRunner preparePostgresSqlDatabase() throws SQLException, IOException {
        Connection connection = getConnection("postgres/postgres_init.properties");
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(new PrintWriter(new FileWriter(new File("log.txt"))));
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/postgres/postgres_data.sql")));
        return scriptRunner;
    }

    private static Connection getConnection(String fileName) throws IOException, SQLException {
        InputStream inputStream = DatabaseConnectionProvider.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(inputStream);
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }
}
