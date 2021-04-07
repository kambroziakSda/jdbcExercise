package example.entity;

import java.util.Date;

/**
 * Created by krzysztof on 14.12.17.
 */
public class StudentGrade {

    private Integer id;

    private Integer studentId;

    private Integer value;


    private Date date;

    public StudentGrade(Integer id, Integer studentId, Integer value, Date date) {
        this.id = id;
        this.studentId = studentId;
        this.value = value;
        this.date = date;
    }

    public StudentGrade(Integer studentId, Integer value, Date date) {
        this.studentId = studentId;
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public Integer getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "StudentGrade{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", value=" + value +
                ", date=" + date +
                '}';
    }
}
