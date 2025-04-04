package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private SubmissionRepo submissionRepo;
    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private CourseToolRepo courseToolRepo;
    @Autowired
    private SubmitProjectRepo submitProjectRepo;
    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private LessonModuleRepo lessonModuleRepo;

    @Autowired
    private QuizRepo quizRepo;


    @Autowired
    private VideoProgressRepo videoProgressRepo;

    @Autowired
    private AIAssignmentRepo aiAssignmentRepo;

    @Autowired
    private AIQuizRepo aiQuizRepo;

    @Autowired
    private TeacherFeedbackRepo teacherFeedbackRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Course saveCourse(Course course, Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            course.setUser(userOpt.get());
            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Optional<Course> findCourseById(Long id) {
        return courseRepo.findById(id);
    }

    public List<Course> findAllCourses() {
        return courseRepo.findAll();
    }



    @Transactional
    public void deleteCourseById(Long id) {
        videoProgressRepo.deleteByCourseId(id);

        lessonRepo.deleteByCourseId(id);
        enrollmentRepo.deleteByCourseId(id);
        projectRepo.deleteByCourseId(id);
        reviewRepo.deleteByCourseId(id);
        videoRepo.deleteByCourseId(id);
        ratingRepo.deleteByCourseId(id);
        submissionRepo.deleteByCourseId(id);
        assignmentRepo.deleteByCourseId(id);
        courseToolRepo.deleteByCourseId(id);
        submitProjectRepo.deleteByCourseId(id);
        announcementRepo.deleteByCourseId(id);
        lessonModuleRepo.deleteByCourseId(id);
        aiQuizRepo.deleteByCourseId(id);
        aiAssignmentRepo.deleteByCourseId(id);

        courseRepo.deleteById(id);
    }

    public List<Lesson> getCourseLessons(Long courseId) {
        return lessonRepo.findByCourseId(courseId);
    }


    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepo.findByCourseId(courseId);
    }


    public List<Project> getCourseProjects(Long courseId) {
        return projectRepo.findByCourseId(courseId);
    }


    public List<Review> getCourseReviews(Long courseId) {
        return reviewRepo.findByCourseId(courseId);
    }


    public List<Video> getCourseVideos(Long courseId) {
        return videoRepo.findByCourseId(courseId);
    }

    public Optional<Course> getCourseById(long id) {
        return courseRepo.findById(id);
    }
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepo.findById(courseId);
    }

    public Course savingCourse(Course existingCourse) {
        return courseRepo.save(existingCourse);
    }


    public Course generatePromoCode(Long courseId, Long discountPercentage, LocalDateTime promoCodeExpiration, Long courseFee, String currency, String coursePercentage) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            String promoCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            course.setPromoCode(promoCode);

            course.setPromoCodeExpiration(promoCodeExpiration);
            course.setDiscountPercentage(discountPercentage);
            course.setCourseFee(courseFee);
            course.setCurrency(currency);
            course.setCoursePercentage(coursePercentage);

            Long discountedFee = (long) (course.getCourseFee() * (1 - (discountPercentage / 100.0)));
            course.setDiscountFee(discountedFee);

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    public Course applyPromoCode(Long courseId, String promoCode) {
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (course.getPromoCode().equals(promoCode) && LocalDateTime.now().isBefore(course.getPromoCodeExpiration())) {
                // Apply discount
                Long discountedFee = (long) (course.getCourseFee() * (1 - (course.getDiscountPercentage() / 100.0)));
                course.setDiscountFee(discountedFee);
                return courseRepo.save(course);
            } else {
                throw new IllegalArgumentException("Invalid or expired promo code");
            }
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    public String getPromoCode(Long courseId) {
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isPresent()) {
            return optionalCourse.get().getPromoCode();
        } else {
            throw new RuntimeException("Course not found with id: " + courseId);

        }
    }
        public List<Course> findOlderCourses() {
            return courseRepo.findTop20ByOrderByCreatedAtAsc();
        }

        public List<Course> findRecentCourses() {
            return courseRepo.findTop20ByOrderByCreatedAtDesc();
        }

        public List<Course> findUpdatedCourses() {
            return courseRepo.findTop20ByOrderByUpdatedAtDesc();
        }



    public Course addCourse(Long userId, String courseTitle, String courseDescription, String courseLanguage, Course.Level level,

                            String level_1, String level_2, String level_3, String level_4, String level_5,
                            String level_6, String level_7, String level_8,
                             MultipartFile courseImage, String promoCode,Long courseFee,

                            String currency, Long discountFee, Long discountPercentage,String courseDuration,String courseType,String coursePercentage,MultipartFile courseDocument) throws IOException {

        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            Course course = new Course();
            course.setUser(userOpt.get());
            course.setCourseTitle(courseTitle);
            course.setCourseDescription(courseDescription);
            course.setCourseDuration(courseDuration);
            course.setCourseLanguage(courseLanguage);
            course.setLevel(level);
            course.setLevel_1(level_1);
            course.setLevel_2(level_2);
            course.setLevel_3(level_3);
            course.setLevel_4(level_4);
            course.setLevel_5(level_5);
            course.setLevel_6(level_6);
            course.setLevel_7(level_7);
            course.setLevel_8(level_8);
            course.setCourseImage(courseImage.getBytes());
            course.setCourseFee(courseFee);
            course.setDiscountFee(discountFee);
            course.setDiscountPercentage(discountPercentage);
            course.setPromoCode(promoCode);
            course.setCurrency(currency);
            course.setCourseType(courseType);
            course.setCoursePercentage(coursePercentage);
            course.setCourseDocument(courseDocument.getBytes());
            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void updateCourseFields(Course existingCourse, Course courseUpdates, MultipartFile courseImage) {

        if (courseUpdates.getCourseTitle() != null) {
            existingCourse.setCourseTitle(courseUpdates.getCourseTitle());
        }
        if (courseUpdates.getCourseDescription() != null) {
            existingCourse.setCourseDescription(courseUpdates.getCourseDescription());

        }
        if (courseUpdates.getCourseLanguage() != null) {
            existingCourse.setCourseLanguage(courseUpdates.getCourseLanguage());
        }
        if (courseUpdates.getLevel() != null) {
            existingCourse.setLevel(courseUpdates.getLevel());
        }
        if (courseUpdates.getLevel_1() != null) {
            existingCourse.setLevel_1(courseUpdates.getLevel_1());
        }
        if (courseUpdates.getLevel_2() != null) {
            existingCourse.setLevel_2(courseUpdates.getLevel_2());
        }
        if (courseUpdates.getLevel_3() != null) {
            existingCourse.setLevel_3(courseUpdates.getLevel_3());
        }
        if (courseUpdates.getLevel_4() != null) {
            existingCourse.setLevel_4(courseUpdates.getLevel_4());
        }
        if (courseUpdates.getLevel_5() != null) {
            existingCourse.setLevel_5(courseUpdates.getLevel_5());
        }

        if (courseUpdates.getLevel_6() != null) {
            existingCourse.setLevel_6(courseUpdates.getLevel_6());
        }
        if (courseUpdates.getLevel_7() != null) {
            existingCourse.setLevel_7(courseUpdates.getLevel_7());
        }
        if (courseUpdates.getLevel_8() != null) {
            existingCourse.setLevel_8(courseUpdates.getLevel_8());
        }

        if (courseUpdates.getCurrency() != null) {
            existingCourse.setCurrency(courseUpdates.getCurrency());
        }
        if (courseUpdates.getCourseFee() != null) {
            existingCourse.setCourseFee(courseUpdates.getCourseFee());
        }

        if (courseUpdates.getCourseDuration() != null) {
            existingCourse.setCourseDuration(courseUpdates.getCourseDuration());
        }
        if (courseUpdates.getDiscountFee() != null) {
            existingCourse.setDiscountFee(courseUpdates.getDiscountFee());
        }
        if (courseUpdates.getCourseType() != null) {
            existingCourse.setCourseType(courseUpdates.getCourseType());
        }
        if (courseUpdates.getCoursePercentage() != null) {
            existingCourse.setCourseType(courseUpdates.getCoursePercentage());
        }

        if (courseImage != null && !courseImage.isEmpty()) {
            try {
                existingCourse.setCourseImage(courseImage.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update tool image", e);
            }
        }
    }

    public Course addNewCourse(long userId, String courseTitle, String courseDescription, String CourseLanguage, Course.Level level, String level1, String level2, String level3, String level4, String level5, String level6, String level7, String level8, MultipartFile courseImage, String courseDuration,String courseType,String courseTerm) throws IOException {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            Course course = new Course();
            course.setUser(userOpt.get());
            course.setCourseTitle(courseTitle);
            course.setCourseDescription(courseDescription);
            course.setCourseLanguage(CourseLanguage);
            course.setLevel(level);
            course.setLevel_1(level1);
            course.setLevel_2(level2);
            course.setLevel_3(level3);
            course.setLevel_4(level4);
            course.setLevel_5(level5);
            course.setLevel_6(level6);
            course.setLevel_7(level7);
            course.setLevel_8(level8);
            course.setCourseImage(courseImage.getBytes());
            course.setCourseDuration(courseDuration);
            course.setCourseType(courseType);
            course.setCourseTerm(courseTerm);


            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    public List<Course> getCoursesByCourseType(String courseType) {
        return courseRepo.findByCourseType(courseType);
    }

    public List<Course> getCoursesByUserId(Long userId) {
        return courseRepo.findByUserId(userId);
    }

    public Course updateNewCourse(long courseId, String courseTitle, String courseDescription, String CourseLanguage, Course.Level level, String level_1, String level_2, String level_3, String level_4, String level_5, String level_6, String level_7, String level_8, MultipartFile courseImage, String courseDuration, String courseType,String courseTerm) throws IOException {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course not found"));

        course.setCourseTitle(courseTitle);
        course.setCourseDescription(courseDescription);
        course.setCourseLanguage(CourseLanguage);
        course.setLevel(level);
        course.setLevel_1(level_1);
        course.setLevel_2(level_2);
        course.setLevel_3(level_3);
        course.setLevel_4(level_4);
        course.setLevel_5(level_5);
        course.setLevel_6(level_6);
        course.setLevel_7(level_7);
        course.setLevel_8(level_8);
        course.setCourseDuration(courseDuration);
        course.setCourseType(courseType);
        course.setCourseTerm(courseTerm );

        if (!courseImage.isEmpty()) {
            course.getCourseImage();
        }

        return courseRepo.save(course);
    }


    public Course getNewCourseById(long courseId) {
        return courseRepo.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course not found"));
    }

    public Course patchCourseById(long id, Map<String, Object> updates) {
        Optional<Course> optionalCourse = courseRepo.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "courseTitle":
                        course.setCourseTitle((String) value);
                        break;
                    case "courseDescription":
                        course.setCourseDescription((String) value);
                        break;
                    case "language":
                        course.setCourseLanguage((String) value);
                        break;
                    case "level":
                        course.setLevel(Course.Level.valueOf((String) value));
                        break;
                    case "courseDuration":
                        course.setCourseDuration((String) value);
                        break;
                    case "courseFee":
                        course.setCourseFee(Long.valueOf(value.toString()));
                        break;
                    case "discountFee":
                        course.setDiscountFee(Long.valueOf(value.toString()));
                        break;
                    case "discountPercentage":
                        course.setDiscountPercentage(Long.valueOf(value.toString()));
                        break;
                    case "currency":
                        course.setCurrency((String) value);
                        break;
                    case "promoCodeExpiration":
                        course.setPromoCodeExpiration(LocalDateTime.parse((String) value));
                        break;
                    case "promoCode":
                        course.setPromoCode((String) value);
                        break;
                    case "courseType":
                        course.setCourseType((String) value);
                        break;
                    case "coursePercentage":
                        course.setCoursePercentage((String) value);
                        break;
                    case "courseImage":
                        course.setCourseImage((byte[]) value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field: " + key);
                }
            });
            course.setUpdatedAt(LocalDateTime.now());
            return courseRepo.save(course);
        }
        return null;
    }

    public List<Course> getCoursesByUser(User user) {
        return courseRepo.findByUser(user);
    }

    public Optional<Course> findById(Long id) {
        return courseRepo.findById(id);
    }



    public List<Course> getByUserId(long userId) {
        return courseRepo.findByUserId(userId);
    }



    public void updateCourseDocument(long id, MultipartFile document) throws IOException {
        Optional<Course> courseOptional = courseRepo.findById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setCourseDocument(document.getBytes());
            courseRepo.save(course);
        } else {
            throw new RuntimeException("Course not found.");
        }
    }


    public Course getQuizById(Long courseId) {
        return courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseId));
    }

    public List<Map<String, Object>> getCourseDetailsWithContentByUserId(long userId) {
        List<Course> courses = courseRepo.findCoursesByUserId(userId);
        List<Map<String, Object>> results = new ArrayList<>();

        System.out.println("Courses retrieved for user ID " + userId + ": " + courses.size());

        for (Course course : courses) {
            System.out.println("Processing course: " + course.getCourseTitle());

            Map<String, Object> courseDetails = new HashMap<>();
            courseDetails.put("courseTitle", course.getCourseTitle());
            courseDetails.put("courseDescription", course.getCourseDescription());

            courseDetails.put("projectTitles", course.getProjects().stream()
                    .map(Project::getProjectTitle)
                    .collect(Collectors.toList()));
            courseDetails.put("assignmentTitles", course.getAssignments().stream()
                    .map(Assignment::getAssignmentTitle)
                    .collect(Collectors.toList()));
            courseDetails.put("quizNames", course.getQuizzes().stream()
                    .map(Quiz::getQuizName)
                    .collect(Collectors.toList()));

            results.add(courseDetails);
        }

        System.out.println("Total courses found for user ID " + userId + ": " + results.size());

        return results;
    }

    public Course findById(long id) {
        return courseRepo.findById(id).orElse(null);
    }

    public List<Course> getOnlineCoursesByUser(Long userId, String courseType) {
        return courseRepo.findByUserIdAndCourseType(userId, courseType);

    }

    public Course addCourses(long userId, String courseDescription, String CourseLanguage, Course.Level level, String level1, String level2, String level3, String level4, String level5, String level6, String level7, String level8, MultipartFile courseImage, String courseDuration,String courseType,String courseTerm) throws IOException {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            Course course = new Course();
            course.setUser(userOpt.get());
            course.setCourseDescription(courseDescription);
            course.setCourseLanguage(CourseLanguage);
            course.setLevel(level);
            course.setLevel_1(level1);
            course.setLevel_2(level2);
            course.setLevel_3(level3);
            course.setLevel_4(level4);
            course.setLevel_5(level5);
            course.setLevel_6(level6);
            course.setLevel_7(level7);
            course.setLevel_8(level8);
            course.setCourseImage(courseImage.getBytes());
            course.setCourseDuration(courseDuration);
            course.setCourseType(courseType);
            course.setCourseTerm(courseTerm);
            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    public Course createCourse(Long userId, String courseTitle) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = new Course();
        course.setCourseTitle(courseTitle);
        course.setUser(user);

        return courseRepo.save(course);
    }

    public Course addNewCourseById(long courseId, String courseDescription, String courseLanguage, Course.Level level, String level1, String level2, String level3, String level4, String level5, String level6, String level7, String level8, MultipartFile courseImage, String courseDuration, String courseType, String courseTerm) throws IOException {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setCourseDescription(courseDescription);
            course.setCourseLanguage(courseLanguage);
            course.setLevel(level);
            course.setLevel_1(level1);
            course.setLevel_2(level2);
            course.setLevel_3(level3);
            course.setLevel_4(level4);
            course.setLevel_5(level5);
            course.setLevel_6(level6);
            course.setLevel_7(level7);
            course.setLevel_8(level8);
            course.setCourseImage(courseImage.getBytes());
            course.setCourseDuration(courseDuration);
            course.setCourseType(courseType);
            course.setCourseTerm(courseTerm);

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found");
        }
    }

    public Course updateCourseDetails(long courseId, Long courseFee, Long discountPercentage, String currency, String promoCodeExpiration) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            if (courseFee != null) {
                course.setCourseFee(courseFee);
            }
            if (discountPercentage != null) {
                course.setDiscountPercentage(discountPercentage);
            }
            if (currency != null) {
                course.setCurrency(currency);
            }
            if (promoCodeExpiration != null) {
                course.setPromoCodeExpiration(LocalDateTime.parse(promoCodeExpiration));
            }
            course.setUpdatedAt(LocalDateTime.now());
            return courseRepo.save(course);
        } else {
            throw new RuntimeException("Course not found with id " + courseId);
        }
    }

    public List<Object[]> getCourseDetailsByTeacherIdAndCourseId(Long teacherId, Long courseId) {
        List<Object[]> assignments = assignmentRepo.findAssignmentsByTeacherIdAndCourseId(teacherId, courseId);
        List<Object[]> projects = projectRepo.findProjectsByTeacherIdAndCourseId(teacherId, courseId);
        List<Object[]> quizzes = quizRepo.findQuizzesByTeacherIdAndCourseId(teacherId, courseId);

        List<Object[]> combinedResults = new ArrayList<>();
        combinedResults.addAll(assignments);
        combinedResults.addAll(projects);
        combinedResults.addAll(quizzes);

        return combinedResults;
    }

    public Course addNewCoursesById(long courseId, String courseDescription, String courseLanguage, Course.Level level, String level1, String level2, String level3, String level4, String level5, String level6, String level7, String level8, MultipartFile courseImage, String courseDuration, String courseType, String courseTerm,String coursePercentage) throws IOException {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setCourseDescription(courseDescription);
            course.setCourseLanguage(courseLanguage);
            course.setLevel(level);
            course.setLevel_1(level1);
            course.setLevel_2(level2);
            course.setLevel_3(level3);
            course.setLevel_4(level4);
            course.setLevel_5(level5);
            course.setLevel_6(level6);
            course.setLevel_7(level7);
            course.setLevel_8(level8);
            course.setCourseImage(courseImage.getBytes());
            course.setCourseDuration(courseDuration);
            course.setCourseType(courseType);
            course.setCourseTerm(courseTerm);
            course.setCoursePercentage(coursePercentage);

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found");
        }
    }

    public Course generatedPromoCode(Long courseId, Long discountPercentage, LocalDateTime promoCodeExpiration, Long courseFee, String currency) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            String promoCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            course.setPromoCode(promoCode);

            course.setPromoCodeExpiration(promoCodeExpiration);
            course.setDiscountPercentage(discountPercentage);
            course.setCourseFee(courseFee);
            course.setCurrency(currency);

            Long discountedFee = (long) (course.getCourseFee() * (1 - (discountPercentage / 100.0)));
            course.setDiscountFee(discountedFee);

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    public Course updateCourseById(long courseId, String courseDescription, String courseLanguage, Course.Level level, String level1, String level2, String level3, String level4, String level5, String level6, String level7, String level8, MultipartFile courseImage, String courseDuration, String courseType, String courseTerm, String coursePercentage) throws IOException {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            // Update only if the new values are provided
            if (courseDescription != null && !courseDescription.isEmpty()) {
                course.setCourseDescription(courseDescription);
            }
            if (courseLanguage != null && !courseLanguage.isEmpty()) {
                course.setCourseLanguage(courseLanguage);
            }
            if (level != null) {
                course.setLevel(level);
            }
            if (level1 != null && !level1.isEmpty()) {
                course.setLevel_1(level1);
            }
            if (level2 != null && !level2.isEmpty()) {
                course.setLevel_2(level2);
            }
            if (level3 != null && !level3.isEmpty()) {
                course.setLevel_3(level3);
            }
            if (level4 != null && !level4.isEmpty()) {
                course.setLevel_4(level4);
            }
            if (level5 != null && !level5.isEmpty()) {
                course.setLevel_5(level5);
            }
            if (level6 != null && !level6.isEmpty()) {
                course.setLevel_6(level6);
            }
            if (level7 != null && !level7.isEmpty()) {
                course.setLevel_7(level7);
            }
            if (level8 != null && !level8.isEmpty()) {
                course.setLevel_8(level8);
            }
            if (courseImage != null && !courseImage.isEmpty()) {
                course.setCourseImage(courseImage.getBytes());
            }
            if (courseDuration != null && !courseDuration.isEmpty()) {
                course.setCourseDuration(courseDuration);
            }
            if (courseType != null && !courseType.isEmpty()) {
                course.setCourseType(courseType);
            }
            if (courseTerm != null && !courseTerm.isEmpty()) {
                course.setCourseTerm(courseTerm);
            }
            if (coursePercentage != null && !coursePercentage.isEmpty()) {
                course.setCoursePercentage(coursePercentage);
            }

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found");
        }
    }

    public Course updatePromoCode(Long courseId, Long discountPercentage, LocalDateTime promoCodeExpiration, Long courseFee, String currency) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            // Update only if the value is provided, otherwise keep the existing value
            if (discountPercentage != null) {
                course.setDiscountPercentage(discountPercentage);

                // Ensure courseFee is not null before performing calculations
                Long feeToApplyDiscount = (course.getCourseFee() != null) ? course.getCourseFee() : 0L;
                Long discountedFee = (long) (feeToApplyDiscount * (1 - (discountPercentage / 100.0)));
                course.setDiscountFee(discountedFee);
            }

            if (promoCodeExpiration != null) {
                course.setPromoCodeExpiration(promoCodeExpiration);
            }

            if (courseFee != null) {
                course.setCourseFee(courseFee);

                // Recalculate discounted fee if discount is present
                if (discountPercentage != null) {
                    Long discountedFee = (long) (courseFee * (1 - (discountPercentage / 100.0)));
                    course.setDiscountFee(discountedFee);
                }
            }

            if (currency != null) {
                course.setCurrency(currency);
            }

            return courseRepo.save(course);
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    public List<Map<String, Object>> getCourseProgress(Long studentId) {
        // Retrieve the user's enrollments
        List<Enrollment> enrollments = enrollmentRepo.findByUserId(studentId);

        // Add logging to see if enrollments exist
        System.out.println("Enrollments for studentId=" + studentId + ": " + enrollments);

        List<Map<String, Object>> progressCourses = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            Long courseId = course.getId();
            String courseTitle = course.getCourseTitle();

            double totalProgress = 0;
            int itemCount = 0;

            // Check grades for Assignments
            List<TeacherFeedback> assignmentFeedbacks = teacherFeedbackRepo.findByStudent_IdAndAssignment_Id(studentId, courseId);
            System.out.println("Assignment feedbacks for studentId=" + studentId + ": " + assignmentFeedbacks);

            for (TeacherFeedback feedback : assignmentFeedbacks) {
                totalProgress += getProgressForGrade(feedback.getGrade());
                itemCount++;
            }

            // Check grades for Quizzes
            List<TeacherFeedback> quizFeedbacks = teacherFeedbackRepo.findByStudent_IdAndQuiz_Id(studentId, courseId);
            System.out.println("Quiz feedbacks for studentId=" + studentId + ": " + quizFeedbacks);

            for (TeacherFeedback feedback : quizFeedbacks) {
                totalProgress += getProgressForGrade(feedback.getGrade());
                itemCount++;
            }

            // Check grades for Projects
            List<TeacherFeedback> projectFeedbacks = teacherFeedbackRepo.findByStudent_IdAndProject_Id(studentId, courseId);
            System.out.println("Project feedbacks for studentId=" + studentId + ": " + projectFeedbacks);

            for (TeacherFeedback feedback : projectFeedbacks) {
                totalProgress += getProgressForGrade(feedback.getGrade());
                itemCount++;
            }

            // Calculate overall progress for the course
            double overallProgress = itemCount > 0 ? totalProgress / itemCount : 0;

            // Prepare a map with course details and calculated progress
            Map<String, Object> courseProgress = new HashMap<>();
            courseProgress.put("courseId", courseId);
            courseProgress.put("courseTitle", courseTitle);
            courseProgress.put("overallProgress", overallProgress);

            progressCourses.add(courseProgress);
        }

        return progressCourses;
    }

    // Method to calculate progress based on grade ranges
    private double getProgressForGrade(String grade) {
        switch (grade.toUpperCase()) {
            case "A":
                return 92.5;  // Average of 85 and 100
            case "B":
                return 77.5;  // Average of 70 and 85
            case "C":
                return 62.5;  // Average of 55 and 70
            case "D":
                return 47.5;  // Average of 40 and 55
            case "E":
                return 30;    // Less than 40
            default:
                return 0;     // If no valid grade is provided
        }
    }

    public List<Course> getCoursesWithPercentageAbove90() {
        return courseRepo.findCoursesWithPercentageAbove90();
    }

    public List<byte[]> getEnrolledCourseDocuments(Long userId) {
        String sql = "SELECT c.course_document FROM enrollment e " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.user_id = :userId";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);

        List<byte[]> courseDocuments = query.getResultList();

        return courseDocuments.stream()
                .filter(Objects::nonNull) // Ensure no null values
                .collect(Collectors.toList());
    }
}

