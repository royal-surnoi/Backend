package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity

public class UserSkills {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;


    @JsonProperty("skillName")

    @Column(name = "skill_name")

    private String skillName;

    @Enumerated(EnumType.STRING)

    @JsonProperty("level")

    @Column(name = "proficiency_level")

    private SkillLevel level;

    @ManyToOne

    @JoinColumn(name = "user_id")

    private User user;



    public enum SkillLevel {

        AVERAGE,

        SKILLED,

        EXPERT

    }

    public UserSkills(Long id, String name, String skillName, SkillLevel level, User user) {

        this.id = id;


        this.skillName = skillName;

        this.level = level;

        this.user = user;

    }

    public Long getId() {

        return id;

    }

    public void setId(Long id) {

        this.id = id;

    }


    public String getSkillName() {

        return skillName;

    }

    public void setSkillName(String skillName) {

        this.skillName = skillName;

    }

    public SkillLevel getLevel() {

        return level;

    }

    public void setLevel(SkillLevel level) {

        this.level = level;

    }

    public User getUser() {

        return user;

    }

    public void setUser(User user) {

        this.user = user;

    }

    @Override

    public String toString() {

        return "UserSkills{" +

                "id=" + id +
                ", skillName='" + skillName + '\'' +

                ", level=" + level +

                ", user=" + user +

                '}';

    }

    public UserSkills() {

    }

}
