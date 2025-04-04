package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.service.AssignmentService;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import fusionIQ.AI.V2.fusionIq.service.ProjectService;
import fusionIQ.AI.V2.fusionIq.service.QuizService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class  CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CourseRepo courseRepo;
    @PostMapping("/save")
    public ResponseEntity<Course> save(@RequestBody Course course, @RequestParam long userId) {
        try {
            Course savedCourse = courseService.saveCourse(course, userId);
            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getBy/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.findCourseById(id);
        return courseOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allCourses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.findAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/lessons/{courseId}")
    public ResponseEntity<List<Lesson>> getCourseLessons(@PathVariable Long courseId) {
        List<Lesson> lessons = courseService.getCourseLessons(courseId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/enrollments/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        List<Enrollment> enrollments = courseService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/projects/{courseId}")
    public ResponseEntity<List<Project>> getCourseProjects(@PathVariable Long courseId) {
        List<Project> projects = courseService.getCourseProjects(courseId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/reviews/{courseId}")
    public ResponseEntity<List<Review>> getCourseReviews(@PathVariable Long courseId) {
        List<Review> reviews = courseService.getCourseReviews(courseId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/generatePromoCode/{courseId}")
    public ResponseEntity<Course> generatePromoCode(@PathVariable Long courseId,
                                                    @RequestParam(required = false) Long discountPercentage,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime promoCodeExpiration,
                                                    @RequestParam Long courseFee,
                                                    @RequestParam String currency,
                                                    @RequestParam String coursePercentage) {
        Course generatedCourse = courseService.generatePromoCode(courseId, discountPercentage, promoCodeExpiration, courseFee, currency,coursePercentage);
        return ResponseEntity.ok(generatedCourse);
    }

    @PostMapping("/applyPromoCode/{courseId}")
    public ResponseEntity<Course> applyPromoCode(@PathVariable Long courseId,
                                                 @RequestParam String promoCode) {
        Course updatedCourse = courseService.applyPromoCode(courseId, promoCode);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/getPromoCode/{courseId}")
    public String getPromoCode(@PathVariable Long courseId) {
        return courseService.getPromoCode(courseId);
    }

    @GetMapping("/olderCourses")
    public ResponseEntity<List<Course>> getOlderCourses() {
        List<Course> olderCourses = courseService.findOlderCourses();
        return ResponseEntity.ok(olderCourses);
    }

    @GetMapping("/recentCourses")
    public ResponseEntity<List<Course>> getRecentCourses() {
        List<Course> recentCourses = courseService.findRecentCourses();
        return ResponseEntity.ok(recentCourses);
    }

    @GetMapping("/updatedCourses")
    public ResponseEntity<List<Course>> getUpdatedCourses() {
        List<Course> updatedCourses = courseService.findUpdatedCourses();
        return ResponseEntity.ok(updatedCourses);
    }

    @PostMapping("/saveCourse/{userId}")
    public ResponseEntity<Course> addCourses(
            @PathVariable long userId,

            @RequestParam String courseTitle,
            @RequestParam String courseDescription,
            @RequestParam String courseLanguage,
            @RequestParam Course.Level level,
            @RequestParam String level_1,
            @RequestParam String level_2,
            @RequestParam String level_3,
            @RequestParam String level_4,
            @RequestParam String level_5,
            @RequestParam String level_6,
            @RequestParam String level_7,
            @RequestParam String level_8,

            @RequestParam String currency,
            @RequestParam Long courseFee,
            @RequestParam String promoCode,
            @RequestParam Long discountFee,
            @RequestParam Long discountPercentage,
            @RequestParam("courseImage") MultipartFile courseImage,
            @RequestParam String courseDuration,
            @RequestParam String courseType,
            @RequestParam String coursePercentage,
            @RequestParam("file") MultipartFile courseDocument) {


        try {

            Course savedCourse = courseService.addCourse(userId, courseTitle, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8,courseImage,promoCode,courseFee, currency, discountFee, discountPercentage,courseDuration,courseType,coursePercentage,courseDocument);


            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PutMapping("/updateCourses/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable("id") long id,
            @RequestBody Course courseUpdates,
            @RequestParam(value = "toolImage", required = false) MultipartFile toolImage) {

        Optional<Course> existingCourseOptional = courseService.getCourseById(id);
        if (existingCourseOptional.isPresent()) {
            Course existingCourse = existingCourseOptional.get();

            courseService.updateCourseFields(existingCourse, courseUpdates, toolImage);

            Course savedCourse = courseService.savingCourse(existingCourse);
            return ResponseEntity.ok(savedCourse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/saveNewCourse/{userId}")
    public ResponseEntity<Course> addNewCourses(
            @PathVariable long userId,
            @RequestParam String courseTitle,
            @RequestParam String courseDescription,
            @RequestParam String courseLanguage,
            @RequestParam Course.Level level,
            @RequestParam String level_1,
            @RequestParam String level_2,
            @RequestParam String level_3,
            @RequestParam String level_4,
            @RequestParam String level_5,
            @RequestParam String level_6,
            @RequestParam String level_7,
            @RequestParam String level_8,
            @RequestParam("courseImage") MultipartFile courseImage,
            @RequestParam String courseDuration,

            @RequestParam String courseType,
            @RequestParam String courseTerm) {
        try {
            Course savedCourse = courseService.addNewCourse(userId, courseTitle, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage,courseDuration, courseType,courseTerm);
            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/byCourseType")
    public List<Course> getCoursesByCourseType(@RequestParam String courseType) {
        return courseService.getCoursesByCourseType(courseType);
    }


    @GetMapping("/{id}/getPercentage")
    public ResponseEntity<String> getCoursePercentage(@PathVariable Long id) {
        Optional<Course> course = courseRepo.findById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get().getCoursePercentage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update course percentage by course ID
    @PutMapping("/{id}/updatePercentage")
    public ResponseEntity<Course> updateCoursePercentage(@PathVariable Long id, @RequestBody String newPercentage) {
        Optional<Course> course = courseRepo.findById(id);
        if (course.isPresent()) {
            Course updatedCourse = course.get();
            updatedCourse.setCoursePercentage(newPercentage);
            courseRepo.save(updatedCourse);
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/postPercentage")
    public ResponseEntity<Course> saveCoursePercentage(@PathVariable Long id, @RequestBody String newPercentage) {
        Optional<Course> course = courseRepo.findById(id);
        if (course.isPresent()) {
            Course existingCourse = course.get();
            existingCourse.setCoursePercentage(newPercentage);
            courseRepo.save(existingCourse);
            return ResponseEntity.ok(existingCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        List<Course> courses = courseService.getCoursesByUserId(userId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}/details")
    public Course getCourseFeeDetails(@PathVariable long id) {
        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            Course response = new Course();
            response.setCourseFee(course.getCourseFee());
            response.setDiscountPercentage(course.getDiscountPercentage());
            response.setCurrency(course.getCurrency());
            response.setPromoCode(course.getPromoCode());
            response.setPromoCodeExpiration(course.getPromoCodeExpiration());
            return response;
        } else {
            throw new RuntimeException("CourseId not found " + id);
        }
    }

    @PutMapping("/updateAllCourse/{id}")
    public ResponseEntity<Course> updateAllCourse(@PathVariable("id") long id, @RequestBody Course course) {
        Optional<Course> courseOptional = courseService.getCourseById(id);
        if (courseOptional.isPresent()) {
            Course existingCourse = courseOptional.get();

            if (course.getCourseType() != null) {
                existingCourse.setCourseType(course.getCourseType());
            }

            if (course.getCourseDescription() != null) {
                existingCourse.setCourseDescription(course.getCourseDescription());
            }
            if (course.getCourseLanguage() != null) {
                existingCourse.setCourseLanguage(course.getCourseLanguage());
            }
            if (course.getLevel() != null) {
                existingCourse.setLevel(course.getLevel());
            }
            if (course.getCourseDuration() != null) {
                existingCourse.setCourseDuration(course.getCourseDuration());
            }
            if (course.getCourseFee() != null) {
                existingCourse.setCourseFee(course.getCourseFee());
            }
            if (course.getDiscountFee() != null) {
                existingCourse.setDiscountFee(course.getDiscountFee());
            }
            if (course.getDiscountPercentage() != null) {
                existingCourse.setDiscountPercentage(course.getDiscountPercentage());
            }
            if (course.getCurrency() != null) {
                existingCourse.setCurrency(course.getCurrency());
            }
            if (course.getLevel_1() != null) {
                existingCourse.setLevel_1(course.getLevel_1());
            }

            if (course.getLevel_2() != null) {
                existingCourse.setLevel_2(course.getLevel_2());
            }
            if (course.getLevel_3() != null) {
                existingCourse.setLevel_3(course.getLevel_3());
            }
            if (course.getLevel_4() != null) {
                existingCourse.setLevel_4(course.getLevel_4());
            }
            if (course.getLevel_5() != null) {
                existingCourse.setLevel_5(course.getLevel_5());
            }
            if (course.getLevel_6() != null) {
                existingCourse.setLevel_6(course.getLevel_6());
            }
            if (course.getLevel_7() != null) {
                existingCourse.setLevel_7(course.getLevel_7());
            }
            if (course.getLevel_8() != null) {
                existingCourse.setLevel_8(course.getLevel_8());
            }
            if (course.getPromoCode() != null) {
                existingCourse.setPromoCode(course.getPromoCode());
            }
            if (course.getCourseType() != null) {
                existingCourse.setCourseType(course.getCourseType());
            }
            if (course.getCoursePercentage() != null) {
                existingCourse.setCoursePercentage(course.getCoursePercentage());
            }
            if (course.getCourseImage() != null) {
                existingCourse.setCourseImage(course.getCourseImage());
            }
            if (course.getCourseDocument() != null) {
                existingCourse.setCourseDocument(course.getCourseDocument());
            }


            Course saveCourse = courseService.savingCourse(existingCourse);
            return ResponseEntity.ok(saveCourse);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}/updatedDetails")
    public Course updateCourseDetails(
            @PathVariable long id,
            @RequestParam(required = false) Long courseFee,
            @RequestParam(required = false) Long discountPercentage,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String promoCodeExpiration
    ) {
        Optional<Course> courseOpt = courseRepo.findById(id);
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
            throw new RuntimeException("Course not found with id " + id);
        }
    }

    @GetMapping("/{id}/getDetails")
    public Course getCourseFeesDetails(@PathVariable long id) {
        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            Course response = new Course();
            response.setCourseFee(course.getCourseFee());
            response.setDiscountPercentage(course.getDiscountPercentage());
            response.setCurrency(course.getCurrency());
            response.setPromoCodeExpiration(course.getPromoCodeExpiration());
            return response;
        } else {
            throw new RuntimeException("Course not found with CourseId " + id);
        }
    }

    @PutMapping("/updateNewCourse/{courseId}")
    public ResponseEntity<Course> updateNewCourse(
            @PathVariable long courseId,
            @RequestParam(required = false) String courseTitle,
            @RequestParam(required = false) String courseDescription,
            @RequestParam(required = false) String courseLanguage,
            @RequestParam(required = false) Course.Level level,
            @RequestParam(required = false) String level_1,
            @RequestParam(required = false) String level_2,
            @RequestParam(required = false) String level_3,
            @RequestParam(required = false) String level_4,
            @RequestParam(required = false) String level_5,
            @RequestParam(required = false) String level_6,
            @RequestParam(required = false) String level_7,
            @RequestParam(required = false) String level_8,
            @RequestParam(required = false) MultipartFile courseImage,
            @RequestParam(required = false) String courseDuration,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String courseTerm){
        try {
            Course updatedCourse = courseService.updateNewCourse(courseId, courseTitle, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage, courseDuration, courseType,courseTerm);
            return ResponseEntity.ok(updatedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getCourse/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourse(@PathVariable long courseId) {
        try {
            Course course = courseService.getNewCourseById(courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("courseId", course.getId());
            response.put("courseTitle", course.getCourseTitle());
            response.put("courseDescription", course.getCourseDescription());
            response.put("courseLanguage", course.getCourseLanguage());
            response.put("level", course.getLevel());
            response.put("level_1", course.getLevel_1());
            response.put("level_2", course.getLevel_2());
            response.put("level_3", course.getLevel_3());
            response.put("level_4", course.getLevel_4());
            response.put("level_5", course.getLevel_5());
            response.put("level_6", course.getLevel_6());
            response.put("level_7", course.getLevel_7());
            response.put("level_8", course.getLevel_8());
            response.put("courseImage", course.getCourseImage());
            response.put("courseDuration", course.getCourseDuration());
            response.put("courseType", course.getCourseType());
            response.put("courseTerm",course.getCourseTerm());

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/patchCourse/{id}")
    public ResponseEntity<Course> patchCourseById(
            @PathVariable long id,
            @RequestBody Map<String, Object> updates) {

        Course updatedCourse = courseService.patchCourseById(id, updates);

        if (updatedCourse != null) {
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourseById(@PathVariable Long id) {
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getByUser/{userId}")
    public List<Course> getCoursesByUserId(@PathVariable long userId) {
        return courseService.getByUserId(userId);
    }

    @PostMapping("/{id}/document")
    public ResponseEntity<String> uploadCourseDocument(@PathVariable long id, @RequestParam("document") MultipartFile document) {
        try {
            courseService.updateCourseDocument(id, document);
            return ResponseEntity.ok("Course document updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error updating course document.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Course not found.");
        }
    }

    @GetMapping("/courses/details-by-user")
    public List<Map<String, Object>> getCourseDetailsWithContentByUserId(@RequestParam long userId) {
        return courseService.getCourseDetailsWithContentByUserId(userId);
    }

    @GetMapping("/course/detail-by-user")
    public List<Map<String, Object>> getCourseDetailsWithContentAndProgressByUserId(@RequestParam long userId) {
        List<Course> courses = courseRepo.findCoursesByUserId(userId);
        List<Map<String, Object>> results = new ArrayList<>();

        for (Course course : courses) {
            Map<String, Object> courseDetails = new HashMap<>();
            courseDetails.put("courseId", course.getId()); // Include courseId
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

            double assignmentProgress = assignmentService.getProgressByModulesInCourses(course.getId(), userId);
            courseDetails.put("assignmentProgress", assignmentProgress);

            double quizProgress = quizService.getQuizProgressByCourseAndUser(course.getId(), userId);
            courseDetails.put("quizProgress", quizProgress);

            double projectProgress = projectService.calculateUserProgressForCourse(course, new User(userId));
            courseDetails.put("projectProgress", projectProgress);

            results.add(courseDetails);
        }

        return results;
    }



    @GetMapping("/online/byMentor/{userId}")
    public List<Course> getOnlineCoursesByMentor(@PathVariable Long userId) {
        return courseService.getOnlineCoursesByUser(userId, "online");
    }

    @PostMapping("/saveCourses/{userId}")
    public ResponseEntity<Course> addCourses(
            @PathVariable long userId,
            @RequestParam String courseDescription,
            @RequestParam String courseLanguage,
            @RequestParam Course.Level level,
            @RequestParam String level_1,
            @RequestParam String level_2,
            @RequestParam String level_3,
            @RequestParam String level_4,
            @RequestParam String level_5,
            @RequestParam String level_6,
            @RequestParam String level_7,
            @RequestParam String level_8,
            @RequestParam("courseImage") MultipartFile courseImage,
            @RequestParam String courseDuration,
            @RequestParam String courseType,
            @RequestParam String courseTerm) {
        try {
            Course savedCourse = courseService.addCourses(userId, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage,courseDuration, courseType,courseTerm);
            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @PostMapping("/saveCourseTitle")
//    public ResponseEntity<Course> createCourse(@RequestParam String courseTitle) {
//        Course course = new Course();
//        course.setCourseTitle(courseTitle);
//        courseRepo.save(course);
//        return new ResponseEntity<>(course, HttpStatus.CREATED);
//    }


    @PostMapping("/get/saveCourseTitle")
    public ResponseEntity<Course> createCourse(@RequestParam Long userId, @RequestParam String courseTitle) {
        Course course = courseService.createCourse(userId, courseTitle);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PostMapping("/saveNewCourseById/{courseId}")
    public ResponseEntity<Course> addNewCoursesById(
            @PathVariable long courseId,
            @RequestParam String courseDescription,
            @RequestParam String courseLanguage,
            @RequestParam Course.Level level,
            @RequestParam String level_1,
            @RequestParam String level_2,
            @RequestParam String level_3,
            @RequestParam String level_4,
            @RequestParam String level_5,
            @RequestParam String level_6,
            @RequestParam String level_7,
            @RequestParam String level_8,
            @RequestParam("courseImage") MultipartFile courseImage,
            @RequestParam String courseDuration,
            @RequestParam String courseType,
            @RequestParam String courseTerm) {
        try {
            Course savedCourse = courseService.addNewCourseById(courseId, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage, courseDuration, courseType, courseTerm);
            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/postDetails/{courseId}")
    public ResponseEntity<Course> postCourseDetails(
            @PathVariable long courseId,
            @RequestParam(required = false) Long courseFee,
            @RequestParam(required = false) Long discountPercentage,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String promoCodeExpiration) {
        try {
            Course updatedCourse = courseService.updateCourseDetails(courseId, courseFee, discountPercentage, currency, promoCodeExpiration);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/api/course/details")
    public List<Object[]> getCourseDetailsByTeacherIdAndCourseId(
            @RequestParam Long teacherId,
            @RequestParam Long courseId) {
        return courseService.getCourseDetailsByTeacherIdAndCourseId(teacherId, courseId);
    }

    @PostMapping("/saveNewCoursesById/{courseId}")
    public ResponseEntity<Course> addNewCourseById(
            @PathVariable long courseId,
            @RequestParam String courseDescription,
            @RequestParam String courseLanguage,
            @RequestParam Course.Level level,
            @RequestParam String level_1,
            @RequestParam String level_2,
            @RequestParam String level_3,
            @RequestParam String level_4,
            @RequestParam String level_5,
            @RequestParam String level_6,
            @RequestParam String level_7,
            @RequestParam String level_8,
            @RequestParam("courseImage") MultipartFile courseImage,
            @RequestParam String courseDuration,
            @RequestParam String courseType,
            @RequestParam String courseTerm,
            @RequestParam String coursePercentage) {
        try {
            Course savedCourse = courseService.addNewCoursesById(courseId, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage, courseDuration, courseType, courseTerm,coursePercentage);
            return ResponseEntity.ok(savedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/generatedPromoCode/{courseId}")
    public ResponseEntity<Course> generatedPromoCode(@PathVariable Long courseId,
                                                     @RequestParam(required = false) Long discountPercentage,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime promoCodeExpiration,
                                                     @RequestParam Long courseFee,
                                                     @RequestParam String currency) {
        Course generatedCourse = courseService.generatedPromoCode(courseId, discountPercentage, promoCodeExpiration, courseFee, currency);
        return ResponseEntity.ok(generatedCourse);
    }

    @PutMapping("/updateCourseById/{courseId}")
    public ResponseEntity<Course> updateCourseById(
            @PathVariable long courseId,
            @RequestParam(required = false) String courseDescription,
            @RequestParam(required = false) String courseLanguage,
            @RequestParam(required = false) Course.Level level,
            @RequestParam(required = false) String level_1,
            @RequestParam(required = false) String level_2,
            @RequestParam(required = false) String level_3,
            @RequestParam(required = false) String level_4,
            @RequestParam(required = false) String level_5,
            @RequestParam(required = false) String level_6,
            @RequestParam(required = false) String level_7,
            @RequestParam(required = false) String level_8,
            @RequestParam(value = "courseImage", required = false) MultipartFile courseImage,
            @RequestParam(required = false) String courseDuration,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String courseTerm,
            @RequestParam(required = false) String coursePercentage) {

        try {
            Course updatedCourse = courseService.updateCourseById(courseId, courseDescription, courseLanguage, level, level_1, level_2, level_3, level_4, level_5, level_6, level_7, level_8, courseImage, courseDuration, courseType, courseTerm, coursePercentage);
            return ResponseEntity.ok(updatedCourse);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/updatePromoCode/{courseId}")
    public ResponseEntity<Course> updatePromoCode(@PathVariable Long courseId,
                                                  @RequestParam(required = false) Long discountPercentage,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime promoCodeExpiration,
                                                  @RequestParam(required = false) Long courseFee,
                                                  @RequestParam(required = false) String currency) {
        Course updatedCourse = courseService.updatePromoCode(courseId, discountPercentage, promoCodeExpiration, courseFee, currency);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/overallUserGrade/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getCourseProgress(@PathVariable Long userId) {
        List<Map<String, Object>> progressList = courseService.getCourseProgress(userId);
        return ResponseEntity.ok(progressList);
    }


    @GetMapping("/course/detail-by-lesson")
    public Map<String, Object> getCourseDetailsWithContentAndProgressByLessonIdAndUserId(
            @RequestParam long lessonId) {

        Course course = courseRepo.findCourseByLessonId(lessonId);

        if (course == null) {
            throw new EntityNotFoundException("No course found for the provided lessonId and userId");
        }

        Map<String, Object> courseDetails = new HashMap<>();
//        courseDetails.put("courseId", course.getId());
//        courseDetails.put("courseTitle", course.getCourseTitle());
//        courseDetails.put("courseDescription", course.getCourseDescription());
        courseDetails.put("projectTitles", course.getProjects().stream()
                .map(Project::getProjectTitle)
                .collect(Collectors.toList()));
        courseDetails.put("assignmentTitles", course.getAssignments().stream()
                .map(Assignment::getAssignmentTitle)
                .collect(Collectors.toList()));
        courseDetails.put("quizNames", course.getQuizzes().stream()
                .map(Quiz::getQuizName)
                .collect(Collectors.toList()));

        double assignmentProgress = assignmentService.getProgressByModulesInCourses(course.getId(), lessonId);
        courseDetails.put("assignmentProgress", assignmentProgress);

        double quizProgress = quizService.getQuizProgressByCourseAndUser(course.getId(), lessonId);
        courseDetails.put("quizProgress", quizProgress);

        double projectProgress = projectService.calculateUserProgressForCourse(course, new User(lessonId));
        courseDetails.put("projectProgress", projectProgress);

        return courseDetails;
    }


    @GetMapping("/high-percentage")
    public List<Course> getCoursesWithHighPercentage() {
        return courseService.getCoursesWithPercentageAbove90();
    }

    @GetMapping("/{userId}/documents")
    public ResponseEntity<List<byte[]>> getEnrolledCourseDocuments(@PathVariable Long userId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documents.zip") // Optional for downloads
                .body(courseService.getEnrolledCourseDocuments(userId));
    }
    @GetMapping("/mentor/getStats/{userId}")
    public ResponseEntity<Map<String, Object>> getCoursesForUser(@PathVariable Long userId) {
        // Fetch courses, ensuring null safety
        List<Course> courses = courseRepo.findByUserId(userId);
        if (courses == null) {
            courses = new ArrayList<>();
        }

        // Extract course names (handling possible null course titles)
        List<String> courseNames = courses.stream()
                .map(course -> course.getCourseTitle() != null ? course.getCourseTitle() : "Unknown Course")
                .collect(Collectors.toList());

        // Compute the average cost safely
        double avg = courses.stream()
                .filter(course -> course.getCourseFee() != null) // Ensure no null values
                .mapToDouble(Course::getCourseFee)
                .average()
                .orElse(0.0); // Default value

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("courseNames", courseNames);
        response.put("averageCost", avg);
        response.put("totalCourses", courses.size());

        return ResponseEntity.ok(response);
    }

}
