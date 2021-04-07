package example.control;

import example.entity.StudentGrade;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
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
        return -1;
    }


    /**
     * TODO zad. 9.1 - Uzupełnij implementacje tak aby metoda robiła to co w zad 9 ale na połączeniu otrzymywanym jako parametr
     */
    public int saveStudentGrade(StudentGrade studentGrade, Connection connection) throws IOException, SQLException {
        return -1;
    }

    /**
     * TODO zad. 10 - Uzupełnij implementacje tak aby metoda zwracała wszystkie oceny stuenta o zadanym id
     * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAllStudentGradesTest
     */
    public List<StudentGrade> getAllGradesByStudentId(Integer studentId) throws IOException, SQLException {
        return Collections.emptyList();
    }

    /**
     * TODO zad. 10.1 - Uzupełnij implementacje tak aby metoda robiła to co w zad 10 ale na połączeniu otrzymywanym jako parametr
     */
    public List<StudentGrade> getAllGradesByStudentId(Integer studentId, Connection connection) throws SQLException {
        return Collections.emptyList();
    }


    /**
     * TODO zad. 11 - Uzupełnij implementacje tak aby metoda zwracala oceny wszystkich studentów z zadanego miasta
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAllStudentGradesFromGdanskTest
     */
    public List<StudentGrade> getAllStudentGradesFromCity(String city) throws IOException, SQLException {
        return Collections.emptyList();
    }

    /**
     * TODO zad. 12 - Uzupełnij implementacje tak aby metoda zwracala srednia ocene studentów z zadanego miasta
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.StudentGradeDAOTest.getAverageStudentGradeFromGdansk
      * Uwaga: W implementacji nalezy wykorzystać funkcje sql avg oraz AS
     * * @see <https://www.w3schools.com/sql/sql_count_avg_sum.asp">AVG</a>
     */
    public Optional<Double> getAverageStudentGradeFromCity(String city) throws IOException, SQLException {
        return Optional.empty();
    }

}
