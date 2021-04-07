package example.control;

import example.DatabaseConnectionProvider;
import example.entity.Student;

import java.io.IOException;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 14.12.17.
 */
public class StudentDAO {


    /**
     * TODO: zad. 1 - Uzupelnij implementacje tak, aby metoda zwracała wszystkich studentów w bazie
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.testGetAllStudents
     */
    public List<Student> getAllStudents() throws IOException, SQLException {
        return Collections.emptyList();
    }

    /**
     * TODO: zad. 2 - Uzupelnij implementacje tak, aby metoda zwracała studenta o zadanym Id
     * lub pustego Optionala jeśli student o zadanym id nie istnieje w bazie
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.testGetStudentById
     * Uwaga: w implementacji można wykorzystać:
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#first--">resultSet#first()</a>
     * lub
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html#next--">resultSet#next()</a>
     **/
    public Optional<Student> findStudentById(Integer id) throws IOException, SQLException {
        return Optional.empty();
    }

    /**
     * TODO: zad. 3 - Uzupelnij implementacje tak, aby metoda zwracała studenta o zadanym name i password
     * lub pustego Optionala jeśli taki student nie istnieje w bazie
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.testFindStudentByNameAndPassword
     **/
    public Optional<Student> findStudentByNameAndPassword(String name, String password) throws IOException, SQLException {
        return Optional.empty();
    }

    /**
     * TODO: zad. 4 - Uzupelnij implementacje tak, aby metoda zapisywala studenta do bazy
     * Uwaga: Nie zapisujemy pola id - jest ono generowane automatycznie przez bazę
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.saveStudentTest
     **/
    public void saveStudent(Student student) throws IOException, SQLException {
    }

    /**
     * TODO: zad. 5 - Uzupelnij implementacje tak, aby metoda zapisywala wszystkich stuentów studenta do bazy
     * Uwaga: Nie zapisujemy pola id - jest ono generowane automatycznie przez bazę
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.saveAllStudentTest
     * Implementację można oprzeć na
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/PreparedStatement.html#addBatch--">PreparedStatement#addBatch</a>
     * lub ewentualnie metodzie {@link StudentDAO#saveStudent(Student)}
     **/
    public void saveAllStudents(List<Student> students) throws IOException, SQLException {
    }

    /**
     * TODO: zad. 6 - Uzupelnij implementacje tak, aby metoda zmieniala średnią ocenę studenta o zadanym id i zwracała ilość zaktaulizowanych
     * rekordów w bazie (Studentów)
     * Uwaga: Należy skorzystać z wartości zwracanej przez metodę
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/PreparedStatement.html#executeUpdate--">PreparedStatement#addBatch</a>
     *
     * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.testUpdateAverageGrade
     *
     **/
    public int changeStudentAverageGrade(Integer studentId, Double newAverageGrade) throws IOException, SQLException {
        return -1;
    }

    /**
     * TODO: zad. 6.1 - Uzupelnij implementacje tak, aby metoda działała identycznie jak {@link StudentDAO#changeStudentAverageGrade(Integer, Double)}
     * z tą różnicą że na połączeniu ({@link Connection}) przyjmowanym z zewnątrz
     *
     **/
    public int changeStudentAverageGrade(Integer studentId, double asDouble, Connection connection) throws SQLException {
        return -1;
    }

    /**
     * TODO zad. 7 - Uzupełnij implementacje tak aby metoda aktualizowała studenta w bazie o zadanym id wartosciami przekazanymi w obiekcie newStudent
     * Metoda powinna zwracac ilosc zaktualizowanych wierszy
     * Uwaga: nie aktualizuje pola id
     * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.testUpdateStudent
     *
     **/
    public int updateStudent(Integer studentId, Student newStudent) throws IOException, SQLException {
        return -1;
    }

    /**
     * TODO zad. 8 - Uzupełnij implementacje tak aby metoda usuwała studenta o zadanym id z bazy
     * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentDAOTest.removeStudentTest
     *
     */
    public int removeStudent(Integer studentId) throws IOException, SQLException {
        return -1;
    }


/*
        Connection connection = DatabaseConnectionProvider.getConnection();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM student where name = " + name + " AND password = " + password);
 */

}
