package example;

import example.entity.Student;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.assertj.core.api.Condition;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by krzysztof on 14.12.17.
 */
public class TestHelper {

    public  static  List<Condition<Student>> allStudentConditions() {
        Condition<Student> idIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getId()).isPresent(), "Id is null!");
        Condition<Student> nameIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getName()).isPresent(), "Name is null!");
        Condition<Student> cityIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getCity()).isPresent(), "City is null!");
        Condition<Student> passwordIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getPassword()).isPresent(), "Password is null!");
        Condition<Student> averageGradeIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getAverageGrade()).isPresent(), "AverageGrade is null!");
        Condition<Student> birthDayGradeIsNotNull = new Condition<>(student -> Optional.ofNullable(student.getBirthday()).isPresent(), "Birthday is null!");

        return Arrays
                .asList(idIsNotNull, nameIsNotNull, cityIsNotNull, passwordIsNotNull, averageGradeIsNotNull, birthDayGradeIsNotNull);
    }

    public static Student expectedStudentWithId1() {
        Student expectedStudent = new Student();
        expectedStudent.setId(1);
        expectedStudent.setName("Jan");
        expectedStudent.setAverageGrade(5d);
        expectedStudent.setPassword("jan123");
        expectedStudent.setCity("Gdańsk");
        expectedStudent.setBirthday(Date.from(LocalDate.of(1987, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        return expectedStudent;
    }

    public static Student newStudent() {
        Student newStudent = new Student();
        newStudent.setCity("Warszawa");
        newStudent.setBirthday(Date.from(LocalDate.of(1990, 4, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        newStudent.setPassword("janusz123");
        newStudent.setName("Janusz");
        newStudent.setAverageGrade(0d);
        newStudent.setId(4);
        return newStudent;
    }

    public static List<Student> twoNewStudents(){
        Student newStudent = newStudent();

        Student newStudent2 = new Student();
        newStudent2.setCity("Wejherowo");
        newStudent2.setBirthday(Date.from(LocalDate.of(1994, 6, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        newStudent2.setPassword("marek123");
        newStudent2.setName("Marek");
        newStudent2.setAverageGrade(0d);
        newStudent2.setId(5);

        return Arrays.asList(newStudent, newStudent2);
    }



}
