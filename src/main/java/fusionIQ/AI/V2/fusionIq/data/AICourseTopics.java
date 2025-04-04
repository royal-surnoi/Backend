package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_course_topics")
public class AICourseTopics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String mainTopicTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String subTopic;

    @Lob
    @Column(columnDefinition = "LONGTEXT", name = "explanation", nullable = false)
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "ai_course_plan_id", nullable = false)
    private AICoursePlan aiCoursePlan;

    @Lob
    @Column(name = "video_s3key", columnDefinition = "LONGTEXT")
    private String videoS3Key;

    @Lob
    @Column(name = "video_s3url", columnDefinition = "LONGTEXT")
    private String videoS3Url;

    @Column(name = "video_course_id")
    private Long videoCourseId;
    public AICourseTopics() {
    }


    public AICourseTopics(Long id, int weekNumber, String mainTopicTitle, String subTopic, String explanation,
                          AICoursePlan aiCoursePlan, String videoS3Key, String videoS3Url, Long videoCourseId) {
        this.id = id;
        this.weekNumber = weekNumber;
        this.mainTopicTitle = mainTopicTitle;
        this.subTopic = subTopic;
        this.explanation = explanation;
        this.aiCoursePlan = aiCoursePlan;
        this.videoS3Key = videoS3Key;
        this.videoS3Url = videoS3Url;
        this.videoCourseId = videoCourseId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getMainTopicTitle() {
        return mainTopicTitle;
    }

    public void setMainTopicTitle(String mainTopicTitle) {
        this.mainTopicTitle = mainTopicTitle;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public AICoursePlan getAiCoursePlan() {
        return aiCoursePlan;
    }

    public void setAiCoursePlan(AICoursePlan aiCoursePlan) {
        this.aiCoursePlan = aiCoursePlan;
    }

    public String getVideoS3Key() {
        return videoS3Key;
    }

    public void setVideoS3Key(String videoS3Key) {
        this.videoS3Key = videoS3Key;
    }

    public String getVideoS3Url() {
        return videoS3Url;
    }

    public void setVideoS3Url(String videoS3Url) {
        this.videoS3Url = videoS3Url;
    }

    public Long getVideoCourseId() {
        return videoCourseId;
    }

    public void setVideoCourseId(Long videoCourseId) {
        this.videoCourseId = videoCourseId;
    }

    @Override
    public String toString() {
        return "AICourseTopics{" +
                "id=" + id +
                ", weekNumber=" + weekNumber +
                ", mainTopicTitle='" + mainTopicTitle + '\'' +
                ", subTopic='" + subTopic + '\'' +
                ", explanation='" + explanation + '\'' +
                ", aiCoursePlan=" + aiCoursePlan +
                ", videoS3Key='" + videoS3Key + '\'' +
                ", videoS3Url='" + videoS3Url + '\'' +
                ", videoCourseId=" + videoCourseId +
                '}';
    }
}