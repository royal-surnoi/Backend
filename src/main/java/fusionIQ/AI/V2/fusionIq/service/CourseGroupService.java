package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseGroupService {

    @Autowired
    private CourseGroupRepo courseGroupRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private QuizRepo quizRepo;

    public CourseGroup createProjectCourseGroup(String groupName, Long teacherId) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        CourseGroup courseGroup = new CourseGroup();
        courseGroup.setGroupName(groupName);
        courseGroup.setTeacher(teacher);

        courseGroup.setCreatedAt(LocalDateTime.now());

        return courseGroupRepo.save(courseGroup);
    }

    public List<CourseGroup> getCourseGroupsByTeacherId(Long teacherId) {
        return courseGroupRepo.findByTeacherId(teacherId);
    }

    public Optional<CourseGroup> updateCourseGroup(Long id, CourseGroup updatedCourseGroup) {
        return courseGroupRepo.findById(id).map(existingCourseGroup -> {
            if (updatedCourseGroup.getGroupName() != null) {
                existingCourseGroup.setGroupName(updatedCourseGroup.getGroupName());
            }
            if (updatedCourseGroup.getTeacher() != null) {
                existingCourseGroup.setTeacher(updatedCourseGroup.getTeacher());
            }

            if (updatedCourseGroup.getCreatedAt() != null) {
                existingCourseGroup.setCreatedAt(updatedCourseGroup.getCreatedAt());
            }
            return courseGroupRepo.save(existingCourseGroup);
        });
    }

    public void deleteCourseGroup(Long id) {
        courseGroupRepo.deleteById(id);
    }

    public void deleteCourseGroupByGroupName(String groupName) {
        courseGroupRepo.deleteByGroupName(groupName);
    }

    public CourseGroup createAssignmentCourseGroup(String groupName, Long teacherId) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));


        CourseGroup courseGroup = new CourseGroup();
        courseGroup.setGroupName(groupName);
        courseGroup.setTeacher(teacher);

        courseGroup.setCreatedAt(LocalDateTime.now());

        return courseGroupRepo.save(courseGroup);
    }



}
