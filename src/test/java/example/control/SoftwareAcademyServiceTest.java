package example.control;

import example.DatabaseUtil;
import example.entity.StudentGrade;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by krzysztof on 16.12.17.
 */
public class SoftwareAcademyServiceTest {

    private SoftwareAcademyService sut = new SoftwareAcademyService();

    private static ScriptRunner scriptRunner;

    @Before
    public void setUp() throws IOException, SQLException {
        prepareDatabaseForTest();
    }

    private void prepareDatabaseForTest() throws SQLException, IOException {
        scriptRunner = DatabaseUtil.prepareDatabase();
    }

    @Test
    public void addStudentGradeAndUpdateAverageTest() throws IOException, SQLException {
        StudentGrade studentGrade = new StudentGrade(1, 4, new Date());

        sut.addStudentGradeAndUpdateAverage(studentGrade);

        Double averageGradeFromStudent = sut.getStudentDAO().findStudentById(1).get().getAverageGrade();
        Double averageGradeFromGrades = sut.getStudentGradeDAO().getAllGradesByStudentId(1).stream().mapToInt(StudentGrade::getValue)
                .average().getAsDouble();
        System.out.println("Average grade is: " + averageGradeFromGrades);
        Assertions.assertThat(averageGradeFromStudent).isEqualTo(averageGradeFromGrades);
        Assertions.assertThat(averageGradeFromStudent).isEqualTo(4.5d);
    }

    @Test(expected = SQLException.class)
    public void testThrowExceptionWhenOneOutOfBoundGradePresent() throws IOException, SQLException {
        StudentGrade grade = new StudentGrade(1, 4, new Date());
        StudentGrade grade2 = new StudentGrade(1, 1, new Date());
        List<StudentGrade> studentGrades = Arrays.asList(grade, grade2);

        sut.addStudentGradesAndUpdateAverage(studentGrades);
    }

    @Test
    public void addStudentGradesWithOneOutOfBoundAndGetAverageTest() throws IOException, SQLException {
        StudentGrade grade = new StudentGrade(1, 4, new Date());
        StudentGrade grade2 = new StudentGrade(1, 6, new Date());
        List<StudentGrade> studentGradesToSave = Arrays.asList(grade, grade2);

        Double averageGradeFromStudent = sut.addStudentGradesAndGetAverage(studentGradesToSave);

        double averageGradeFromGrades = getStudentAverageGradeFromGrades();
        Assertions.assertThat(averageGradeFromStudent).isEqualTo(averageGradeFromGrades);
    }

    @Test
    public void addStudentGradesAndGetAverageTest() throws IOException, SQLException {
        StudentGrade grade = new StudentGrade(1, 2, new Date());
        StudentGrade grade2 = new StudentGrade(1, 5, new Date());
        List<StudentGrade> studentGradesToSave = Arrays.asList(grade, grade2);
        Double averageGradeFromStudent = sut.addStudentGradesAndGetAverage(studentGradesToSave);
        double averageGradeFromGrades = getStudentAverageGradeFromGrades();

        Assertions.assertThat(averageGradeFromStudent).isEqualTo(averageGradeFromGrades);
    }

    private double getStudentAverageGradeFromGrades() throws IOException, SQLException {
        List<StudentGrade> allGradesByStudentId = sut.getStudentGradeDAO().getAllGradesByStudentId(1);
        return allGradesByStudentId.stream().mapToInt(StudentGrade::getValue).average().getAsDouble();
    }

    @After
    public void after() throws SQLException {
        scriptRunner.closeConnection();
    }

}