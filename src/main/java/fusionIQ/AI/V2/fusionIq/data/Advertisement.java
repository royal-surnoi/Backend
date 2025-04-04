package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adTitle;
    private String adDescription;
    private String adTags;
    private String adS3Url;
    private String adType; // pre-roll, mid-roll, post-roll
    private int adDuration; // in seconds
    private String targetAgeGroup;
    private String targetGender;
    private String location;
    private String category;
    private String adS3Key;
    private String adStatus; // active, inactive
    private LocalDate startDate;
    private LocalDate endDate;
    private String callToActionButton;

    public Advertisement() {
    }

    public Advertisement(Long id, String adTitle, String adDescription, String adTags, String adS3Url, String adType, int adDuration, String targetAgeGroup, String targetGender, String location, String category, String adS3Key, String adStatus, LocalDate startDate, LocalDate endDate, String callToActionButton) {
        this.id = id;
        this.adTitle = adTitle;
        this.adDescription = adDescription;
        this.adTags = adTags;
        this.adS3Url = adS3Url;
        this.adType = adType;
        this.adDuration = adDuration;
        this.targetAgeGroup = targetAgeGroup;
        this.targetGender = targetGender;
        this.location = location;
        this.category = category;
        this.adS3Key = adS3Key;
        this.adStatus = adStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.callToActionButton = callToActionButton;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getAdTags() {
        return adTags;
    }

    public void setAdTags(String adTags) {
        this.adTags = adTags;
    }

    public String getAdS3Url() {
        return adS3Url;
    }

    public void setAdS3Url(String adS3Url) {
        this.adS3Url = adS3Url;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getAdDuration() {
        return adDuration;
    }

    public void setAdDuration(int adDuration) {
        this.adDuration = adDuration;
    }

    public String getTargetAgeGroup() {
        return targetAgeGroup;
    }

    public void setTargetAgeGroup(String targetAgeGroup) {
        this.targetAgeGroup = targetAgeGroup;
    }

    public String getTargetGender() {
        return targetGender;
    }

    public void setTargetGender(String targetGender) {
        this.targetGender = targetGender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdS3Key() {
        return adS3Key;
    }

    public void setAdS3Key(String adS3Key) {
        this.adS3Key = adS3Key;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCallToActionButton() {
        return callToActionButton;
    }

    public void setCallToActionButton(String callToActionButton) {
        this.callToActionButton = callToActionButton;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", adTitle='" + adTitle + '\'' +
                ", adDescription='" + adDescription + '\'' +
                ", adTags='" + adTags + '\'' +
                ", adS3Url='" + adS3Url + '\'' +
                ", adType='" + adType + '\'' +
                ", adDuration=" + adDuration +
                ", targetAgeGroup='" + targetAgeGroup + '\'' +
                ", targetGender='" + targetGender + '\'' +
                ", location='" + location + '\'' +
                ", category='" + category + '\'' +
                ", adS3Key='" + adS3Key + '\'' +
                ", adStatus='" + adStatus + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", callToActionButton='" + callToActionButton + '\'' +
                '}';
    }
}
