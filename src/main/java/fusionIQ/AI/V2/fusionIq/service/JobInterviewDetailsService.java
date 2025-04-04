package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.controller.JobInterviewDetailsController;
import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;
import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
import fusionIQ.AI.V2.fusionIq.repository.JobInterviewDetailsRepo;
import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobInterviewDetailsService {


    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private    JobRepository jobRepository;
    @Autowired
    private UserRepo userRepo;
    private final JobInterviewDetailsRepo jobInterviewDetailsRepo;
    private final ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    @Autowired
    public JobInterviewDetailsService(JobInterviewDetailsRepo jobInterviewDetailsRepo,
                                      ShortlistedCandidatesRepository shortlistedCandidatesRepository) {
        this.jobInterviewDetailsRepo = jobInterviewDetailsRepo;
        this.shortlistedCandidatesRepository = shortlistedCandidatesRepository;
    }

    public JobInterviewDetails saveShortlistDetails(
            Long userId, String userName, String userEmail,
            Long recruiterId, String recruiterName, String recruiterEmail,
            String currentFeedback, Long jobId,
            Long adminId, String adminName, String adminEmail, List<JobInterviewDetails> existingDetails) {

        // Fetch the job details
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));

        // Initialize the new job interview details
        JobInterviewDetails details = new JobInterviewDetails();
        details.setUserId(userId);
        details.setUserName(userName);
        details.setUserEmail(userEmail);
        details.setCurrentFeedback(currentFeedback);
        details.setJob(job);

        // Determine levels
        int numberOfLevels = job.getNumberOfLevels();
        if (!existingDetails.isEmpty()) {
            // Get the last entry's upcoming level
            String lastUpcomingLevel = existingDetails.get(existingDetails.size() - 1).getUpcomingLevel();
            details.setCurrentLevel(lastUpcomingLevel);

            // Calculate the next level
            int currentLevelNum = Integer.parseInt(lastUpcomingLevel.replace("level-", ""));
            String nextLevel = (currentLevelNum + 1) > numberOfLevels ? "Accepted" : "level-" + (currentLevelNum + 1);
            details.setUpcomingLevel(nextLevel);
        } else {
            // Default levels for new entries
            details.setCurrentLevel("level-1");
            details.setUpcomingLevel(numberOfLevels > 1 ? "level-2" : "Accepted");
        }

        // Set recruiter or admin details
        if (recruiterId != null) {
            details.setRecruiterId(recruiterId);
            details.setRecruiterName(recruiterName);
            details.setRecruiterEmail(recruiterEmail != null ? recruiterEmail : "");
        } else if (adminId != null) {
            details.setAdminId(adminId);
            details.setAdminName(adminName);
            details.setAdminEmail(adminEmail);
        }

        // Save the job interview details
        JobInterviewDetails savedDetails = jobInterviewDetailsRepo.save(details);

        // Update the shortlisted candidates table
        updateShortlistedCandidates(userId, jobId, recruiterId, adminId, details.getUpcomingLevel(), job);
        return savedDetails;
    }

    private void updateShortlistedCandidates(
            Long userId, Long jobId, Long recruiterId, Long adminId, String status, Job job) {

        // Fetch existing shortlisted candidate or create a new one
        ShortlistedCandidates shortlisted = shortlistedCandidatesRepository
                .findByUserIdAndJobId(userId, jobId)
                .orElse(new ShortlistedCandidates());

        // Set details for shortlisted candidate
        shortlisted.setJob(job);
        shortlisted.setUser(userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId)));
        shortlisted.setStatus(status);
        shortlisted.setShortlistedAt(LocalDateTime.now());
        shortlisted.setRecruiterId(recruiterId);
        shortlisted.setAdminId(adminId);

        // Save the shortlisted candidate
        shortlistedCandidatesRepository.save(shortlisted);
    }

    public List<JobInterviewDetails> findByUserIdAndJobId(Long userId, Long jobId) {
        return jobInterviewDetailsRepo.findByUserIdAndJobId(userId, jobId);
    }

    public JobInterviewDetails updateInterviewDetails(Long id, JobInterviewDetails updatedDetails) {
        // Fetch the existing JobInterviewDetails by ID
        JobInterviewDetails existingDetails = jobInterviewDetailsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobInterviewDetails not found for id: " + id));

        Long jobId = existingDetails.getJob().getId();

        // Generate a new interview link
        String interviewLink = generateConferenceUrl();
        String interviewScoreLink = generateScoreUrl(existingDetails.getUserId(), existingDetails.getRecruiterId(), id, jobId);
        existingDetails.setInterviewLink(interviewLink);
        existingDetails.setInterviewDescription(updatedDetails.getInterviewDescription());
        existingDetails.setInterviewerEmail(updatedDetails.getInterviewerEmail());
        existingDetails.setInterviewerName(updatedDetails.getInterviewerName());
        existingDetails.setInterviewTimestamp(updatedDetails.getInterviewTimestamp());
        existingDetails.setInterviewScoreLink(interviewScoreLink);

        // Save the updated details
        JobInterviewDetails savedDetails = jobInterviewDetailsRepo.save(existingDetails);

        // Determine email recipient and send notifications
        sendEmailNotifications(existingDetails, updatedDetails);

        return savedDetails;
    }

    private String generateConferenceUrl() {
        return "https://meet.jit.si/" + UUID.randomUUID().toString();
    }

    private String generateScoreUrl(Long userId, Long recruiterId, Long interviewId, Long jobId) {
        return "http://localhost:4200/levelfeedback/" + userId + "/" + recruiterId + "/" + interviewId + "/" + jobId;
    }


    private void sendEmailNotifications(JobInterviewDetails details, JobInterviewDetails updatedDetails) {
        // Determine whether to use recruiterEmail or adminEmail
        String recipientEmail = updatedDetails.getRecruiterEmail() != null
                ? updatedDetails.getRecruiterEmail()
                : updatedDetails.getAdminEmail();
        String recipientType = updatedDetails.getRecruiterEmail() != null
                ? "recruiter"
                : "admin";

        // Recruiter/Admin Email
        if (recipientEmail != null) {
            emailSenderService.sendEmailConfirmations(
                    recipientEmail,
                    getEmailSubject(recipientType, details.getUserName()),
                    generateEmailContent(details, recipientType)
            );
        }


        // Candidate Email
        emailSenderService.sendEmailConfirmations(
                details.getUserEmail(),
                getEmailSubject("candidate", details.getUserName()),
                generateEmailContent(details, "candidate")
        );

        // Interviewer Email
        emailSenderService.sendEmailConfirmations(
                details.getInterviewerEmail(),
                getEmailSubject("interviewer", details.getUserName()),
                generateEmailContent(details, "interviewer")
        );
    }


    private String generateEmailContent(JobInterviewDetails details, String recipientType) {
        String role = recipientType.equals("recruiter") ? "Recruiter" :
                recipientType.equals("admin") ? "Admin" :
                        recipientType.equals("interviewer") ? "Interviewer" :
                                "Candidate";

        String additionalContent = "";

        // Include the interview score link only for the interviewer
        if (recipientType.equals("interviewer")) {
            additionalContent = "<li><strong>Provide Interview Feedback and Score:</strong> " +
                    "<a href='http://localhost:4200/levelfeedback/" +
                    details.getUserId() + "/" +
                    details.getRecruiterId() + "/" +
                    details.getJob().getId() + "/" +
                    details.getId() + "'>Click here</a></li>";
        }

        return "<html><body>" +
                "<p>Dear " + role + ",</p>" +
                "<p>The interview details have been updated. Please find the updated details below:</p>" +
                "<ul>" +
                "<li><strong>Interview Link:</strong> <a href='" + details.getInterviewLink() + "'>" +
                details.getInterviewLink() + "</a></li>" +
                "<li><strong>Interview Description:</strong> " + details.getInterviewDescription() + "</li>" +
                "<li><strong>Scheduled Time:</strong> " + details.getInterviewTimestamp() + "</li>" +
                additionalContent +
                "</ul>" +
                "<p>Best regards,</p>" +
                "<p>Interview Management Team</p>" +
                "</body></html>";
    }


    private String getEmailSubject(String recipientType, String candidateName) {
        switch (recipientType.toLowerCase()) {
            case "recruiter":
                return "Interview Confirmation for " + candidateName;
            case "admin":
                return "Interview Details Updated for " + candidateName;
            case "interviewer":
                return "Upcoming Interview Scheduled: " + candidateName;
            case "candidate":
                return "Your Interview Has Been Scheduled - Surnoi Technologies";
            default:
                return "Interview Notification";
        }
    }

    public JobInterviewDetails saveScoreAndFeedback(Long interviewId, Integer interviewerScore, String interviewerFeedback) {
        JobInterviewDetails interviewDetails = jobInterviewDetailsRepo.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found for ID: " + interviewId));

        interviewDetails.setInterviewerScore(interviewerScore);
        interviewDetails.setInterviewerFeedback(interviewerFeedback);

        return jobInterviewDetailsRepo.save(interviewDetails);
    }

    public JobInterviewDetails getScoreAndFeedback(Long interviewId) {
        return jobInterviewDetailsRepo.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found for ID: " + interviewId));
    }

    public List<JobInterviewDetails> getTodayInterviewsByRecruiterId(Long recruiterId) {
        LocalDateTime startOfToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return jobInterviewDetailsRepo.findInterviewsForTodayByRecruiterId(recruiterId,startOfToday);
    }

    public List<JobInterviewDetails> findInterviewsFromTodayByAdminId(Long adminId) {
        LocalDateTime startOfToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return jobInterviewDetailsRepo.findByAdminIdAndInterviewTimestampAfter(adminId, startOfToday);
    }

    public List<JobInterviewDetails> findInterviewsFromTodayByUserId(Long userId) {
        LocalDateTime startOfToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return jobInterviewDetailsRepo.findByUserIdAndInterviewTimestampAfter(userId, startOfToday);
    }
    public List<JobInterviewDetails> findInterviewsFromTodayByInterviewerEmail(String email) {
        LocalDateTime startOfToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return jobInterviewDetailsRepo.findByInterviewerEmailAndInterviewTimestampAfter(email, startOfToday);
    }

}