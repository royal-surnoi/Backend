package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
public class SelfIntroductionVideos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String siPersonalS3Key;
    private String siPersonalS3Uri;

    private String siEducationS3Key;
    private String siEducationS3Uri;

    private String siWorkExperienceS3Key;
    private String siWorkExperienceS3Uri;

    private String siAchievementsS3Key;
    private String siAchievementsS3Uri;

    private String siCombinedS3Key;
    private String siCombinedS3Uri;

    private BigInteger selfIntroTimestamp;
    private BigInteger educationTimestamp;
    private BigInteger workExpTimestamp;
    private BigInteger achievementsTimestamp;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public SelfIntroductionVideos() {}


    public SelfIntroductionVideos(Long id, String siPersonalS3Key, String siPersonalS3Uri, String siEducationS3Key, String siEducationS3Uri, BigInteger selfIntroTimestamp,BigInteger workExpTimestamp,BigInteger educationTimestamp,BigInteger achievementsTimestamp, String siWorkExperienceS3Key, String siWorkExperienceS3Uri, String siAchievementsS3Key, String siAchievementsS3Uri, String siCombinedS3Key, String siCombinedS3Uri, User user) {
        this.id = id;
        this.siPersonalS3Key = siPersonalS3Key;
        this.siPersonalS3Uri = siPersonalS3Uri;
        this.siEducationS3Key = siEducationS3Key;
        this.siEducationS3Uri = siEducationS3Uri;
        this.siWorkExperienceS3Key = siWorkExperienceS3Key;
        this.siWorkExperienceS3Uri = siWorkExperienceS3Uri;
        this.siAchievementsS3Key = siAchievementsS3Key;
        this.siAchievementsS3Uri = siAchievementsS3Uri;
        this.siCombinedS3Key = siCombinedS3Key;
        this.siCombinedS3Uri = siCombinedS3Uri;
        this.achievementsTimestamp = achievementsTimestamp;
        this.educationTimestamp = educationTimestamp;
        this.selfIntroTimestamp = selfIntroTimestamp;
        this.workExpTimestamp = workExpTimestamp;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiEducationS3Key() {
        return siEducationS3Key;
    }

    public void setSiEducationS3Key(String siEducationS3Key) {
        this.siEducationS3Key = siEducationS3Key;
    }

    public String getSiEducationS3Uri() {
        return siEducationS3Uri;
    }

    public void setSiEducationS3Uri(String siEducationS3Uri) {
        this.siEducationS3Uri = siEducationS3Uri;
    }

    public String getSiWorkExperienceS3Key() {
        return siWorkExperienceS3Key;
    }

    public void setSiWorkExperienceS3Key(String siWorkExperienceS3Key) {
        this.siWorkExperienceS3Key = siWorkExperienceS3Key;
    }

    public String getSiWorkExperienceS3Uri() {
        return siWorkExperienceS3Uri;
    }

    public void setSiWorkExperienceS3Uri(String siWorkExperienceS3Uri) {
        this.siWorkExperienceS3Uri = siWorkExperienceS3Uri;
    }

    public String getSiAchievementsS3Key() {
        return siAchievementsS3Key;
    }

    public void setSiAchievementsS3Key(String siAchievementsS3Key) {
        this.siAchievementsS3Key = siAchievementsS3Key;
    }

    public String getSiAchievementsS3Uri() {
        return siAchievementsS3Uri;
    }

    public void setSiAchievementsS3Uri(String siAchievementsS3Uri) {
        this.siAchievementsS3Uri = siAchievementsS3Uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSiPersonalS3Key() {
        return siPersonalS3Key;
    }

    public void setSiPersonalS3Key(String siPersonalS3Key) {
        this.siPersonalS3Key = siPersonalS3Key;
    }

    public String getSiPersonalS3Uri() {
        return siPersonalS3Uri;
    }

    public void setSiPersonalS3Uri(String siPersonalS3Uri) {
        this.siPersonalS3Uri = siPersonalS3Uri;
    }

    public String getSiCombinedS3Key() {
        return siCombinedS3Key;
    }

    public BigInteger getAchievementsTimestamp() {
        return achievementsTimestamp;
    }

    public void setAchievementsTimestamp(BigInteger achievementsTimestamp) {
        this.achievementsTimestamp = achievementsTimestamp;
    }

    public BigInteger getEducationTimestamp() {
        return educationTimestamp;
    }

    public void setEducationTimestamp(BigInteger educationTimestamp) {
        this.educationTimestamp = educationTimestamp;
    }

    public BigInteger getSelfIntroTimestamp() {
        return selfIntroTimestamp;
    }

    public void setSelfIntroTimestamp(BigInteger selfIntroTimestamp) {
        this.selfIntroTimestamp = selfIntroTimestamp;
    }

    public BigInteger getWorkExpTimestamp() {
        return workExpTimestamp;
    }

    public void setWorkExpTimestamp(BigInteger workExpTimestamp) {
        this.workExpTimestamp = workExpTimestamp;
    }


    public void setSiCombinedS3Key(String siCombinedS3Key) {
        this.siCombinedS3Key = siCombinedS3Key;
    }

    public String getSiCombinedS3Uri() {
        return siCombinedS3Uri;
    }

    public void setSiCombinedS3Uri(String siCombinedS3Uri) {
        this.siCombinedS3Uri = siCombinedS3Uri;
    }

    @Override
    public String toString() {
        return "SelfIntroductionVideos{" +
                "id=" + id +
                ", siPersonalS3Key='" + siPersonalS3Key + '\'' +
                ", siPersonalS3Uri='" + siPersonalS3Uri + '\'' +
                ", siEducationS3Key='" + siEducationS3Key + '\'' +
                ", siEducationS3Uri='" + siEducationS3Uri + '\'' +
                ", siWorkExperienceS3Key='" + siWorkExperienceS3Key + '\'' +
                ", siWorkExperienceS3Uri='" + siWorkExperienceS3Uri + '\'' +
                ", siAchievementsS3Key='" + siAchievementsS3Key + '\'' +
                ", siAchievementsS3Uri='" + siAchievementsS3Uri + '\'' +
                ", siCombinedS3Key='" + siCombinedS3Key + '\'' +
                ", siCombinedS3Uri='" + siCombinedS3Uri + '\'' +
                ", selfIntroTimestamp=" + selfIntroTimestamp +
                ", educationTimestamp=" + educationTimestamp +
                ", workExpTimestamp=" + workExpTimestamp +
                ", achievementsTimestamp=" + achievementsTimestamp +
                ", user=" + user +
                '}';
    }
}
