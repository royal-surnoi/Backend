package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String criteriaName;

    private int maxPoints;

    @OneToMany(mappedBy = "criteria")
    private List<Grade> grades;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Criteria(Long id, String criteriaName, int maxPoints, List<Grade> grades) {
        this.id = id;
        this.criteriaName = criteriaName;
        this.maxPoints = maxPoints;
        this.grades = grades;
    }

    public Criteria() {
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "id=" + id +
                ", criteriaName='" + criteriaName + '\'' +
                ", maxPoints=" + maxPoints +
                ", grades=" + grades +
                '}';
    }
}