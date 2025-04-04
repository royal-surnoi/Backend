package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class InstituteClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_subject", nullable = false)
    private String classSubject;

    @Column(name = "class", nullable = false)
    private String classGrade;

    @Column(name = "class_section", nullable = false)
    private String classSection;

    @Column(name = "class_academic_year_start", nullable = false)
    private LocalDate academicYearStart;

    @Column(name = "class_academic_year_end", nullable = false)
    private LocalDate academicYearEnd;

    @ManyToOne
    @JoinColumn(name = "teacher_id",nullable = false)
    private InstituteTeacher teacher;

    @ManyToOne
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;



    public Long getid() {
        return id;
    }

    public void setid(Long classId) {
        this.id = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(String classSubject) {
        this.classSubject = classSubject;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public LocalDate getAcademicYearStart() {
        return academicYearStart;
    }

    public void setAcademicYearStart(LocalDate academicYearStart) {
        this.academicYearStart = academicYearStart;
    }

    public LocalDate getAcademicYearEnd() {
        return academicYearEnd;
    }

    public void setAcademicYearEnd(LocalDate academicYearEnd) {
        this.academicYearEnd = academicYearEnd;
    }

    public InstituteTeacher getTeacher() {
        return teacher;
    }

    public void setTeacher(InstituteTeacher teacher) {
        this.teacher = teacher;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public InstituteClass(Long id, String className, String classSubject, String classGrade, String classSection, LocalDate academicYearStart, LocalDate academicYearEnd, InstituteTeacher teacher, Institute institute) {
        this.id = id;
        this.className = className;
        this.classSubject = classSubject;
        this.classGrade = classGrade;
        this.classSection = classSection;
        this.academicYearStart = academicYearStart;
        this.academicYearEnd = academicYearEnd;
        this.teacher = teacher;
        this.institute = institute;
    }

    public InstituteClass() {
    }
}