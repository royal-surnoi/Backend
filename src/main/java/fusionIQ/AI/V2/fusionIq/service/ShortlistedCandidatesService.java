package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;

import fusionIQ.AI.V2.fusionIq.repository.ApplyJobRepository;

import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import java.util.*;

@Service

public class ShortlistedCandidatesService {

    @Autowired

    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    private ApplyJobRepository applyJobRepository;

    private JobRepository jobRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    public List<Map<String, Object>> getShortlistedCandidatesWithDetailsByJobId(Long jobId) {

        List<ShortlistedCandidates> shortlistedCandidates = shortlistedCandidatesRepository.findByJobId(jobId);

        List<Map<String, Object>> response = new ArrayList<>();

        for (ShortlistedCandidates candidate : shortlistedCandidates) {
            Map<String, Object> candidateDetails = new LinkedHashMap<>();
            candidateDetails.put("shortlistedCandidateId", candidate.getId());

            // Get ApplyJob and User details
            ApplyJob applyJob = candidate.getApplyJob();
            Job job = candidate.getJob();
            User user = applyJob.getUser();

            // Populate applicant details
            candidateDetails.put("applicantId", applyJob.getId());
            candidateDetails.put("applicantName", user.getName());
            candidateDetails.put("applicantEmail", user.getEmail());
            candidateDetails.put("applicantStatus", candidate.getStatus());
            candidateDetails.put("shortlistedAt", candidate.getShortlistedAt());
            candidateDetails.put("userId", user.getId());
            candidateDetails.put("userImage", user.getUserImage());

            PersonalDetails personalDetails = user.getPersonalDetails();
            candidateDetails.put("resume", personalDetails != null ? personalDetails.getResume() : "Not Available");

            // Fetch education details (assuming Education entity is related to User)
            Education education = user.getEducation();
            if (education != null) {
                Map<String, Object> educationDetails = new HashMap<>();
                educationDetails.put("schoolStatus", education.getSchoolStatus());
                educationDetails.put("schoolName", education.getSchoolName());
                educationDetails.put("schoolPercentage", education.getSchoolPercentage());
                educationDetails.put("schoolYearOfPassout", education.getSchoolYearOfPassout());
                educationDetails.put("schoolEducationBoard", education.getSchoolEducationBoard());
                educationDetails.put("schoolYearOfJoining", education.getSchoolYearOfJoining());
                educationDetails.put("pursuingClass", education.getPursuingClass());

                educationDetails.put("intermediateDiploma", education.getIntermediateDiploma());
                educationDetails.put("intermediateStatus", education.getIntermediateStatus());
                educationDetails.put("intermediateCollegeName", education.getIntermediateCollegeName());
                educationDetails.put("intermediateCollegeSpecialization", education.getIntermediateCollegeSpecialization());
                educationDetails.put("intermediateCollegePercentage", education.getIntermediateCollegePercentage());
                educationDetails.put("intermediateYearOfPassout", education.getIntermediateYearOfPassout());
                educationDetails.put("intermediateEducationBoard", education.getIntermediateEducationBoard());
                educationDetails.put("intermediateYearOfJoining", education.getIntermediateYearOfJoining());

                educationDetails.put("graduationStatus", education.getGraduationStatus());
                educationDetails.put("graduationCollegeName", education.getGraduationCollegeName());
                educationDetails.put("graduationCollegeSpecialization", education.getGraduationCollegeSpecialization());
                educationDetails.put("graduationCollegePercentage", education.getGraduationCollegePercentage());
                educationDetails.put("graduationYearOfPassout", education.getGraduationYearOfPassout());
                educationDetails.put("graduationYearOfJoining", education.getGraduationYearOfJoining());

                educationDetails.put("postGraduateStatus", education.getPostGraduateStatus());
                educationDetails.put("postGraduateCollegeName", education.getPostGraduateCollegeName());
                educationDetails.put("postGraduateCollegeSpecialization", education.getPostGraduateCollegeSpecialization());
                educationDetails.put("postGraduateCollegePercentage", education.getPostGraduateCollegePercentage());
                educationDetails.put("postGraduateYearOfPassout", education.getPostGraduateYearOfPassout());
                educationDetails.put("postGraduateYearOfJoining", education.getPostGraduateYearOfJoining());

                candidateDetails.put("education", educationDetails);
            } else {
                candidateDetails.put("education", "Not Available");
            }


            // Fetch resume details (assuming PersonalDetails entity is related to User)


            // Populate job details
            Map<String, Object> jobDetails = new HashMap<>();
            jobDetails.put("jobId", job.getId());
            jobDetails.put("jobTitle", job.getJobTitle());
            jobDetails.put("jobDescription", job.getJobDescription());
            candidateDetails.put("jobDetails", jobDetails);

            // Populate shortlisted details
            candidateDetails.put("status", candidate.getStatus());
            candidateDetails.put("shortlistedAt", candidate.getShortlistedAt());

            response.add(candidateDetails);
        }

        return response;

    }


    public ShortlistedCandidatesService(ShortlistedCandidatesRepository shortlistedCandidatesRepository,

                                        ApplyJobRepository applyJobRepository) {

        this.shortlistedCandidatesRepository = shortlistedCandidatesRepository;

        this.applyJobRepository = applyJobRepository;

    }

    public ShortlistedCandidates shortlistCandidate(Long jobId, Long applicantId, String status) {
        // Fetch the ApplyJob entry by applicantId
        ApplyJob applyJob = applyJobRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("ApplyJob not found for applicantId: " + applicantId));

        // Check if the ApplyJob belongs to the specified jobId
        if (!applyJob.getJob().getId().equals(jobId)) {
            throw new RuntimeException("The provided jobId does not match the ApplyJob entry");
        }

        Optional<ShortlistedCandidates> existingShortlistedCandidate =
                shortlistedCandidatesRepository.findByJobIdAndApplyJobId(jobId, applicantId);

        if (existingShortlistedCandidate.isPresent()) {
            throw new RuntimeException("The candidate has already been shortlisted for this job");
        }

        // Update the status in the ApplyJob table
        applyJob.setStatus(status);
        applyJobRepository.save(applyJob);

        // Create a new ShortlistedCandidates entry
        ShortlistedCandidates shortlistedCandidate = new ShortlistedCandidates();
        shortlistedCandidate.setApplyJob(applyJob);
        shortlistedCandidate.setJob(applyJob.getJob());
        shortlistedCandidate.setStatus(status);
        shortlistedCandidate.setShortlistedAt(LocalDateTime.now());
        shortlistedCandidate.setUser(applyJob.getUser()); // Assuming ApplyJob has a reference to User

        // Set recruiter and admin ID if present, otherwise set as null
        Long recruiterId = applyJob.getRecruiterId();
        Long adminId = applyJob.getAdminId();
        shortlistedCandidate.setRecruiterId(recruiterId != null ? recruiterId : null);
        shortlistedCandidate.setAdminId(adminId != null ? adminId : null);

        // Save and return the ShortlistedCandidates entry
        return shortlistedCandidatesRepository.save(shortlistedCandidate);
    }




    @Transactional
    public Map<String, Object> shortlistMultipleCandidates(Long jobId, List<Long> applicantIds, String status) {
        List<Map<String, Object>> successfulShortlists = new ArrayList<>();
        List<Map<String, String>> failedApplicants = new ArrayList<>();

        for (Long applicantId : applicantIds) {
            Map<String, String> failureReason = new HashMap<>();
            try {
                // Fetch the ApplyJob entry by applicantId
                ApplyJob applyJob = applyJobRepository.findById(applicantId)
                        .orElseThrow(() -> new RuntimeException("ApplyJob not found for applicantId: " + applicantId));

                // Validate job ID
                if (!applyJob.getJob().getId().equals(jobId)) {
                    failureReason.put("applicantId", String.valueOf(applicantId));
                    failureReason.put("reason", "Job ID mismatch");
                    failedApplicants.add(failureReason);
                    continue;
                }

                // Check if the candidate is already shortlisted
                Optional<ShortlistedCandidates> existingShortlistedCandidate =
                        shortlistedCandidatesRepository.findByJobIdAndApplyJobId(jobId, applicantId);

                if (existingShortlistedCandidate.isPresent()) {
                    failureReason.put("applicantId", String.valueOf(applicantId));
                    failureReason.put("reason", "Already shortlisted");
                    failedApplicants.add(failureReason);
                    continue;
                }

                // Update status in ApplyJob
                applyJob.setStatus(status);
                applyJobRepository.save(applyJob);

                // Notify if status is "shortlisted"
                if ("shortlisted".equalsIgnoreCase(status)) {
                    sendShortlistNotification(applyJob);
                }

                // Create a new ShortlistedCandidates entry
                ShortlistedCandidates shortlistedCandidate = new ShortlistedCandidates();
                shortlistedCandidate.setApplyJob(applyJob);
                shortlistedCandidate.setJob(applyJob.getJob());
                shortlistedCandidate.setStatus(status);
                shortlistedCandidate.setShortlistedAt(LocalDateTime.now());
                shortlistedCandidate.setUser(applyJob.getUser());
                shortlistedCandidate.setRecruiterId(applyJob.getRecruiterId());
                shortlistedCandidate.setAdminId(applyJob.getAdminId());

                // Save and add to successful list
                ShortlistedCandidates savedCandidate = shortlistedCandidatesRepository.save(shortlistedCandidate);
                Map<String, Object> successEntry = new HashMap<>();
                successEntry.put("id", savedCandidate.getId());
                successEntry.put("status", savedCandidate.getStatus());
                successEntry.put("shortlistedAt", savedCandidate.getShortlistedAt());
                successEntry.put("jobId", savedCandidate.getJob().getId());
                successEntry.put("applicantId", savedCandidate.getApplyJob().getId());
                successEntry.put("recruiterId", savedCandidate.getRecruiterId());
                successEntry.put("adminId", savedCandidate.getAdminId());
                successEntry.put("userId", savedCandidate.getUser().getId());
                successfulShortlists.add(successEntry);

            } catch (Exception e) {
                failureReason.put("applicantId", String.valueOf(applicantId));
                failureReason.put("reason", e.getMessage());
                failedApplicants.add(failureReason);
            }
        }

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("success", successfulShortlists);
        response.put("failed", failedApplicants);

        return response;
    }

    private void sendShortlistNotification(ApplyJob applyJob) {
        String applicantEmail = applyJob.getUser().getEmail();
        String applicantName = applyJob.getUser().getName();
        String jobTitle = applyJob.getJob().getJobTitle();

        String subject = "Congratulations! You have been shortlisted for " + jobTitle;
        String body = String.format(
                "<html>" +
                        "<body>" +
                        "<p>Dear <strong>%s</strong>,</p>" +
                        "<p>We are excited to inform you that you have been <strong>shortlisted</strong> for the position of <strong>%s</strong>.</p>" +
                        "<p>Our team will review your application further, and we will reach out with the next steps shortly.</p>" +
                        "<p>If you have any questions, feel free to contact us at <a href='mailto:support@fusioniq.com'>support@fusioniq.com</a>.</p>" +
                        "<p>Thank you for your interest in joining our team. We wish you the best of luck!</p>" +
                        "<br>" +
                        "<p>Best regards,</p>" +
                        "<p><strong>FusionIQ Recruitment Team</strong></p>" +
                        "</body>" +
                        "</html>",
                applicantName, jobTitle
        );

        emailSenderService.sendEmailConfirmations(applicantEmail, subject, body);
    }

    public List<Map<String, Object>> getAllShortlistedCandidatesWithDetails() {
        List<ShortlistedCandidates> shortlistedCandidates = shortlistedCandidatesRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();


        for (ShortlistedCandidates candidate : shortlistedCandidates) {
            Map<String, Object> candidateDetails = new LinkedHashMap<>();
            candidateDetails.put("shortlistedCandidateId", candidate.getId());

            // Get ApplyJob and User details
            ApplyJob applyJob = candidate.getApplyJob();
            Job job = candidate.getJob();
            User user = applyJob.getUser();

            // Populate applicant details
            candidateDetails.put("applicantId", applyJob.getId());
            candidateDetails.put("applicantName", user.getName());
            candidateDetails.put("applicantEmail", user.getEmail());
            candidateDetails.put("applicantStatus", candidate.getStatus());
            candidateDetails.put("shortlistedAt", candidate.getShortlistedAt());
            candidateDetails.put("userId", user.getId());

            PersonalDetails personalDetails = user.getPersonalDetails();
            candidateDetails.put("resume", personalDetails != null ? personalDetails.getResume() : "Not Available");

            // Fetch education details (assuming Education entity is related to User)
            Education education = user.getEducation();
            if (education != null) {
                Map<String, Object> educationDetails = new HashMap<>();
                educationDetails.put("schoolStatus", education.getSchoolStatus());
                educationDetails.put("schoolName", education.getSchoolName());
                educationDetails.put("schoolPercentage", education.getSchoolPercentage());
                educationDetails.put("schoolYearOfPassout", education.getSchoolYearOfPassout());
                educationDetails.put("schoolEducationBoard", education.getSchoolEducationBoard());
                educationDetails.put("schoolYearOfJoining", education.getSchoolYearOfJoining());
                educationDetails.put("pursuingClass", education.getPursuingClass());

                educationDetails.put("intermediateDiploma", education.getIntermediateDiploma());
                educationDetails.put("intermediateStatus", education.getIntermediateStatus());
                educationDetails.put("intermediateCollegeName", education.getIntermediateCollegeName());
                educationDetails.put("intermediateCollegeSpecialization", education.getIntermediateCollegeSpecialization());
                educationDetails.put("intermediateCollegePercentage", education.getIntermediateCollegePercentage());
                educationDetails.put("intermediateYearOfPassout", education.getIntermediateYearOfPassout());
                educationDetails.put("intermediateEducationBoard", education.getIntermediateEducationBoard());
                educationDetails.put("intermediateYearOfJoining", education.getIntermediateYearOfJoining());

                educationDetails.put("graduationStatus", education.getGraduationStatus());
                educationDetails.put("graduationCollegeName", education.getGraduationCollegeName());
                educationDetails.put("graduationCollegeSpecialization", education.getGraduationCollegeSpecialization());
                educationDetails.put("graduationCollegePercentage", education.getGraduationCollegePercentage());
                educationDetails.put("graduationYearOfPassout", education.getGraduationYearOfPassout());
                educationDetails.put("graduationYearOfJoining", education.getGraduationYearOfJoining());

                educationDetails.put("postGraduateStatus", education.getPostGraduateStatus());
                educationDetails.put("postGraduateCollegeName", education.getPostGraduateCollegeName());
                educationDetails.put("postGraduateCollegeSpecialization", education.getPostGraduateCollegeSpecialization());
                educationDetails.put("postGraduateCollegePercentage", education.getPostGraduateCollegePercentage());
                educationDetails.put("postGraduateYearOfPassout", education.getPostGraduateYearOfPassout());
                educationDetails.put("postGraduateYearOfJoining", education.getPostGraduateYearOfJoining());

                candidateDetails.put("education", educationDetails);
            } else {
                candidateDetails.put("education", "Not Available");
            }


            // Fetch resume details (assuming PersonalDetails entity is related to User)


            // Populate job details
            Map<String, Object> jobDetails = new HashMap<>();
            jobDetails.put("jobId", job.getId());
            jobDetails.put("jobTitle", job.getJobTitle());
            jobDetails.put("jobDescription", job.getJobDescription());
            candidateDetails.put("jobDetails", jobDetails);

            // Populate shortlisted details
            candidateDetails.put("status", candidate.getStatus());
            candidateDetails.put("shortlistedAt", candidate.getShortlistedAt());

            response.add(candidateDetails);
        }

        return response;
    }





}

