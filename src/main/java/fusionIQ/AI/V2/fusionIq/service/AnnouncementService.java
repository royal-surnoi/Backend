package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Announcement;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.repository.AnnouncementRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private  AnnouncementRepo announcementRepo;

    @Autowired
    private CourseRepo courseRepo;

    public AnnouncementService(AnnouncementRepo announcementRepo) {
        this.announcementRepo = announcementRepo;
    }


    public Announcement saveAnnouncement(Announcement announcement, Long courseId) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            announcement.setCourse(course);
        } else {
            throw new IllegalArgumentException("Course not found with id " + courseId);
        }
        return announcementRepo.save(announcement);
    }


    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepo.findById(id);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepo.findAll();
    }
}
