package example.control;

import example.DatabaseUtil;
import example.entity.StudentGrade;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 16.12.17.
 */
public class StudentGradeDAOTest {

    private StudentGradeDAO sut = new StudentGradeDAO();

    private static ScriptRunner scriptRunner;

    @Before
    public void setUp() throws IOException, SQLException {
        prepareDatabaseForTest();
    }

    private void prepareDatabaseForTest() throws SQLException, IOException {
        scriptRunner = DatabaseUtil.prepareDatabase();
    }

    @Test
    public void saveStudentGradeTest() throws IOException, SQLException {
        StudentGrade studentGrade = new StudentGrade(1, 1, 4, new Date());
        sut.saveStudentGrade(studentGrade);
        List<StudentGrade> allGradesByStudentId = sut.getAllGradesByStudentId(1);
        Assertions.assertThat(allGradesByStudentId).size().isEqualTo(2);
    }

    @Test
    public void getAllStudentGradesTest() throws IOException, SQLException {
        List<StudentGrade> allGradesByStudentId = sut.getAllGradesByStudentId(1);
        Assertions.assertThat(allGradesByStudentId).size().isEqualTo(1);
    }

    @Test
    public void getAllStudentGradesFromGdansk() throws IOException, SQLException {
        sut.saveStudentGrade(new StudentGrade(3, 3, new Date()));
        List<StudentGrade> allGradesFromGdansk = sut.getAllStudentGradesFromCity("Gdańsk");
        Assertions.assertThat(allGradesFromGdansk)
                .size()
                .isEqualTo(2);
    }

    @Test
    public void getAverageStudentGradeFromGdansk() throws IOException, SQLException {
        sut.saveStudentGrade(new StudentGrade(3, 3, new Date()));
        Optional<Double> averageStudentGradeFromGdansk = sut.getAverageStudentGradeFromCity("Gdańsk");
        Assertions.assertThat(averageStudentGradeFromGdansk)
                .isPresent()
                .contains(4d);
    }

    @Test
    public void getAverageStudentGradeFromSopot() throws IOException, SQLException {
        Optional<Double> averageStudentGradeFromSopot = sut.getAverageStudentGradeFromCity("Sopot");
        Assertions.assertThat(averageStudentGradeFromSopot)
                .isEmpty();
    }

    @After
    public void after() throws SQLException {
        scriptRunner.closeConnection();
    }
}