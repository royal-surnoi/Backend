package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import fusionIQ.AI.V2.fusionIq.service.QuizProgressService;
import fusionIQ.AI.V2.fusionIq.service.QuizService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizProgressService quizProgressService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuizProgressRepo quizProgressRepo;

//    @Autowired
//    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @PostMapping("/add/{lessonId}")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz, @PathVariable Long lessonId) {
        try {
            Quiz savedQuiz = quizService.addQuiz(quiz, lessonId);
            return ResponseEntity.ok(savedQuiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizById(id);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping("/{quizId}/users/{userId}/questions/{questionId}/submit")
    public ResponseEntity<Answer> submitAnswer(@PathVariable Long quizId, @PathVariable Long userId, @PathVariable Long questionId, @RequestParam String selectedAnswer) {
        Answer answer = quizService.submitAnswer(quizId, userId, questionId, selectedAnswer);
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PostMapping("/{quizId}/users/{userId}/submitAnswers")
    public ResponseEntity<String> submitAnswers(@PathVariable Long quizId, @PathVariable Long userId, @RequestBody List<Answer> answers) {
        try {
            Optional<Quiz> quizOptional = quizService.findByIdWithCourse(quizId);
            if (!quizOptional.isPresent()) {
                return new ResponseEntity<>("Quiz with ID " + quizId + " not found", HttpStatus.NOT_FOUND);
            }

            User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

            if (quizProgressService.hasQuizProgress(quizId, userId)) {
                return new ResponseEntity<>("Quiz with ID " + quizId + " has already been submitted by user with ID " + userId, HttpStatus.CONFLICT);
            }

            List<Answer> submittedAnswers = quizService.submitAnswers(quizId, userId, answers);

            return new ResponseEntity<>("Answers submitted successfully for quiz with ID " + quizId + " by user with ID " + userId, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to submit answers for quiz with ID " + quizId + " by user with ID " + userId + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{quizId}/users/{userId}/percentage")
    public ResponseEntity<Double> getCorrectAnswerPercentage(@PathVariable Long quizId, @PathVariable Long userId) {
        double percentage = quizService.calculateCorrectAnswerPercentage(quizId, userId);
        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }

    @GetMapping("/{quizId}/users/{userId}/ratio")
    public ResponseEntity<String> getCorrectAnswerRatio(@PathVariable Long quizId, @PathVariable Long userId) {
        String ratio = quizService.calculateCorrectAnswerRatio(quizId, userId);
        return new ResponseEntity<>(ratio, HttpStatus.OK);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> addQuestionsToQuiz(@PathVariable Long quizId, @RequestBody List<Question> questions) {
        try {
            List<Question> addedQuestions = quizService.addQuestionsToQuiz(quizId, questions);
            return ResponseEntity.ok(addedQuestions);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsToQuiz(@PathVariable Long quizId) {
        List<Question> questions = quizService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }


    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourseId(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.getQuizzesByCourseId(courseId);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }


    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Quiz>> getQuizzesByLessonId(@PathVariable Long lessonId) {
        List<Quiz> quizzes = quizService.getQuizzesByLessonId(lessonId);
        return ResponseEntity.ok(quizzes);
    }

    @PatchMapping("/updateQuizByLesson/{lessonId}")
    public ResponseEntity<Quiz> patchQuizByLessonId(
            @PathVariable long lessonId,
            @RequestBody Map<String, Object> updates) {

        Quiz updatedQuiz = quizService.patchQuizByLessonId(lessonId, updates);

        if (updatedQuiz != null) {
            return ResponseEntity.ok(updatedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/saveQuizByLessonId")
    public ResponseEntity<Quiz> createNewQuiz(
            @RequestParam("quizName") String quizName,
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        Quiz createdQuiz = quizService.saveByQuizLessonId(quizName, lessonId, startDate, endDate);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/due-in-five-days")
    public List<Quiz> getQuizDueInFiveDays() {
        return quizService.getQuizDueInFiveDays();
    }

    @PostMapping("/saveQuizByLessonModuleId")
    public ResponseEntity<Quiz> addNewQuiz(
            @RequestParam("quizName") String quizName,
            @RequestParam("lessonModuleId") Long lessonModuleId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        Quiz createdQuiz = quizService.saveByQuizByLessonModuleId(quizName, lessonModuleId, startDate, endDate);
        return ResponseEntity.ok(createdQuiz);
    }

    @PostMapping("/progress/{quizId}")
    public ResponseEntity<String> markQuizAsCompleted(@PathVariable Long quizId, @RequestParam Long userId) {
        try {
            Optional<Quiz> quizOptional = quizService.findByIdWithCourse(quizId);
            if (!quizOptional.isPresent()) {
                return new ResponseEntity<>("Quiz with ID " + quizId + " not found", HttpStatus.NOT_FOUND);
            }

            if (quizProgressService.hasQuizProgress(quizId, userId)) {
                return new ResponseEntity<>("Quiz with ID " + quizId + " has already been marked as completed by user with ID " + userId, HttpStatus.CONFLICT);
            }

            Quiz quiz = quizOptional.get();
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            QuizProgress quizProgress = new QuizProgress();
            quizProgress.setUser(user);
            quizProgress.setQuiz(quiz);
            quizProgress.setCourse(quiz.getCourse());
            quizProgress.setCompleted(true);
            quizProgressService.trackQuizProgress(quizProgress);

            return new ResponseEntity<>("Quiz with ID " + quizId + " successfully marked as completed by user with ID " + userId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to mark quiz with ID " + quizId + " as completed by user with ID " + userId + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<List<QuizProgress>> getUserLessonProgress(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            List<QuizProgress> progress = quizProgressService.getUserLessonProgress(userId, lessonId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/percentage/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<Double> getUserLessonProgressPercentage(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            double progressPercentage = quizProgressService.calculateProgressPercentageForLesson(userId, lessonId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/ratio/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<String> getCompletedQuizByLessonRatio(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            String ratio = quizProgressService.calculateCompletedQuizRatioForLesson(userId, lessonId);
            return ResponseEntity.ok(ratio);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/ratio/course/{courseId}")
    public ResponseEntity<String> getCompletedQuizByCourseRatio(@PathVariable Long courseId) {
        try {
            String ratio = quizProgressService.calculateCompletedQuizRatioForCourse(courseId);
            return ResponseEntity.ok(ratio);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/user/{userId}/lessonModule/{lessonModuleId}")
    public ResponseEntity<List<QuizProgress>> getUserLessonModuleProgress(@PathVariable Long userId, @PathVariable Long lessonModuleId) {
        try {
            List<QuizProgress> progress = quizProgressService.getUserLessonModuleProgress(userId, lessonModuleId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/percentage/user/{userId}/lessonModule/{lessonModuleId}")
    public ResponseEntity<Double> getUserLessonModuleProgressPercentage(@PathVariable Long userId, @PathVariable Long lessonModuleId) {
        try {
            double progressPercentage = quizProgressService.calculateProgressPercentage(userId, lessonModuleId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/ratio/user/{userId}/lessonModule/{lessonModuleId}")
    public ResponseEntity<String> getCompletedQuizByLessonModuleRatio(@PathVariable Long userId, @PathVariable Long lessonModuleId) {
        try {
            String ratio = quizProgressService.calculateCompletedQuizRatio(userId, lessonModuleId);
            return ResponseEntity.ok(ratio);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/course/{courseId}/user/{userId}")
    public String getQuizProgress(@PathVariable Long courseId, @PathVariable Long userId) {
        return quizProgressService.getQuizProgressByCourseAndUser(courseId, userId);
    }

    @GetMapping("/{quizId}/submittedUsers")
    public ResponseEntity<List<User>> getUsersWhoSubmittedQuiz(@PathVariable Long quizId) {
        try {
            List<User> users = quizProgressService.getUsersWhoSubmittedQuiz(quizId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createByTeacher/{teacherId}/{studentId}")
    public ResponseEntity<Quiz> createQuizByTeacher(
            @PathVariable long teacherId,
            @PathVariable long studentId,
            @RequestParam String quizName,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false) Long lessonModuleId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        try {
            Quiz createdQuiz = quizService.createQuizByTeacher(teacherId, studentId, quizName, courseId, lessonId, lessonModuleId, startDate, endDate);
            return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{quizId}/users/{userId}/submittedAnswers")
    public ResponseEntity<List<Answer>> getSubmittedAnswers(@PathVariable Long quizId, @PathVariable Long userId) {
        try {
            List<Answer> submittedAnswers = quizService.getSubmittedAnswers(quizId, userId);
            return new ResponseEntity<>(submittedAnswers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/submitted-quiz-ratio")
    public String getSubmittedQuizRatio(@RequestParam Long courseId, @RequestParam Long userId) {
        return quizService.getSubmittedQuizRatio(courseId, userId);
    }

    @GetMapping("/users/{userId}/courses/{courseId}/quizzes")
    public List<Quiz> getQuizzesByCourseIdAndUserId(@PathVariable Long userId, @PathVariable Long courseId) {
        return quizService.getQuizzesByCourseIdAndUserId(courseId, userId);
    }

    @GetMapping("/progress/user/{userId}/course/{courseId}")
    public ResponseEntity<List<QuizProgress>> getQuizProgressByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        try {
            List<QuizProgress> progress = quizService.getQuizProgressByUserAndCourse(userId, courseId);
            return ResponseEntity.ok(progress);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/progress/percentage/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getQuizProgressPercentageByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        try {
            double progressPercentage = quizService.getQuizProgressPercentageByUserAndCourse(userId, courseId);
            return ResponseEntity.ok(progressPercentage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/progress/ratio/user/{userId}/course/{courseId}")
    public ResponseEntity<String> getQuizProgressRatioByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

            List<Quiz> courseQuizzes = quizRepo.findByCourse(course);
            long completedQuizzes = quizProgressRepo.countByUserAndCourseAndCompleted(user, course, true);

            String ratio = completedQuizzes + "/" + courseQuizzes.size();
            return ResponseEntity.ok(ratio);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createIndividualQuiz")
    public Quiz createIndividualQuiz(@RequestParam Long teacherId, @RequestParam Long studentId,
                                     @RequestParam Long courseId, @RequestParam String quizName) {

        return quizService.createIndividualQuiz(teacherId, studentId, courseId, quizName);
    }

    @PostMapping("/{quizId}/students/{studentId}/submitAnswers")
    public ResponseEntity<List<Answer>> submitAnswersIndividual(
            @PathVariable Long quizId,
            @PathVariable Long studentId,
            @RequestBody List<Answer> answers) {
        try {
            List<Answer> submittedAnswers = quizService.submitAnswersIndividual(quizId, studentId, answers);
            return new ResponseEntity<>(submittedAnswers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teacherToStudentProgress")
    public ResponseEntity<Map<String, Object>> getTeacherToStudentProgress(@RequestParam Long studentId,
                                                                           @RequestParam Long courseId) {
        Map<String, Object> progress = quizService.calculateTeacherToStudentProgress(studentId, courseId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }



    @PostMapping("/saveQuizByLessonIdAndLessonModuleId")
    public ResponseEntity<Quiz> createNewQuiz(
            @RequestParam("quizName") String quizName,
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("lessonModuleId") Long lessonModuleId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        Quiz createdQuiz = quizService.saveByQuizLessonAndLessonModuleId(quizName, lessonId, lessonModuleId, startDate, endDate);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/byLessonAndModule")
    public List<Quiz> getQuizzesByLessonAndModule(@RequestParam Long lessonId, @RequestParam Long lessonModuleId) {
        return quizService.getQuizzesByLessonAndModule(lessonId, lessonModuleId);

    }

    @GetMapping("/progress/details/{lessonId}")
    public ResponseEntity<Map<String, Object>> getQuizProgressDetails(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {
        Map<String, Object> progressDetails = quizService.getQuizProgressDetails(lessonId, userId);
        return ResponseEntity.ok(progressDetails);
    }

    @PostMapping("/course/{courseId}/teacher/{teacherId}")
    public ResponseEntity<Quiz> createQuizByCourseId(@PathVariable Long courseId,
                                                     @PathVariable Long teacherId,
                                                     @RequestBody Quiz quiz) {
        Quiz createdQuiz = quizService.createQuizByCourseId(courseId, teacherId, quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/course/{courseId}/teacher/{teacherId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourseAndTeacher(@PathVariable Long courseId,
                                                                   @PathVariable Long teacherId) {
        List<Quiz> quizzes = quizService.getQuizzesByCourseAndTeacher(courseId, teacherId);
        return ResponseEntity.ok(quizzes);
    }


    @PostMapping("/createQuiz")
    public ResponseEntity<Quiz> createQuiz(
            @RequestParam long courseId,
            @RequestParam long teacherId,
            @RequestParam String studentIds, // Accept as String
            @RequestParam String quizName,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false) Long lessonModuleId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        // Convert the comma-separated student IDs string to List<Long>
        List<Long> studentIdList = Arrays.stream(studentIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // Call the service method
        Quiz quiz = quizService.createQuiz(
                courseId, teacherId, studentIdList, quizName, lessonId, lessonModuleId, startDate, endDate);

        return ResponseEntity.ok(quiz);
    }


    @PostMapping("/courses/{courseId}/teachers/{teacherId}/quizzes/{quizId}/addQuestion")
    public ResponseEntity<Question> addQuestionToQuiz(
            @PathVariable long courseId,
            @PathVariable long teacherId,
            @PathVariable long quizId,
            @RequestBody Question question) {

        // Call the service method to add a question
        Question createdQuestion = quizService.addQuestionToQuiz(courseId, teacherId, quizId, question);

        return ResponseEntity.ok(createdQuestion);
    }


    @GetMapping("/{teacherId}/course-quiz-details")
    public ResponseEntity<List<Map<String, Object>>> getCourseQuizDetailsByTeacher(@PathVariable Long teacherId) {
        List<Map<String, Object>> courseQuizDetails = userService.getQuizDetailsByTeacherId(teacherId);

        if (courseQuizDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonList(Collections.singletonMap("message", "No course or quiz details found for the provided teacher ID.")));
        }

        return ResponseEntity.ok(courseQuizDetails);
    }



    @GetMapping("/teacher/{teacherId}/student/{studentId}/statistics")
    public Map<String, Object> getQuizDetailsAndStatisticsByTeacherIdAndStudentId(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        return quizService.getQuizDetailsAndStatisticsByTeacherIdAndStudentId(teacherId, studentId);
    }



    @DeleteMapping("/{quizId}")
    public void deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuizById(quizId);
    }


    @PutMapping("/updateMultipleQuiz/{quizId}")
    public ResponseEntity<String> updateQuizAndQuestions(@PathVariable Long quizId,
                                                         @RequestParam(required = false) String quizName,
                                                         @RequestParam(required = false) String text,
                                                         @RequestParam(required = false) String optionA,
                                                         @RequestParam(required = false) String optionB,
                                                         @RequestParam(required = false) String optionC,
                                                         @RequestParam(required = false) String optionD,
                                                         @RequestParam(required = false) String correctAnswer,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        quizService.updateQuizAndQuestions(quizId, quizName, text, optionA, optionB, optionC, optionD, correctAnswer, startDate, endDate);
        return ResponseEntity.ok("Quiz and associated questions updated successfully");
    }


    @GetMapping("/getQuizDetails/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizDetails(@PathVariable Long quizId) {
        Map<String, Object> quizDetails = quizService.getQuizDetails(quizId);
        if (quizDetails != null) {
            return ResponseEntity.ok(quizDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getQuizDetailsByTeacherId(
            @PathVariable Long teacherId) {
        List<Map<String, Object>> quizzes = quizService.getQuizDetailsByTeacherId(teacherId);

        if (quizzes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(quizzes);
    }

//    @PostMapping("/assign-quiz")
//    public ResponseEntity<Quiz> assignQuiz(
//            @RequestParam Long[] shortlistedCandidateIds,
//            @RequestParam String quizName,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//
//        Quiz quiz = quizService.assignQuizToShortlistedCandidates(shortlistedCandidateIds, quizName, startDate, endDate);
//        return ResponseEntity.ok(quiz);
//    }


}
