
package fusionIQ.AI.V2.fusionIq.data;
import jakarta.persistence.*;
@Entity
@Table(name = "Institute_Class_Room_Students")
public class InstituteClassRoomStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id",nullable = false)
    private InstituteStudent student;
    @ManyToOne
    @JoinColumn(name = "classroom_id",nullable = false)
    private InstituteClass classroom;
    @ManyToOne
    @JoinColumn(name = "teacher_id",nullable = false)
    private InstituteTeacher teacher;
    @Column(name = "active", nullable = false)
    private boolean active;
    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public InstituteStudent getStudent() {
        return student;
    }
    public void setStudent(InstituteStudent student) {
        this.student = student;
    }
    public InstituteClass getClassroom() {
        return classroom;
    }
    public void setClassroom(InstituteClass classroom) {
        this.classroom = classroom;
    }
    public InstituteTeacher getTeacher() {
        return teacher;
    }
    public void setTeacher(InstituteTeacher teacher) {
        this.teacher = teacher;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public InstituteClassRoomStudents(Long id, InstituteStudent student, InstituteClass classroom, InstituteTeacher teacher, boolean active) {
        this.id = id;
        this.student = student;
        this.classroom = classroom;
        this.teacher = teacher;
        this.active = active;
    }
    public InstituteClassRoomStudents() {
    }
}
 
 