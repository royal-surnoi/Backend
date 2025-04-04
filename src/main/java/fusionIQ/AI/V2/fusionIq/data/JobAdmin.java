package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "job_admins")
public class JobAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobAdminName;
    private String jobAdminEmail;
    private String role;
    private String jobAdminCompanyName;

    private String jobAdminPassword;

    @Transient
    private String jobAdminConfirmPassword;


    @Lob
    @Column(name = "company_logo", length = 1024 * 1024)
    private byte[] companyLogo;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String companyDescription;

    private String companyTypeOfIndustry;
    private String companyWebsiteLink;
    private int companyStrength;
    private String companyLocation;

    private String companyLicense;

    @Lob
    @Column(name = "companyAboutDescription", length = 20000)
    private String  companyAboutDescription;

    @Lob
    @Column(name = "companyOverviewDescription", length = 20000)
    private String companyOverviewDescription;

    private double companyLatitude;
    private double companyLongitude;

    private String companyPhoneNumber;

    @Lob
    @Column(name = "company_license_document", length = 1024 * 1024) // Company license document
    private byte[] companyLicenseDocument;

    private String companyGstNumber;

    @Lob
    @Column(name = "company_gst_document", length = 1024 * 1024) // GST document
    private byte[] companyGstDocument;

    private String companyCinNumber;

    @Lob
    @Column(name = "company_cin_document", length = 1024 * 1024) // CIN document
    private byte[] companyCinDocument;

    @Lob
    @Column(name = "description_background", length =2560 * 1440)
    private byte[] descriptionBackground;

    @Lob
    @Column(name = "about_background", length =2560 * 1440)
    private byte[] aboutBackground;

    @Lob
    @Column(name = "add_job_background", length =2560 * 1440)
    private byte[] addJobBackground;

    @Lob
    @Column(name = "overview_background", length =2560 * 1440)
    private byte[] overviewBackground;

    @Lob
    @Column(name = "contact_background", length =2560 * 1440)
    private byte[] contactBackground;

    @OneToMany(mappedBy = "jobAdmin", cascade = CascadeType.ALL) // One JobAdmin can have multiple Jobs
    private List<Job> jobs; // This will hold all jobs posted by this admin


    private int adminOtp  = 0;

    // Constructors
    public JobAdmin() {}

    public JobAdmin(Long id, String jobAdminName, String jobAdminEmail, String role, String jobAdminCompanyName, String jobAdminPassword, String jobAdminConfirmPassword, byte[] companyLogo, String companyDescription, String companyTypeOfIndustry, String companyWebsiteLink, int companyStrength, String companyLocation, String companyLicense, String companyAboutDescription, String companyOverviewDescription, double companyLatitude, double companyLongitude, String companyPhoneNumber, byte[] companyLicenseDocument, String companyGstNumber, byte[] companyGstDocument, String companyCinNumber, byte[] companyCinDocument, byte[] descriptionBackground, byte[] aboutBackground, byte[] addJobBackground, byte[] overviewBackground, byte[] contactBackground, List<Job> jobs, int adminOtp) {
        this.id = id;
        this.jobAdminName = jobAdminName;
        this.jobAdminEmail = jobAdminEmail;
        this.role = role;
        this.jobAdminCompanyName = jobAdminCompanyName;
        this.jobAdminPassword = jobAdminPassword;
        this.jobAdminConfirmPassword = jobAdminConfirmPassword;
        this.companyLogo = companyLogo;
        this.companyDescription = companyDescription;
        this.companyTypeOfIndustry = companyTypeOfIndustry;
        this.companyWebsiteLink = companyWebsiteLink;
        this.companyStrength = companyStrength;
        this.companyLocation = companyLocation;
        this.companyLicense = companyLicense;
        this.companyAboutDescription = companyAboutDescription;
        this.companyOverviewDescription = companyOverviewDescription;
        this.companyLatitude = companyLatitude;
        this.companyLongitude = companyLongitude;
        this.companyPhoneNumber = companyPhoneNumber;
        this.companyLicenseDocument = companyLicenseDocument;
        this.companyGstNumber = companyGstNumber;
        this.companyGstDocument = companyGstDocument;
        this.companyCinNumber = companyCinNumber;
        this.companyCinDocument = companyCinDocument;
        this.descriptionBackground = descriptionBackground;
        this.aboutBackground = aboutBackground;
        this.addJobBackground = addJobBackground;
        this.overviewBackground = overviewBackground;
        this.contactBackground = contactBackground;
        this.adminOtp = adminOtp;
        this.jobs = jobs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobAdminName() {
        return jobAdminName;
    }

    public void setJobAdminName(String jobAdminName) {
        this.jobAdminName = jobAdminName;
    }

    public String getJobAdminEmail() {
        return jobAdminEmail;
    }

    public void setJobAdminEmail(String jobAdminEmail) {
        this.jobAdminEmail = jobAdminEmail;
    }

    public String getJobAdminCompanyName() {
        return jobAdminCompanyName;
    }

    public void setJobAdminCompanyName(String jobAdminCompanyName) {
        this.jobAdminCompanyName = jobAdminCompanyName;
    }

    public String getJobAdminPassword() {
        return jobAdminPassword;
    }

    public void setJobAdminPassword(String jobAdminPassword) {
        this.jobAdminPassword = jobAdminPassword;
    }

    public String getJobAdminConfirmPassword() {
        return jobAdminConfirmPassword;
    }

    public void setJobAdminConfirmPassword(String jobAdminConfirmPassword) {
        this.jobAdminConfirmPassword = jobAdminConfirmPassword;
    }

    public byte[] getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(byte[] companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyTypeOfIndustry() {
        return companyTypeOfIndustry;
    }

    public void setCompanyTypeOfIndustry(String companyTypeOfIndustry) {
        this.companyTypeOfIndustry = companyTypeOfIndustry;
    }

    public String getCompanyWebsiteLink() {
        return companyWebsiteLink;
    }

    public void setCompanyWebsiteLink(String companyWebsiteLink) {
        this.companyWebsiteLink = companyWebsiteLink;
    }

    public int getCompanyStrength() {
        return companyStrength;
    }

    public void setCompanyStrength(int companyStrength) {
        this.companyStrength = companyStrength;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getCompanyLicense() {
        return companyLicense;
    }

    public void setCompanyLicense(String companyLicense) {
        this.companyLicense = companyLicense;
    }

    public byte[] getCompanyLicenseDocument() {
        return companyLicenseDocument;
    }

    public void setCompanyLicenseDocument(byte[] companyLicenseDocument) {
        this.companyLicenseDocument = companyLicenseDocument;
    }

    public String getCompanyGstNumber() {
        return companyGstNumber;
    }

    public void setCompanyGstNumber(String companyGstNumber) {
        this.companyGstNumber = companyGstNumber;
    }

    public byte[] getCompanyGstDocument() {
        return companyGstDocument;
    }

    public void setCompanyGstDocument(byte[] companyGstDocument) {
        this.companyGstDocument = companyGstDocument;
    }

    public String getCompanyCinNumber() {
        return companyCinNumber;
    }

    public void setCompanyCinNumber(String companyCinNumber) {
        this.companyCinNumber = companyCinNumber;
    }

    public byte[] getCompanyCinDocument() {
        return companyCinDocument;
    }

    public void setCompanyCinDocument(byte[] companyCinDocument) {
        this.companyCinDocument = companyCinDocument;
    }
    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getCompanyAboutDescription() {
        return companyAboutDescription;
    }

    public void setCompanyAboutDescription(String companyAboutDescription) {
        this.companyAboutDescription = companyAboutDescription;
    }

    public double getCompanyLatitude() {
        return companyLatitude;
    }

    public void setCompanyLatitude(double companyLatitude) {
        this.companyLatitude = companyLatitude;
    }

    public double getCompanyLongitude() {
        return companyLongitude;
    }

    public void setCompanyLongitude(double companyLongitude) {
        this.companyLongitude = companyLongitude;
    }

    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public String getCompanyOverviewDescription() {
        return companyOverviewDescription;
    }

    public void setCompanyOverviewDescription(String companyOverviewDescription) {
        this.companyOverviewDescription = companyOverviewDescription;
    }

    public byte[] getDescriptionBackground() {
        return descriptionBackground;
    }

    public void setDescriptionBackground(byte[] descriptionBackground) {
        this.descriptionBackground = descriptionBackground;
    }

    public byte[] getAboutBackground() {
        return aboutBackground;
    }

    public void setAboutBackground(byte[] aboutBackground) {
        this.aboutBackground = aboutBackground;
    }

    public byte[] getAddJobBackground() {
        return addJobBackground;
    }

    public void setAddJobBackground(byte[] addJobBackground) {
        this.addJobBackground = addJobBackground;
    }

    public byte[] getOverviewBackground() {
        return overviewBackground;
    }

    public void setOverviewBackground(byte[] overviewBackground) {
        this.overviewBackground = overviewBackground;
    }

    public byte[] getContactBackground() {
        return contactBackground;
    }

    public void setContactBackground(byte[] contactBackground) {
        this.contactBackground = contactBackground;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAdminOtp() {
        return adminOtp;
    }

    public void setAdminOtp(int adminOtp) {
        this.adminOtp = adminOtp;
    }

    @Override
    public String toString() {
        return "JobAdmin{" +
                "id=" + id +
                ", jobAdminName='" + jobAdminName + '\'' +
                ", jobAdminEmail='" + jobAdminEmail + '\'' +
                ", role='" + role + '\'' +
                ", jobAdminCompanyName='" + jobAdminCompanyName + '\'' +
                ", jobAdminPassword='" + jobAdminPassword + '\'' +
                ", jobAdminConfirmPassword='" + jobAdminConfirmPassword + '\'' +
                ", companyLogo=" + Arrays.toString(companyLogo) +
                ", companyDescription='" + companyDescription + '\'' +
                ", companyTypeOfIndustry='" + companyTypeOfIndustry + '\'' +
                ", companyWebsiteLink='" + companyWebsiteLink + '\'' +
                ", companyStrength=" + companyStrength +
                ", companyLocation='" + companyLocation + '\'' +
                ", companyLicense='" + companyLicense + '\'' +
                ", companyAboutDescription='" + companyAboutDescription + '\'' +
                ", companyOverviewDescription='" + companyOverviewDescription + '\'' +
                ", companyLatitude=" + companyLatitude +
                ", companyLongitude=" + companyLongitude +
                ", companyPhoneNumber='" + companyPhoneNumber + '\'' +
                ", companyLicenseDocument=" + Arrays.toString(companyLicenseDocument) +
                ", companyGstNumber='" + companyGstNumber + '\'' +
                ", companyGstDocument=" + Arrays.toString(companyGstDocument) +
                ", companyCinNumber='" + companyCinNumber + '\'' +
                ", companyCinDocument=" + Arrays.toString(companyCinDocument) +
                ", descriptionBackground=" + Arrays.toString(descriptionBackground) +
                ", aboutBackground=" + Arrays.toString(aboutBackground) +
                ", addJobBackground=" + Arrays.toString(addJobBackground) +
                ", overviewBackground=" + Arrays.toString(overviewBackground) +
                ", contactBackground=" + Arrays.toString(contactBackground) +
                ", jobs=" + jobs +
                ", adminOtp=" + adminOtp +
                '}';
    }
}
