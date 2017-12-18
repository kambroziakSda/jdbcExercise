package example;

import example.control.StudentGradeDAO;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by krzysztof on 16.12.17.
 */
public class DatabaseUtil {

    public static ScriptRunner prepareDatabase() throws SQLException, IOException {
        Connection connection = DatabaseConnectionProvider.getSetupConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(new PrintWriter(new FileWriter(new File("log.txt"))));
        scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/postgres_data.sql")));
        scriptRunner.setDelimiter("//");
        //scriptRunner.runScript(new InputStreamReader(StudentGradeDAO.class.getResourceAsStream("/trigger.sql")));
        return scriptRunner;
    }
}
