package example.control;

import example.entity.StudentGrade;
import example.DatabaseConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 14.12.17.
 */
public class StudentGradeDAO {

    public int saveStudentGrade(StudentGrade studentGrade) throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        int update = saveStudentGradeInternal(studentGrade, connection);
        connection.close();
        return update;
    }

    private int saveStudentGradeInternal(StudentGrade studentGrade, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO studentgrade (value, date, studentid) VALUES (?,?,?)");
        preparedStatement.setInt(1, studentGrade.getValue());
        preparedStatement.setDate(2, new Date(studentGrade.getDate().getTime()));
        preparedStatement.setInt(3, studentGrade.getStudentId());
        return preparedStatement.executeUpdate();
    }

    public int saveStudentGrade(StudentGrade studentGrade, Connection connection) throws IOException, SQLException {
        int update = saveStudentGradeInternal(studentGrade, connection);
        return update;
    }

    public List<StudentGrade> getAllGradesByStudentId(Integer studentId) throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        List<StudentGrade> allSudentGrades = getStudentGrades(studentId, connection);
        connection.close();
        return allSudentGrades;
    }

    private List<StudentGrade> getStudentGrades(Integer studentId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM studentgrade where studentid = ?");
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<StudentGrade> allSudentGrades = new ArrayList<>();
        while (resultSet.next()) {
            addGrade(resultSet, allSudentGrades);
        }
        return allSudentGrades;
    }

    public List<StudentGrade> getAllGradesByStudentId(Integer studentId, Connection connection) throws SQLException {
        return getStudentGrades(studentId, connection);
    }

    public List<StudentGrade> getAllStudentGradesFromCity(String city) throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM studentgrade sg join student s on sg.studentid = s.id where s.city = ?");
        preparedStatement.setString(1, city);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<StudentGrade> studentGrades = new ArrayList<>();
        while (resultSet.next()) {
            addGrade(resultSet, studentGrades);
        }
        connection.close();
        return studentGrades;
    }

    public Optional<Double> getAverageStudentGradeFromCity(String city) throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT AVG(VALUE) as avg_value FROM studentgrade sg join student s on sg.studentid = s.id where s.city = ? GROUP BY s.city");
        preparedStatement.setString(1, city);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            return Optional.of(resultSet.getDouble("avg_value"));
        }
        connection.close();
        return Optional.empty();
    }


    private void addGrade(ResultSet resultSet, List<StudentGrade> studentGrades) throws SQLException {
        StudentGrade studentGrade = new StudentGrade(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("value"),
                resultSet.getDate("date"));
        studentGrades.add(studentGrade);
    }
}
