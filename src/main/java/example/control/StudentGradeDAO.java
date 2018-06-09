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

    /**
     * TODO zad. 9 - Uzupełnij implementacje tak aby metoda zapisywała ocene studenta do bazy i zwracala ilosc dodanych rekordow
     * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.saveStudentGradeTest
     */
    public int saveStudentGrade(StudentGrade studentGrade) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            return saveStudentGradeInternal(studentGrade, connection);
        }
    }

    private int saveStudentGradeInternal(StudentGrade studentGrade, Connection connection) throws SQLException {
        String sql = "INSERT INTO studentgrade (value, date, studentid) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentGrade.getValue());
            preparedStatement.setDate(2, new Date(studentGrade.getDate().getTime()));
            preparedStatement.setInt(3, studentGrade.getStudentId());
            return preparedStatement.executeUpdate();
        }

    }

    /**
     * TODO zad. 9.1 - Uzupełnij implementacje tak aby metoda robiła to co w zad 9 ale na połączeniu otrzymywanym jako parametr
     */
    public int saveStudentGrade(StudentGrade studentGrade, Connection connection) throws IOException, SQLException {
        return saveStudentGradeInternal(studentGrade, connection);
    }

    /**
     * TODO zad. 10 - Uzupełnij implementacje tak aby metoda zwracała wszystkie oceny stuenta o zadanym id
     * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAllStudentGradesTest
     */
    public List<StudentGrade> getAllGradesByStudentId(Integer studentId) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            List<StudentGrade> allSudentGrades = getStudentGrades(studentId, connection);
            return allSudentGrades;
        }

    }

    private List<StudentGrade> getStudentGrades(Integer studentId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM studentgrade where studentid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<StudentGrade> allSudentGrades = new ArrayList<>();
            while (resultSet.next()) {
                addGrade(resultSet, allSudentGrades);
            }
            return allSudentGrades;
        }

    }

    /**
     * TODO zad. 10.1 - Uzupełnij implementacje tak aby metoda robiła to co w zad 10 ale na połączeniu otrzymywanym jako parametr
     */
    public List<StudentGrade> getAllGradesByStudentId(Integer studentId, Connection connection) throws SQLException {
        return getStudentGrades(studentId, connection);
    }

    /**
     * TODO zad. 11 - Uzupełnij implementacje tak aby metoda zwracala oceny wszystkich studentów z zadanego miasta
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAllStudentGradesFromGdanskTest
     */
    public List<StudentGrade> getAllStudentGradesFromCity(String city) throws IOException, SQLException {
        String sql = "SELECT * FROM studentgrade sg join student s on sg.studentid = s.id where s.city = ?";
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<StudentGrade> studentGrades = new ArrayList<>();
            while (resultSet.next()) {
                addGrade(resultSet, studentGrades);
            }
            return studentGrades;
        }


    }

    /**
     * TODO zad. 12 - Uzupełnij implementacje tak aby metoda zwracala srednia ocene studentów z zadanego miasta
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAverageStudentGradeFromGdansk
     * Uwaga: W implementacji nalezy wykorzystać funkcje sql avg oraz AS
     * * @see <https://www.w3schools.com/sql/sql_count_avg_sum.asp">AVG</a>
     */
    public Optional<Double> getAverageStudentGradeFromCity(String city) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT AVG(VALUE) as avg_value FROM studentgrade sg join student s on sg.studentid = s.id where s.city = ? GROUP BY s.city")) {
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return Optional.of(resultSet.getDouble("avg_value"));
            }
            return Optional.empty();
        }
    }


    private void addGrade(ResultSet resultSet, List<StudentGrade> studentGrades) throws SQLException {
        StudentGrade studentGrade = new StudentGrade(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("value"),
                resultSet.getDate("date"));
        studentGrades.add(studentGrade);
    }
}
