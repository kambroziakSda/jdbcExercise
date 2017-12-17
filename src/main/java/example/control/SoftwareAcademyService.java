package example.control;

import example.entity.StudentGrade;
import example.DatabaseConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Created by krzysztof on 14.12.17.
 */
public class SoftwareAcademyService {

    private StudentDAO studentDAO = new StudentDAO();

    private StudentGradeDAO studentGradeDAO = new StudentGradeDAO();

    /**
     * TODO zad. 13 - Uzupełnij implementacje tak aby zapisywala ocene studenta i aktualizowala jego ocene srednia
     * wyliczona ze wszystkich otrzymanych ocen
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.SoftwareAcademyServiceTest.addStudentGradeAndUpdateAverageTest
     * Uwaga: W implementacji nalezy wykorzystać:
     * {@link StudentGradeDAO#saveStudentGrade(StudentGrade)}
     * {@link StudentGradeDAO#getAllGradesByStudentId(Integer)}
     * {@link example.control.StudentDAO#changeStudentAverageGrade(java.lang.Integer, java.lang.Double)}
     **/
    public void addStudentGradeAndUpdateAverage(StudentGrade studentGrade) throws IOException, SQLException {

    }

    /**
     * TODO zad. 14 - Uzupełnij implementacje tak aby najperw zapisywala wszystkie oceny studenta a potem aktualizowala jego ocene srednia
     * wyliczona ze wszystkich otrzymanych ocen
     *  * * Po zaimplementowaniu sukcesem powinien konczyc sie test example.control.SoftwareAcademyServiceTest.testThrowExceptionWhenOneOutOfBoundGradePresent
     * Uwaga: W pierwszej wersji implementacji nalezy wykorzystać:
     * {@link StudentGradeDAO#saveStudentGrade(StudentGrade)}
     **/
    public void addStudentGradesAndUpdateAverage(List<StudentGrade> studentGrades) throws IOException, SQLException {
        if (studentGrades.isEmpty()) {
            return;
        }
        if (studentGrades.stream().map(StudentGrade::getStudentId).distinct().count() > 1) {
            throw new IllegalArgumentException("Only one student grades are allowed!");
        }
    }

    /**
     * TODO zad. 15 - Metoda powiazana jest z testami addStudentGradesWithOneOutOfBoundAndGetAverageTest oraz addStudentGradesAndGetAverageTest
     * Jej implementacja jest kompletna. Zmien implementaje metody {@link SoftwareAcademyService#addStudentGradesAndUpdateAverage(List)}
     * tak aby pewne operacje grupowane były w transkacje
     *
     */
    public Double addStudentGradesAndGetAverage(List<StudentGrade> studentGrades) throws IOException, SQLException {
        try {
            addStudentGradesAndUpdateAverage(studentGrades);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double averageGrade = studentDAO.findStudentById(1).get().getAverageGrade();
        System.out.println(averageGrade);
        return averageGrade;
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    public StudentGradeDAO getStudentGradeDAO() {
        return studentGradeDAO;
    }
}
