package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepo submissionRepo;
    @Autowired
    private ActivityRepo activityRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    GradeRepo gradeRepo;
    @Autowired
    CriteriaRepo criteriaRepo;
    @Autowired
    CourseRepo courseRepo;

    @Autowired
    SearchRepo searchRepo;


    public Optional<Submission> findSubmissionById(Long id) {
        return submissionRepo.findById(id);
    }

    public List<Submission> findAllSubmissions() {
        return submissionRepo.findAll();
    }

    public void deleteSubmission(Long id) {
        submissionRepo.deleteById(id);
    }

    public List<Submission> findSubmissionsByActivity(Long activityId) {
        return submissionRepo.findByActivityId(activityId);
    }

    public List<Submission> findSubmissionsByProject(Long projectId) {
        return submissionRepo.findByProjectId(projectId);
    }

    public List<Submission> findSubmissionsByUser(Long userId) {
        return submissionRepo.findByUserId(userId);
    }


    public Submission saveSubmission(Submission submission,  Long userId, Long courseId) {

        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (userOpt.isPresent() && courseOptional.isPresent()) {

            submission.setUser(userOpt.get());
            submission.setCourse(courseOptional.get());
        } else {
            throw new IllegalArgumentException("Activity, User, or Project not found");
        }
        return submissionRepo.save(submission);
    }


    public Submission addSubmission(Submission submission, Long userId, Long courseId) {
        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (userOpt.isPresent() && courseOptional.isPresent()) {

            submission.setUser(userOpt.get());
            submission.setCourse(courseOptional.get());
        } else {
            throw new IllegalArgumentException("Activity, User, or Project not found");
        }
        return submissionRepo.save(submission);
    }
    public Submission saveSubmission(Submission submission,  Long userId, Long courseId,Long projectId) {

        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Activity> activityOptional = activityRepo.findById(courseId);
        Optional<Project> projectOptional = projectRepo.findById(projectId);

        if (userOpt.isPresent() && activityOptional.isPresent() && projectOptional.isPresent()) {

            submission.setUser(userOpt.get());
            submission.setProject(projectOptional.get());
            submission.setActivity(activityOptional.get());
        } else {
            throw new IllegalArgumentException("Activity, User, or Project not found");
        }
        return submissionRepo.save(submission);
    }



}






