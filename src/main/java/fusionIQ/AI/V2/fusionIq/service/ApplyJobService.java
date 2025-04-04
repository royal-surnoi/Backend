package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.ApplyJobRepository;
import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ApplyJobService {

    private final ApplyJobRepository applyJobRepository;
    private final JobRepository jobRepository;
    private final UserRepo userRepo;
    private final EducationService educationService;
    @Autowired
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;



    public ApplyJobService(ApplyJobRepository applyJobRepository, JobRepository jobRepository, UserRepo userRepo, EducationService educationService) {
        this.applyJobRepository = applyJobRepository;
        this.jobRepository = jobRepository;
        this.userRepo = userRepo;
        this.educationService = educationService;
    }

    @Transactional
    public ApplyJob applyJob(Long jobId, Long userId, String status, byte[] resume) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        Long recruiterId = Optional.ofNullable(job.getRecruiter())
                .map(Recruiter::getId)
                .orElse(null);

        Long adminId = Optional.ofNullable(job.getRecruiter())
                .map(Recruiter::getJobAdmin)
                .map(JobAdmin::getId)
                .orElse(null);

        ApplyJob applyJob = new ApplyJob(user, job, status, resume);
        applyJob.setRecruiterId(recruiterId);
        applyJob.setAdminId(adminId);

        return applyJobRepository.save(applyJob);
    }



    public List<ApplyJob> getApplicationsByUserId(Long userId) {
        List<ApplyJob> applications = applyJobRepository.findByUserId(userId);

        // Debugging: Print timestamps to confirm retrieval
        applications.forEach(app -> System.out.println("Application ID: " + app.getId() + " | Last Updated: " + app.getLastUpdated()));

        return applications;
    }

    public Map<String, Object> getApplicationsByJobId(Long jobId) {
        List<ApplyJob> applications = applyJobRepository.findByJobId(jobId);

        // Construct the response object
        Map<String, Object> response = new LinkedHashMap<>(); // Using LinkedHashMap to preserve the order

        // Collect details of applicants who have applied for the job
        List<Map<String, Object>> applicants = new ArrayList<>();
        for (ApplyJob application : applications) {
            Map<String, Object> applicantDetails = new LinkedHashMap<>(); // Using LinkedHashMap to preserve the order
            User user = application.getUser();

            // Add user details in the desired order
            applicantDetails.put("userId", user.getId());
            applicantDetails.put("userName", user.getName());  // Use the correct method for the user's name
            applicantDetails.put("userEmail", user.getEmail());
            applicantDetails.put("applicationStatus", application.getStatus());
            applicantDetails.put("resume", application.getResume()); // Include the resume if needed
            applicantDetails.put("userImage", user.getUserImage()); // Include the resume if needed

            applicants.add(applicantDetails);
        }

        // Add the list of applicants to the response first
        response.put("applicants", applicants);

        if (!applications.isEmpty()) {
            Job job = applications.get(0).getJob();

            // Add job details after the user details
            Map<String, Object> jobDetails = new LinkedHashMap<>(); // Using LinkedHashMap to preserve the order
            jobDetails.put("jobId", job.getId());
            jobDetails.put("jobTitle", job.getJobTitle());
            jobDetails.put("jobDescription", job.getJobDescription());
            jobDetails.put("requiredSkills", job.getRequiredSkills());
            jobDetails.put("location", job.getLocation());
            jobDetails.put("minSalary", job.getMinSalary());
            jobDetails.put("maxSalary", job.getMaxSalary());
            jobDetails.put("jobType", job.getJobType());
            jobDetails.put("status", job.getStatus());
            jobDetails.put("appliedCount", job.getAppliedCount());
            jobDetails.put("requiredEducation", job.getRequiredEducation()); // New field
            jobDetails.put("requiredEducationStream", job.getRequiredEducationStream()); // New field
            jobDetails.put("requiredPercentage", job.getRequiredPercentage()); // New field
            jobDetails.put("requiredPassoutYear", job.getRequiredPassoutYear()); // New field
            jobDetails.put("requiredWorkExperience", job.getRequiredWorkExperience()); // New field

            // Add job details to the response
            response.put("jobDetails", jobDetails);
        }

        return response;
    }
    public ApplyJob updateApplicationStatus(Long applicationId, String status) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
        applyJob.setStatus(status);
        return applyJobRepository.save(applyJob);
    }

    public ApplyJob updateResume(Long applicationId, byte[] resume) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
        applyJob.setResume(resume); // Update the resume
        return applyJobRepository.save(applyJob);
    }

    public ApplyJob getApplicationByJobAndUser(Long jobId, Long userId) {
        return applyJobRepository.findByJobIdAndUserId(jobId, userId);
    }
    public Map<String, Object> getApplicationsWithEducationByJobId(Long jobId) {
        // Retrieve the job details regardless of whether there are applications or not
        Job job = jobRepository.findById(jobId).orElse(null);

        // Initialize the response object
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> applicants = new ArrayList<>();

        // Fetch applications for the given jobId
        List<ApplyJob> applications = applyJobRepository.findByJobId(jobId);

        // Fetch shortlisted applicants for this job
        List<Long> shortlistedApplicantIds = shortlistedCandidatesRepository
                .findByJobId(jobId)
                .stream()
                .map(sc -> sc.getApplyJob().getId()) // Get ApplyJob ID of shortlisted candidates
                .toList();

        // Check for null or empty applications
        if (applications != null && !applications.isEmpty()) {
            for (ApplyJob application : applications) {
                // Skip already shortlisted applicants
                if (shortlistedApplicantIds.contains(application.getId())) {
                    continue;
                }

                Map<String, Object> applicantDetails = new LinkedHashMap<>();
                User user = application.getUser();

                // Add applicant details
                applicantDetails.put("applicantId", application.getId());
                applicantDetails.put("userId", user.getId());
                applicantDetails.put("userName", user.getName());
                applicantDetails.put("userEmail", user.getEmail());
                applicantDetails.put("userImage", user.getUserImage() != null ?
                        Base64.getEncoder().encodeToString(user.getUserImage()) : null);
                applicantDetails.put("applicationStatus", application.getStatus() != null ? application.getStatus() : "Unknown");
                applicantDetails.put("resume", application.getResume());
                applicantDetails.put("createdAt", application.getLastUpdated());

                // Fetch education details for the user
                List<Education> educationDetails = educationService.getEducationByUserId(user.getId());

                // Map education details to the response
                List<Map<String, Object>> formattedEducationDetails = new ArrayList<>();
                if (educationDetails != null) {
                    for (Education education : educationDetails) {
                        Map<String, Object> educationMap = new LinkedHashMap<>();
                        educationMap.put("id", education.getId());
                        educationMap.put("schoolStatus", education.getSchoolStatus());
                        educationMap.put("schoolName", education.getSchoolName());
                        educationMap.put("schoolPercentage", education.getSchoolPercentage());
                        educationMap.put("schoolYearOfPassout", education.getSchoolYearOfPassout());
                        educationMap.put("schoolEducationBoard", education.getSchoolEducationBoard());
                        educationMap.put("schoolYearOfJoining", education.getSchoolYearOfJoining());
                        educationMap.put("pursuingClass", education.getPursuingClass());
                        educationMap.put("intermediateDiploma", education.getIntermediateDiploma());
                        educationMap.put("intermediateStatus", education.getIntermediateStatus());
                        educationMap.put("intermediateCollegeName", education.getIntermediateCollegeName());
                        educationMap.put("intermediateCollegeSpecialization", education.getIntermediateCollegeSpecialization());
                        educationMap.put("intermediateCollegePercentage", education.getIntermediateCollegePercentage());
                        educationMap.put("intermediateYearOfPassout", education.getIntermediateYearOfPassout());
                        educationMap.put("intermediateEducationBoard", education.getIntermediateEducationBoard());
                        educationMap.put("intermediateYearOfJoining", education.getIntermediateYearOfJoining());
                        educationMap.put("graduationStatus", education.getGraduationStatus());
                        educationMap.put("graduationCollegeName", education.getGraduationCollegeName());
                        educationMap.put("graduationCollegeSpecialization", education.getGraduationCollegeSpecialization());
                        educationMap.put("graduationCollegePercentage", education.getGraduationCollegePercentage());
                        educationMap.put("graduationYearOfPassout", education.getGraduationYearOfPassout());
                        educationMap.put("graduationYearOfJoining", education.getGraduationYearOfJoining());
                        educationMap.put("postGraduateStatus", education.getPostGraduateStatus());
                        educationMap.put("postGraduateCollegeName", education.getPostGraduateCollegeName());
                        educationMap.put("postGraduateCollegeSpecialization", education.getPostGraduateCollegeSpecialization());
                        educationMap.put("postGraduateCollegePercentage", education.getPostGraduateCollegePercentage() != null ?
                                education.getPostGraduateCollegePercentage() : "N/A");
                        educationMap.put("postGraduateYearOfPassout", education.getPostGraduateYearOfPassout());
                        educationMap.put("postGraduateYearOfJoining", education.getPostGraduateYearOfJoining());
                        educationMap.put("createdAt", education.getCreatedAt());
                        formattedEducationDetails.add(educationMap);
                    }
                }

                // Add the education details to the applicant details
                applicantDetails.put("educationDetails", formattedEducationDetails);

                // Add applicant details to the list
                applicants.add(applicantDetails);
            }
        }

        // Add applicants to the response
        response.put("applicants", applicants);

        // Add job details to the response even if there are no applicants
        if (job != null) {
            Map<String, Object> jobDetails = new LinkedHashMap<>();
            jobDetails.put("jobId", job.getId());
            jobDetails.put("jobTitle", job.getJobTitle());
            jobDetails.put("jobDescription", job.getJobDescription());
            jobDetails.put("requiredSkills", job.getRequiredSkills());
            jobDetails.put("location", job.getLocation());
            jobDetails.put("minSalary", job.getMinSalary());
            jobDetails.put("maxSalary", job.getMaxSalary());
            jobDetails.put("jobType", job.getJobType());
            jobDetails.put("status", job.getStatus());
            jobDetails.put("appliedCount", job.getAppliedCount());
            jobDetails.put("createdAt", job.getCreatedAt());
            jobDetails.put("vacancyCount", job.getVacancyCount());
            jobDetails.put("NumberOfLevels", job.getNumberOfLevels());
            response.put("jobDetails", jobDetails);
        }

        return response;
    }


    public void deleteApplication(Long applicationId) {
        // Check if the application exists, if so delete
        if (applyJobRepository.existsById(applicationId)) {
            applyJobRepository.deleteById(applicationId);
        } else {
            throw new IllegalArgumentException("Application not found with id: " + applicationId);
        }
    }

    public ApplyJob updateWithdrawStatus(Long applicationId) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
        applyJob.setWithdraw("yes");
        return applyJobRepository.save(applyJob);
    }

    // Fetch applications with withdraw = "no" for a specific user
    public List<ApplyJob> getActiveApplicationsByUserId(Long userId) {
        return applyJobRepository.findByUserIdAndWithdraw(userId, "no");
    }

    public Optional<ApplyJob> getApplyJobById(Long applicantId) {
        return applyJobRepository.findById(applicantId);
    }

    public ApplyJob reapplyForJob(Long applicationId) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
        if ("yes".equalsIgnoreCase(applyJob.getWithdraw())) {
            applyJob.setWithdraw("no");
            return applyJobRepository.save(applyJob);
        } else {
            throw new IllegalStateException("Application is already active or ineligible for reapplication.");
        }
    }
    public Boolean Isapply(Long userId,Long jobId){    boolean isApplied = applyJobRepository.existsByUserIdAndJobId(userId, jobId);    return isApplied;}
}
