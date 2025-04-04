package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

@Entity
public class AiSkillProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;


    private String skillName;


    private SkillLevel skillLevel;


    public enum SkillLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public AiSkillProgress(Long id, Long userId, String skillName, SkillLevel skillLevel) {
        this.id = id;
        this.userId = userId;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", userId=" + userId +
                ", skillName='" + skillName + '\'' +
                ", skillLevel=" + skillLevel +
                '}';
    }

    public AiSkillProgress() {
    }
}
