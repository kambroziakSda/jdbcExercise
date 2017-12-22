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

    public void addStudentGradeAndUpdateAverage(StudentGrade studentGrade) throws IOException, SQLException {
        studentGradeDAO.saveStudentGrade(studentGrade);
        List<StudentGrade> allGradesByStudentId = studentGradeDAO.getAllGradesByStudentId(studentGrade.getStudentId());
        OptionalDouble average = allGradesByStudentId.stream().mapToInt(StudentGrade::getValue).average();
        studentDAO.changeStudentAverageGrade(studentGrade.getStudentId(), average.getAsDouble());
    }

    public void addStudentGradesAndUpdateAverage(List<StudentGrade> studentGrades) throws IOException, SQLException {
        if (studentGrades.isEmpty()) {
            return;
        }
        if (studentGrades.stream().map(StudentGrade::getStudentId).distinct().count() > 1) {
            throw new IllegalArgumentException("Only one student grades are allowed!");
        }
        Connection connection = DatabaseConnectionProvider.getConnection();
        connection.setAutoCommit(false);
        try {
            for (StudentGrade studentGrade : studentGrades) {
                studentGradeDAO.saveStudentGrade(studentGrade, connection);
            }
            Integer studentId = studentGrades.get(0).getStudentId();
            List<StudentGrade> allGradesByStudentId = studentGradeDAO.getAllGradesByStudentId(studentId, connection);
            OptionalDouble average = allGradesByStudentId.stream().mapToInt(StudentGrade::getValue).average();
            studentDAO.changeStudentAverageGrade(studentId, average.getAsDouble(), connection);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }


    public Double addStudentGradesAndGetAverage(List<StudentGrade> studentGrades) throws IOException, SQLException {
        try {
            addStudentGradesAndUpdateAverage(studentGrades);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentDAO.findStudentById(1).get().getAverageGrade();
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    public StudentGradeDAO getStudentGradeDAO() {
        return studentGradeDAO;
    }
}
