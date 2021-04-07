package example.control;

import example.DatabaseUtil;
import example.TestHelper;
import example.entity.Student;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 14.12.17.
 */
public class StudentDAOTest {

    private StudentDAO sut = new StudentDAO();
    private static ScriptRunner scriptRunner;

    @Before
    public void setUp() throws IOException, SQLException {
        prepareDatabaseForTest();
    }

    private void prepareDatabaseForTest() throws SQLException, IOException {
        scriptRunner = DatabaseUtil.prepareDatabase();
    }

    @Test
    public void testGetAllStudents() throws IOException, SQLException {
        List<Student> allStudents = sut.getAllStudents();
        Assertions.assertThat(allStudents).size().isEqualTo(3);
        TestHelper.allStudentConditions()
                .stream()
                .forEach(connection -> Assertions.assertThat(allStudents).are(connection));

    }

    @Test
    public void testGetStudentById() throws Exception {
        Student expectedStudent = TestHelper.expectedStudentWithId1();
        Optional<Student> studentById = sut.findStudentById(1);
        Assertions.assertThat(studentById)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(expectedStudent);

    }

    @Test
    public void testFindStudentByNameAndPassword() throws IOException, SQLException {
        Student expectedStudent = TestHelper.expectedStudentWithId1();
        Optional<Student> studentByNameAndPassword = sut.findStudentByNameAndPassword("Jan", "jan123");

        Assertions.assertThat(studentByNameAndPassword)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(expectedStudent);

    }

    @Test
    public void saveStudentTest() throws IOException, SQLException {
        Student newStudent = TestHelper.newStudent();
        sut.saveStudent(newStudent);
        Optional<Student> studentById = sut.findStudentById(4);

        Assertions.assertThat(studentById)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(newStudent);

    }

    @Test
    public void saveAllStudentsTest() throws IOException, SQLException {
        sut.saveAllStudents(TestHelper.twoNewStudents());
        Optional<Student> firstStudent = sut.findStudentById(4);
        Assertions.assertThat(firstStudent)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(TestHelper.twoNewStudents().get(0));

        Optional<Student> secondStudent = sut.findStudentById(5);
        Assertions.assertThat(secondStudent)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(TestHelper.twoNewStudents().get(1));

    }

    @Test
    public void testUpdateAverageGrade() throws IOException, SQLException {
        double newAverageGrade = 4d;
        int updatedCount = sut.changeStudentAverageGrade(1, newAverageGrade);
        Assertions.assertThat(updatedCount).isEqualTo(1);
        Optional<Student> studentById = sut.findStudentById(1);
        Condition<Optional<Student>> condition = new Condition<>(student -> student.get().getAverageGrade() == newAverageGrade, "Has averagegrade of: "+newAverageGrade);
        Assertions.assertThat(studentById)
                .isPresent()
                .is(condition);
    }

    @Test
    public void removeStudentTest() throws IOException, SQLException {
        int removed = sut.removeStudent(1);
        Optional<Student> studentById = sut.findStudentById(1);
        Assertions.assertThat(removed).isEqualTo(1);
        Assertions.assertThat(studentById)
                .isEmpty();
    }

    @Test
    public void testUpdateStudent() throws IOException, SQLException {
        Student newStudent = TestHelper.newStudent();
        newStudent.setId(1);
        int updatedCount = sut.updateStudent(1, newStudent);
        Optional<Student> studentById = sut.findStudentById(1);


        Assertions.assertThat(updatedCount).isEqualTo(1);
        Assertions.assertThat(studentById)
                .isPresent()
                .usingFieldByFieldValueComparator()
                .contains(newStudent);
    }


    @After
    public void after() throws SQLException {
        scriptRunner.closeConnection();
    }
}