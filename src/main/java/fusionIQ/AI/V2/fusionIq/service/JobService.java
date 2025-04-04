package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.repository.JobAdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobAdminRepository adminRepository;

    @Autowired
    private SavedJobsRepository savedJobsRepository;

    public JobService(JobRepository jobRepository, RecruiterRepository recruiterRepository, JobAdminRepository adminRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.adminRepository = adminRepository;
    }

    public Job createJob(Long adminId, Long recruiterId, Job job) {
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        job.setJobAdmin(admin);
        job.setRecruiter(recruiter);
        return jobRepository.save(job);
    }


    public Job createJob(Long adminId, Job job) {
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));



        job.setJobAdmin(admin);

        return jobRepository.save(job);
    }

    public List<Job> getJobsByRecruiterId(Long recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    public List<Job> getJobsByAdminId(Long adminId) {
        return jobRepository.findByJobAdmin_Id(adminId);
    }

    public List<Map<String, Object>> getJobsWithSelectedDetails(Long recruiterId) {
        // Fetch jobs associated with the given recruiterId
        List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);

        // List to hold the job and recruiter details
        List<Map<String, Object>> jobDetailsList = new ArrayList<>();

        // Iterate over the jobs and populate the details
        for (Job job : jobs) {
            Map<String, Object> jobDetails = new LinkedHashMap<>();  // Use LinkedHashMap to maintain order
            jobDetails.put("id", job.getId());  // Job ID
            jobDetails.put("jobTitle", job.getJobTitle());  // Job Title
            jobDetails.put("jobDescription", job.getJobDescription());  // Job Description
            jobDetails.put("requiredSkills", job.getRequiredSkills());  // Required Skills
            jobDetails.put("location", job.getLocation());  // Location
            jobDetails.put("minSalary", job.getMinSalary());  // Minimum Salary
            jobDetails.put("maxSalary", job.getMaxSalary());  // Maximum Salary
            jobDetails.put("jobType", job.getJobType());  // Job Type
            jobDetails.put("status", job.getStatus());  // Status
            jobDetails.put("vacancyCount", job.getVacancyCount());  // Vacancy Count
            jobDetails.put("appliedCount", job.getAppliedCount());  // Applied Count
            jobDetails.put("createdAt", job.getCreatedAt());  // Created At
            jobDetails.put("requiredEducation", job.getRequiredEducation());  // Required Education
            jobDetails.put("requiredEducationStream", job.getRequiredEducationStream());  // Required Education Stream
            jobDetails.put("requiredPercentage", job.getRequiredPercentage());  // Required Percentage
            jobDetails.put("requiredPassoutYear", job.getRequiredPassoutYear());  // Required Passout Year
            jobDetails.put("requiredWorkExperience", job.getRequiredWorkExperience());  // Required Work Experience

            // Add recruiter details excluding sensitive information
            Map<String, Object> recruiterDetails = new LinkedHashMap<>();
            recruiterDetails.put("id", job.getRecruiter().getId());  // Recruiter ID
            recruiterDetails.put("recruiterName", job.getRecruiter().getRecruiterName());  // Recruiter Name
//            recruiterDetails.put("recruiterEmail", job.getRecruiter().getRecruiterEmail());  // Recruiter Email
            recruiterDetails.put("recruiterRole", job.getRecruiter().getRecruiterRole());  // Recruiter Role

            // Add recruiter details to the job details
            jobDetails.put("recruiter", recruiterDetails);

            // Add the job details (including recruiter details) to the list
            jobDetailsList.add(jobDetails);
        }

        return jobDetailsList;
    }




    // Method to get jobs with only job and admin details
    public List<Job> getJobsWithAdminDetails(Long adminId) {
        List<Job> jobs = jobRepository.findByJobAdmin_Id(adminId);

        // Remove recruiter details from each job
        for (Job job : jobs) {
            job.setRecruiter(null);
        }
        return jobs;
    }

    // Service method to retrieve and clean job data




    // Method to clean job data and remove unwanted fields
    public List<Job> getAllJobs() {
        // Fetch all jobs from the repository
        List<Job> allJobs = jobRepository.findAll();

        // Filter jobs to only include those with the status 'open'
        List<Job> openJobs = allJobs.stream()
                .filter(job -> "open".equals(job.getStatus())) // Assuming the 'status' field holds the job status
                .collect(Collectors.toList());

        // If no jobs with 'open' status, return an empty list
        if (openJobs.isEmpty()) {
            System.out.println("No open jobs available");
            return openJobs; // Return an empty list if no open jobs
        }

        // Print out the filtered jobs
        openJobs.forEach(job -> System.out.println("Job ID: " + job.getId() + ", Vacancy Count: " + job.getVacancyCount()));

        // Return the cleaned data for the filtered jobs
        return openJobs.stream()
                .map(this::cleanJobData)
                .collect(Collectors.toList());
    }

    // Method to clean job data and remove unwanted fields
    private Job cleanJobData(Job originalJob) {
        // Create a new Job object and copy required fields
        Job cleanedJob = new Job();
        cleanedJob.setId(originalJob.getId());
        cleanedJob.setJobTitle(originalJob.getJobTitle());
        cleanedJob.setJobDescription(originalJob.getJobDescription());
        cleanedJob.setRequiredSkills(originalJob.getRequiredSkills());
        cleanedJob.setLocation(originalJob.getLocation());
        cleanedJob.setMinSalary(originalJob.getMinSalary());
        cleanedJob.setMaxSalary(originalJob.getMaxSalary());
        cleanedJob.setJobType(originalJob.getJobType());
        cleanedJob.setCreatedAt(originalJob.getCreatedAt());
        cleanedJob.setVacancyCount(originalJob.getVacancyCount());
        cleanedJob.setAppliedCount(originalJob.getAppliedCount()); // Set the appliedCount field

        if (originalJob.getJobAdmin() != null) {
            JobAdmin originalJobAdmin = originalJob.getJobAdmin();
            JobAdmin cleanedJobAdmin = new JobAdmin();
            cleanedJobAdmin.setId(originalJobAdmin.getId());
            cleanedJobAdmin.setJobAdminCompanyName(originalJobAdmin.getJobAdminCompanyName());
            cleanedJob.setJobAdmin(cleanedJobAdmin);
        }

        // Return the new job object with only the required fields
        return cleanedJob;
    }

    public List<Job> getActiveJobs() {
        // Retrieve all jobs from the repository
        List<Job> allJobs = jobRepository.findAll();

        // Filter the jobs where status is "open"
        return allJobs.stream()
                .filter(job -> "open".equalsIgnoreCase(job.getStatus()))
                .collect(Collectors.toList());
    }







    public List<Job> findJobsBySkillsAndLocation(String skills, String location) {
        if (skills != null && location != null) {
            return jobRepository.findByRequiredSkillsContainingAndLocationContaining(skills, location);
        } else if (skills != null) {
            return jobRepository.findByRequiredSkillsContaining(skills);
        } else if (location != null) {
            return jobRepository.findByLocationContaining(location);
        } else {
            return jobRepository.findAll();
        }
    }
    public List<Recruiter> getRecruitersByJobAdminId(Long jobAdminId) {
        return recruiterRepository.findByJobAdminId(jobAdminId);
    }

    public Optional<Job> getJobDetails(Long jobId) {
        return jobRepository.findDetailedById(jobId);
    }


    public void deleteJob(Long adminId, Long recruiterId, Long jobId) {
        // Validate admin existence
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Validate recruiter existence
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        // Validate job existence
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Perform delete operation
        jobRepository.delete(job);
    }


    public void deleteJob(Long adminId, Long jobId) {
        // Validate admin existence
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Validate job existence
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Perform delete operation
        jobRepository.delete(job);
    }

    public Job updateJob(Long jobId, Long adminId, Job updatedJob) {
        // Fetch the job admin
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Fetch the job to be updated
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Update fields
        job.setJobTitle(updatedJob.getJobTitle());
        job.setJobDescription(updatedJob.getJobDescription());
        job.setBasicJobQualification(updatedJob.getBasicJobQualification());
        job.setPrimaryRoles(updatedJob.getPrimaryRoles());
        job.setMainResponsibilities(updatedJob.getMainResponsibilities());
        job.setRequiredSkills(updatedJob.getRequiredSkills());
        job.setLocation(updatedJob.getLocation());
        job.setMinSalary(updatedJob.getMinSalary());
        job.setMaxSalary(updatedJob.getMaxSalary());
        job.setJobType(updatedJob.getJobType());
        job.setStatus(updatedJob.getStatus());
        job.setVacancyCount(updatedJob.getVacancyCount());
        job.setAppliedCount(updatedJob.getAppliedCount());
        job.setNumberOfLevels(updatedJob.getNumberOfLevels());
        job.setRequiredEducation(updatedJob.getRequiredEducation());
        job.setRequiredEducationStream(updatedJob.getRequiredEducationStream());
        job.setRequiredPercentage(updatedJob.getRequiredPercentage());
        job.setRequiredPassoutYear(updatedJob.getRequiredPassoutYear());
        job.setRequiredWorkExperience(updatedJob.getRequiredWorkExperience());

        // Update recruiter and jobAdmin if provided
        if (updatedJob.getRecruiter() != null && updatedJob.getRecruiter().getId() != null) {
            Recruiter recruiter = recruiterRepository.findById(updatedJob.getRecruiter().getId())
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));
            job.setRecruiter(recruiter);
        }

        if (updatedJob.getJobAdmin() != null && updatedJob.getJobAdmin().getId() != null) {
            job.setJobAdmin(admin); // Admin is already fetched
        }

        // Save the updated job
        return jobRepository.save(job);
    }


    public Job updateJobByRecruiter(Long jobId, Long recruiterId, Job updatedJob) {
        // Check if the recruiter exists
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        // Fetch the job to be updated
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if the job was created by the recruiter
        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("Recruiter is not authorized to update this job");
        }

        // Update the job fields with the data from the updated job
        job.setJobTitle(updatedJob.getJobTitle());
        job.setJobDescription(updatedJob.getJobDescription());
        job.setRequiredSkills(updatedJob.getRequiredSkills());
        job.setLocation(updatedJob.getLocation());
        job.setMinSalary(updatedJob.getMinSalary());
        job.setMaxSalary(updatedJob.getMaxSalary());
        job.setJobType(updatedJob.getJobType());
        job.setStatus(updatedJob.getStatus());
        job.setVacancyCount(updatedJob.getVacancyCount());
        job.setRequiredEducation(updatedJob.getRequiredEducation());
        job.setRequiredEducationStream(updatedJob.getRequiredEducationStream());
        job.setRequiredPercentage(updatedJob.getRequiredPercentage());
        job.setRequiredPassoutYear(updatedJob.getRequiredPassoutYear());
        job.setRequiredWorkExperience(updatedJob.getRequiredWorkExperience());

        // Save the updated job
        return jobRepository.save(job);
    }


    @Transactional // Make this method transactional
    public void deleteJobByRecruiter(Long jobId, Long recruiterId) {
        // First, delete related saved jobs
        savedJobsRepository.deleteByJobId(jobId);

        // Now, delete the job
        jobRepository.deleteById(jobId);
    }
    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }
    public boolean closeJobByRecruiterIdAndJobId(Long recruiterId, Long jobId) {
        // Find job by jobId and recruiterId
        Job job = jobRepository.findById(jobId)
                .filter(j -> j.getRecruiter().getId().equals(recruiterId))
                .orElse(null);

        if (job != null) {
            // Update status to 'closed'
            job.setStatus("closed");
            jobRepository.save(job);  // Save updated job back to the database
            return true;
        }
        return false;
    }
    public boolean openJobByRecruiterIdAndJobId(Long recruiterId, Long jobId) {
        // Find job by jobId and recruiterId
        Job job = jobRepository.findById(jobId)
                .filter(j -> j.getRecruiter().getId().equals(recruiterId))
                .orElse(null);

        if (job != null) {
            // Update status to 'open'
            job.setStatus("open");
            jobRepository.save(job); // Save updated job back to the database
            return true;
        }
        return false;
    }
    public void deleteJobByAdmin(Long adminId, Long jobId) {
        // Validate admin existence
        JobAdmin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Validate job existence
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Perform delete operation
        jobRepository.delete(job);
    }

    public boolean checkIfJobIsOpen(Long jobId) {
        return jobRepository.isJobOpen(jobId);}

    @Transactional
    public void updateJobStatus(Long jobId, Long recruiterId, Long jobAdminId, String status) {
        // Validate that only one of recruiterId or jobAdminId is provided
        if ((recruiterId != null && jobAdminId != null) || (recruiterId == null && jobAdminId == null)) {
            throw new IllegalArgumentException("Provide either recruiterId or jobAdminId, but not both.");
        }

        // Validate the input status
        if (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("inactive")) {
            throw new IllegalArgumentException("Invalid status. Allowed values are 'active' or 'inactive'.");
        }

        // Find the job by ID
        Job job = jobRepository.findById(jobId).orElseThrow(() ->
                new IllegalArgumentException("Job with ID " + jobId + " not found."));

        // Validate the recruiter or job admin ID
        if (recruiterId != null) {
            if (job.getRecruiter() == null || !job.getRecruiter().getId().equals(recruiterId)) {
                throw new IllegalArgumentException("Recruiter with ID " + recruiterId + " is not authorized to update this job.");
            }
        } else if (jobAdminId != null) {
            if (job.getJobAdmin() == null || !job.getJobAdmin().getId().equals(jobAdminId)) {
                throw new IllegalArgumentException("Job Admin with ID " + jobAdminId + " is not authorized to update this job.");
            }
        }

        // Update the job status
        if (status.equalsIgnoreCase("active")) {
            job.setStatus("open");
        } else if (status.equalsIgnoreCase("inactive")) {
            job.setStatus("closed");
        }

        // Save the updated job
        jobRepository.save(job);
    }

    @Transactional
    public List<Job> getActiveJobs(Long recruiterId, Long jobAdminId) {
        if ((recruiterId != null && jobAdminId != null) || (recruiterId == null && jobAdminId == null)) {
            throw new IllegalArgumentException("Provide either recruiterId or jobAdminId, but not both.");
        }

        if (recruiterId != null) {
            return jobRepository.findByRecruiterIdAndStatus(recruiterId, "open");
        } else {
            return jobRepository.findByJobAdminIdAndStatus(jobAdminId, "open");
        }
    }

    @Transactional
    public List<Job> getActiveJobs(Long jobAdminId) {
        if (jobAdminId == null) {
            throw new IllegalArgumentException("Job Admin ID is required.");
        }
        return jobRepository.findByJobAdminIdAndRecruiterIdAndStatus(jobAdminId,null,"open");
    }
}
