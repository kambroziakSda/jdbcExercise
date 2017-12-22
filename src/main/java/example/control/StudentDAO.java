package example.control;

import example.entity.Student;
import example.DatabaseConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 14.12.17.
 */
public class StudentDAO {

    public List<Student> getAllStudents() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from student");
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                Student student = createStudent(resultSet);
                students.add(student);
            }
            return students;
        }
    }

    private Student createStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setAverageGrade(resultSet.getDouble("averagegrade"));
        student.setName(resultSet.getString("name"));
        student.setBirthday(new Date(resultSet.getDate("birthday").getTime()));
        student.setPassword(resultSet.getString("password"));
        student.setCity(resultSet.getString("city"));
        return student;
    }


    public Optional<Student> findStudentById(Integer id) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student where id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Student> student = Optional.of(createStudent(resultSet));
                return student;
            }
            return Optional.empty();
        }


    }

    public Optional<Student> findStudentByNameAndPassword(String name, String password) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student where name = ? AND password = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Student> student = Optional.of(createStudent(resultSet));
                return student;
            }
            return Optional.empty();
        }
    }

    public void saveStudent(Student student) throws IOException, SQLException {
        String sql = "INSERT INTO student (name,password,city,birthday) VALUES (?,?,?,?)";
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getCity());
            preparedStatement.setDate(4, new java.sql.Date(student.getBirthday().getTime()));
            preparedStatement.executeUpdate();
        }
    }

    public void saveAllStudents(List<Student> students) throws IOException, SQLException {
        String sql = "INSERT INTO student (name,password,city,birthday) VALUES (?,?,?,?)";
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Student student : students) {
                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getPassword());
                preparedStatement.setString(3, student.getCity());
                preparedStatement.setDate(4, new java.sql.Date(student.getBirthday().getTime()));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        }
    }

    public int changeStudentAverageGrade(Integer studentId, Double newAverageGrade) throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();) {
            return changeAverageGradeInternal(studentId, newAverageGrade, connection);
        }

    }

    private int changeAverageGradeInternal(Integer studentId, Double newAverageGrade, Connection connection) throws SQLException {
        String sql = "UPDATE student SET averagegrade = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setDouble(1, newAverageGrade);
            preparedStatement.setInt(2, studentId);
            return preparedStatement.executeUpdate();
        }


    }

    public int changeStudentAverageGrade(Integer studentId, double asDouble, Connection connection) throws SQLException {
        return changeAverageGradeInternal(studentId, asDouble, connection);
    }

    public int updateStudent(Integer studentId, Student newStudent) throws IOException, SQLException {
        String sql = "UPDATE student SET name = ?, password=?, city=?, birthday=?, averagegrade=? WHERE id=?";
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStudent.getName());
            preparedStatement.setString(2, newStudent.getPassword());
            preparedStatement.setString(3, newStudent.getCity());
            preparedStatement.setDate(4, new java.sql.Date(newStudent.getBirthday().getTime()));
            preparedStatement.setDouble(5, newStudent.getAverageGrade());
            preparedStatement.setInt(6, studentId);
            return preparedStatement.executeUpdate();
        }
    }

    public void removeStudent(Integer studentId) throws IOException, SQLException {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        }
    }
}
