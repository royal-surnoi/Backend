package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.SystemColor.text;

@Service
public class QuizService {
    @Autowired
    private QuizRepo quizRepo;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private LessonModuleRepo lessonModuleRepo;

    @Autowired
    private NotificationRepo notificationRepo;

//    @Autowired
//    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    @Autowired
    private QuizProgressRepo quizProgressRepo;

    public List<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
    }

    public Optional<Quiz> getQuizsById(Long id) {
        return quizRepo.findById(id);
    }

    public Answer submitAnswer(Long quizId, Long userId, Long questionId, String selectedAnswer) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Question question = questionRepo.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setQuiz(quiz);
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setSelectedAnswer(selectedAnswer);
        answer.setCorrect(question.getCorrectAnswer().equals(selectedAnswer));

        return answerRepo.save(answer);
    }

    public double calculateCorrectAnswerPercentage(Long quizId, Long userId) {
        List<Answer> answers = answerRepo.findByQuizIdAndUserId(quizId, userId);
        long correctCount = answers.stream().filter(Answer::isCorrect).count();
        long totalCount = answers.size();
        return (double) correctCount / totalCount * 100;
    }

    public String calculateCorrectAnswerRatio(Long quizId, Long userId) {
        List<Answer> answers = answerRepo.findByQuizIdAndUserId(quizId, userId);
        long correctCount = answers.stream().filter(Answer::isCorrect).count();
        long totalCount = answers.size();
        return correctCount + "/" + totalCount;
    }

    public Quiz addQuiz(Quiz quiz, Long lessonId) {

        Optional<Lesson> lessonOptional = lessonRepo.findById(lessonId);
        if (lessonOptional.isPresent()) {
            quiz.setLesson(lessonOptional.get());
        } else {
            throw new IllegalArgumentException("Activity, User, or Project not found");
        }
        return quizRepo.save(quiz);
    }


    public List<Long> getQuestionsToQuiz(Long quizId, List<Question> questions) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        List<Long> addedQuestionIds = new ArrayList<>();
        for (Question question : questions) {
            question.setQuiz(quiz);
            Question savedQuestion = questionRepo.save(question);
            addedQuestionIds.add(savedQuestion.getId());
        }
        return addedQuestionIds;
    }

    public List<Question> addQuestionsToQuiz(Long quizId, List<Question> questions) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
        List<Question> addedQuestions = new ArrayList<>();

        for (Question question : questions) {
            question.setQuiz(quiz);
            Question savedQuestion = questionRepo.save(question);
            addedQuestions.add(savedQuestion);
        }

        return addedQuestions;
    }

    public List<Quiz> getQuizzesByLessonId(Long lessonId) {
        return quizRepo.findByLessonId(lessonId);
    }

    public Quiz patchQuizByLessonId(long lessonId, Map<String, Object> updates) {
        Optional<Lesson> optionalLesson = lessonRepo.findById(lessonId);
        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            Optional<Quiz> optionalQuiz = lesson.getQuizzes().stream()
                    .findFirst();
            if (optionalQuiz.isPresent()) {
                Quiz quiz = optionalQuiz.get();
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "quizName":
                            quiz.setQuizName((String) value);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid field: " + key);
                    }
                });
                quiz.setUpdatedAt(LocalDateTime.now());
                return quizRepo.save(quiz);
            }
        }
        return null;
    }

    public Quiz saveByQuizLessonId(String quizName, Long lessonId, LocalDateTime startDate, LocalDateTime endDate) {
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);

        quiz.setLesson(lessonRepo.findById(lessonId).orElse(null));
        Quiz savedQuiz = quizRepo.save(quiz);

        createNotificationForLesson(savedQuiz);

        return savedQuiz;
    }

    public Quiz saveByQuizByLessonModuleId(String quizName, Long lessonModuleId, LocalDateTime startDate, LocalDateTime endDate) {
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);

        quiz.setLessonModule(lessonModuleRepo.findById(lessonModuleId).orElse(null));
        Quiz savedQuiz = quizRepo.save(quiz);

        createNotificationForLessonModule(savedQuiz);

        return savedQuiz;
    }


    private void createNotificationForLesson(Quiz quiz) {
        LocalDateTime notificationDate = quiz.getEndDate().minusDays(2);
        String content = "Reminder: The Quiz '" + quiz.getQuizName() + "' is due on " + quiz.getEndDate() + ".";
        Notification notification = new Notification();
        notification.setUser(quiz.getLesson().getUser()); // Assuming Course has a User
        notification.setContent(content);
        notification.setRead(notification.isRead());
        notification.setTimestamp(notificationDate);
        notificationRepo.save(notification);
    }

    public List<Quiz> getQuizDueInFiveDays() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveDaysLater = now.plusDays(5);
        return quizRepo.findAll().stream()
                .filter(assignment -> assignment.getEndDate() != null && assignment.getEndDate().isAfter(now) && assignment.getEndDate().isBefore(fiveDaysLater))
                .collect(Collectors.toList());
    }

    private void createNotificationForLessonModule(Quiz quiz) {
        LocalDateTime notificationDate = quiz.getEndDate().minusDays(2);
        String content = "Reminder: The Quiz '" + quiz.getQuizName() + "' is due on " + quiz.getEndDate() + ".";
        Notification notification = new Notification();
        notification.setUser(quiz.getLessonModule().getUser()); // Assuming Course has a User
        notification.setContent(content);
        notification.setRead(notification.isRead());
        notification.setTimestamp(notificationDate);
        notificationRepo.save(notification);
    }

    public Optional<Quiz> findById(Long quizId) {
        return quizRepo.findById(quizId);
    }

    public List<Quiz> getUpcomingQuizzesForUser(Long userId) {
        return quizRepo.findUpcomingQuizzesByUser(userId);
    }

    public boolean hasUserSubmittedAnswers(Long quizId, Long userId) {
        return answerRepo.existsByQuizIdAndUserId(quizId, userId);
    }

    public List<Answer> submitAnswers(Long quizId, Long userId, List<Answer> answers) {
        Optional<Quiz> quizOptional = quizRepo.findById(quizId);
        if (!quizOptional.isPresent()) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        Quiz quiz = quizOptional.get();
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        for (Answer answer : answers) {
            answer.setQuiz(quiz);
            answer.setUser(user);

            Question question = questionRepo.findById(answer.getQuestion().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

            answer.setQuestion(question);
            answer.setCorrect(checkAnswerCorrectness(answer));
        }

        List<Answer> savedAnswers = answerRepo.saveAll(answers);

        createQuizCompletionNotification(quiz, user);
        createPendingNotification(quiz);

        return savedAnswers;
    }


    private void createQuizCompletionNotification(Quiz quiz, User user) {
        String content = "Quiz '" + quiz.getQuizName() + "' has been completed.";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(content);
        notification.setRead(notification.isRead());
        notification.setTimestamp(LocalDateTime.now());

        notificationRepo.save(notification);
    }

    private void createPendingNotification(Quiz quiz) {
        LocalDateTime endDate = quiz.getEndDate();
        if (endDate != null && endDate.isBefore(LocalDateTime.now()) && !isQuizSubmitted(quiz.getId())) {
            String content = "Quiz '" + quiz.getQuizName() + "' is pending and was not submitted on time.";

            Notification notification = new Notification();
            notification.setUser(quiz.getCourse().getUser());
            notification.setContent(content);
            notification.setRead(notification.isRead());
            notification.setTimestamp(LocalDateTime.now());

            notificationRepo.save(notification);
        }
    }

    private boolean isQuizSubmitted(Long quizId) {
        return answerRepo.existsByQuizId(quizId);
    }

    private boolean checkAnswerCorrectness(Answer answer) {
        Question question = answer.getQuestion();
        String correctAnswer = question.getCorrectAnswer();
        String selectedAnswer = answer.getSelectedAnswer();
        return correctAnswer != null && correctAnswer.equals(selectedAnswer);

    }

    public List<Quiz> getQuizzesByCourseId(Long courseId) {
        return quizRepo.findByCourseIdWithLesson(courseId);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }

    public Quiz createQuizByTeacher(long teacherId, long studentId, String quizName, String courseId, Long lessonId, Long lessonModuleId, LocalDateTime startDate, LocalDateTime endDate) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        Quiz quiz = new Quiz();
        quiz.setTeacher(teacher);
        quiz.setStudent(student);
        quiz.setQuizName(quizName);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);

        if (courseId != null) {
            Course course = courseRepo.findById(Long.parseLong(courseId)).orElse(null);
            quiz.setCourse(course);
        }
        if (lessonId != null) {
            Lesson lesson = lessonRepo.findById(lessonId).orElse(null);
            quiz.setLesson(lesson);
        }
        if (lessonModuleId != null) {
            LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId).orElse(null);
            quiz.setLessonModule(lessonModule);
        }

        return quizRepo.save(quiz);
    }

    public Optional<Quiz> findByIdWithCourse(Long id) {
        return quizRepo.findByIdWithCourse(id);
    }

    public List<Answer> getSubmittedAnswers(Long quizId, Long userId) {
        return answerRepo.findByQuizIdAndUserId(quizId, userId);
    }

    public String getSubmittedQuizRatio(Long courseId, Long userId) {
        int totalQuizzes = quizRepo.countByCourseId(courseId);
        int submittedQuizzes = answerRepo.countDistinctByUserIdAndCourseId(userId, courseId);

        return submittedQuizzes + "/" + totalQuizzes;
    }

    public List<Quiz> getQuizzesByCourseIdAndUserId(Long courseId, Long userId) {
        return quizRepo.findQuizzesByCourseIdAndUserId(courseId, userId);
    }

    public List<QuizProgress> getQuizProgressByUserAndCourse(Long userId, Long courseId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return quizProgressRepo.findByUserAndCourse(user, course);
    }

    public double getQuizProgressPercentageByUserAndCourse(Long userId, Long courseId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        List<Quiz> courseQuizzes = quizRepo.findByCourse(course);
        if (courseQuizzes.isEmpty()) {
            return 0.0;
        }

        long completedQuizzes = quizProgressRepo.countByUserAndCourseAndCompleted(user, course, true);

        return (double) completedQuizzes / courseQuizzes.size() * 100;
    }

    public Quiz createIndividualQuiz(Long teacherId, Long studentId, Long courseId, String quizName) {

        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setTeacher(teacher);
        quiz.setStudent(student);
        quiz.setCourse(course);
        quiz.setStartDate(LocalDateTime.now());
        quiz.setEndDate(LocalDateTime.now().plusHours(1));
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepo.save(quiz);
    }

    public List<Answer> submitAnswersIndividual(Long quizId, Long studentId, List<Answer> answers) {
        Optional<Quiz> quizOptional = quizRepo.findById(quizId);
        if (!quizOptional.isPresent()) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        Quiz quiz = quizOptional.get();
        User student = userRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        for (Answer answer : answers) {
            answer.setQuiz(quiz);
            answer.setStudent(student); // Set the student as the user who submitted the answer

            Question question = questionRepo.findById(answer.getQuestion().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

            answer.setQuestion(question);
            answer.setCorrect(checkAnswerCorrectness(answer));
        }

        List<Answer> savedAnswers = answerRepo.saveAll(answers);

        createQuizCompletionNotification(quiz, student);
        createPendingNotification(quiz);

        return savedAnswers;
    }

    public Map<String, Object> calculateTeacherToStudentProgress(Long studentId, Long courseId) {
        List<Quiz> totalQuizzes = quizRepo.findByStudent_IdAndCourse_Id(studentId, courseId);

        List<QuizProgress> completedQuizzes = quizProgressRepo.findByUser_IdAndCourse_IdAndCompleted(studentId, courseId, true);

        int totalQuizzesCount = totalQuizzes.size();
        int completedQuizzesCount = completedQuizzes.size();

        double progressPercentage = totalQuizzesCount == 0 ? 0 : ((double) completedQuizzesCount / totalQuizzesCount) * 100;

        String courseTitle = courseRepo.findById(courseId)
                .map(Course::getCourseTitle)
                .orElse("Unknown Course");

        List<String> quizTitles = totalQuizzes.stream()
                .map(Quiz::getQuizName)
                .collect(Collectors.toList());

        Long teacherId = null;
        String teacherName = "Unknown";
        if (!totalQuizzes.isEmpty() && totalQuizzes.get(0).getTeacher() != null) {
            User teacher = totalQuizzes.get(0).getTeacher();
            teacherId = teacher.getId();
            teacherName = teacher.getName();
        }

        Map<String, Object> progressData = new HashMap<>();
        progressData.put("totalQuizzes", totalQuizzesCount);
        progressData.put("completedQuizzes", completedQuizzesCount);
        progressData.put("progressPercentage", progressPercentage);
        progressData.put("courseTitle", courseTitle);
        progressData.put("quizTitles", quizTitles);
        progressData.put("teacherId", teacherId);
        progressData.put("teacherName", teacherName);

        return progressData;
    }


    public double getQuizProgressByCourseAndUser(Long courseId, Long userId) {
        List<QuizProgress> progressList = quizProgressRepo.findByCourseIdAndUserId(courseId, userId);

        long totalQuizzes = progressList.size();
        long completedQuizzes = progressList.stream().filter(QuizProgress::isCompleted).count();

        if (totalQuizzes == 0) {
            return 0.0;
        }

        return (double) completedQuizzes / totalQuizzes * 100;
    }


    public Quiz saveByQuizLessonAndLessonModuleId(String quizName, Long lessonId, Long lessonModuleId, LocalDateTime startDate, LocalDateTime endDate) {
        // Find the Lesson and LessonModule by their IDs
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson ID"));
        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson module ID"));

        // Create a new Quiz object
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setLesson(lesson);
        quiz.setLessonModule(lessonModule);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepo.save(quiz);
    }

    public List<Quiz> getQuizzesByLessonAndModule(Long lessonId, Long lessonModuleId) {
        return quizRepo.findByLessonIdAndLessonModuleId(lessonId, lessonModuleId);
    }


    public Map<String, Object> getQuizProgressDetails(Long lessonId, Long userId) {
        Map<String, Object> progressDetails = new HashMap<>();

        // Fetch lesson details
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Determine courseTitle based on whether it's directly mapped to a Course or via a LessonModule
        final String courseTitle;
        if (lesson.getCourse() != null) {
            courseTitle = lesson.getCourse().getCourseTitle();
        } else if (lesson.getLessonModule() != null && lesson.getLessonModule().getCourse() != null) {
            courseTitle = lesson.getLessonModule().getCourse().getCourseTitle();
        } else {
            throw new ResourceNotFoundException("Course not associated with the lesson or its module");
        }

        // Fetch quizzes for the lesson
        List<Quiz> totalQuizzes = quizRepo.findByLessonId(lessonId);

        // Fetch quiz progress for the user, using the correct repository query
        List<QuizProgress> quizProgressList = quizProgressRepo.findByUserIdAndQuiz_Lesson_Id(userId, lessonId);

        // Calculate counts
        int totalQuizzesCount = totalQuizzes.size();
        int submittedQuizzesCount = (int) quizProgressList.stream().filter(QuizProgress::isCompleted).count();
        int unsubmittedQuizzesCount = totalQuizzesCount - submittedQuizzesCount;

        // Collect quiz titles and statuses
        List<Map<String, String>> quizzesWithStatus = totalQuizzes.stream().map(quiz -> {
            Map<String, String> details = new HashMap<>();
            details.put("quizTitle", quiz.getQuizName());
            details.put("quizId", quiz.getId().toString());
            details.put("courseTitle", courseTitle);
            details.put("lessonTitle", lesson.getLessonTitle());

            // Check if the quiz is completed based on the QuizProgress table
            boolean isCompleted = quizProgressList.stream()
                    .anyMatch(progress -> progress.getQuiz().getId().equals(quiz.getId()) && progress.isCompleted());

            details.put("status", isCompleted ? "Completed" : "Incomplete");

            return details;
        }).collect(Collectors.toList());

        // Calculate progress percentage
        double progressPercentage = 0.0;
        if (totalQuizzesCount > 0) {
            progressPercentage = (double) submittedQuizzesCount / totalQuizzesCount * 100;
        }

        // Set data in response map
        progressDetails.put("courseTitle", courseTitle);
        progressDetails.put("lessonTitle", lesson.getLessonTitle());
        progressDetails.put("totalQuizzesCount", totalQuizzesCount);
        progressDetails.put("submittedQuizzesCount", submittedQuizzesCount);
        progressDetails.put("unsubmittedQuizzesCount", unsubmittedQuizzesCount);
        progressDetails.put("progressPercentage", progressPercentage);
        progressDetails.put("quizzes", quizzesWithStatus);

        return progressDetails;
    }

    public void deleteQuestionById(Long questionId) {
        questionRepo.deleteById(questionId);
    }

    public Quiz createQuizByCourseId(Long courseId, Long teacherId, Quiz quiz) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));

        quiz.setCourse(course);
        quiz.setTeacher(teacher);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepo.save(quiz);
    }

    public List<Quiz> getQuizzesByCourseAndTeacher(Long courseId, Long teacherId) {
        return quizRepo.findByCourseIdAndTeacherId(courseId, teacherId);
    }

    public Quiz createQuiz(long courseId, long teacherId, List<Long> studentIdList,
                           String quizName, Long lessonId, Long lessonModuleId,
                           LocalDateTime startDate, LocalDateTime endDate) {

        // Fetch the course and teacher from repositories
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Fetch students based on the provided IDs
        List<User> students = userRepo.findAllById(studentIdList);

        // Create a new Quiz instance
        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTeacher(teacher);
        quiz.setQuizName(quizName);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);

        // Set Lesson and LessonModule if provided
        if (lessonId != null) {
            Lesson lesson = lessonRepo.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            quiz.setLesson(lesson);
        }
        if (lessonModuleId != null) {
            LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                    .orElseThrow(() -> new RuntimeException("Lesson Module not found"));
            quiz.setLessonModule(lessonModule);
        }

        // Convert student ID list to a comma-separated string and set it to the quiz
        String studentIdsString = studentIdList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        quiz.setStudentIds(studentIdsString);

        // Save the Quiz entity
        return quizRepo.save(quiz);
    }

    public Question addQuestionToQuiz(long courseId, long teacherId, long quizId, Question question) {

        // Fetch the course, teacher, and quiz from repositories
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + teacherId));
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + quizId));

        // Validate that the quiz belongs to the course and teacher
        if (!quiz.getCourse().equals(course)) {
            throw new ResourceNotFoundException("Quiz does not belong to the specified course");
        }
        if (!quiz.getTeacher().equals(teacher)) {
            throw new ResourceNotFoundException("Quiz was not created by the specified teacher");
        }

        // Set the quiz reference in the question entity
        question.setQuiz(quiz);

        // Save the Question entity
        return questionRepo.save(question);
    }

    public Map<String, Object> getCourseAndQuizDetailsWithStudentsAndQuestions(Long teacherId) {
        // Fetch quizzes by teacherId from the repository
        List<Quiz> quizzes = quizRepo.findByTeacherId(teacherId);

        // Initialize the map to hold the entire course summary
        Map<String, Object> courseSummary = new HashMap<>();

        if (quizzes.isEmpty()) {
            // Return an empty map or handle as per your logic if no quizzes are found
            return Collections.emptyMap();
        }

        // Get the course associated with the quizzes (assuming all quizzes belong to the same course)
        Course course = quizzes.get(0).getCourse();

        // Calculate total enrolled students across all quizzes
        Set<Long> totalEnrolledStudentIds = new HashSet<>();
        quizzes.forEach(quiz -> {
            if (quiz.getStudentIds() != null) {
                Arrays.stream(quiz.getStudentIds().split(","))
                        .map(Long::valueOf)
                        .forEach(totalEnrolledStudentIds::add);
            }
        });
        int totalEnrolledStudentsCount = totalEnrolledStudentIds.size();

        // Calculate enrolled students count for this particular course
        Set<Long> courseEnrolledStudentIds = new HashSet<>();
        quizzes.forEach(quiz -> {
            if (quiz.getStudentIds() != null) {
                Arrays.stream(quiz.getStudentIds().split(","))
                        .map(Long::valueOf)
                        .forEach(courseEnrolledStudentIds::add);
            }
        });
        int courseEnrolledStudentsCount = courseEnrolledStudentIds.size();

        // Handle quizzes and their respective details
        List<Map<String, Object>> quizDetails = quizzes.stream().map(quiz -> {
            // Handle student IDs and names for each quiz
            List<Map<String, Object>> studentDetails = new ArrayList<>();
            if (quiz.getStudentIds() != null && !quiz.getStudentIds().isEmpty()) {
                String[] studentIdsArray = quiz.getStudentIds().split(",");

                // Fetch the name of each student based on their ID
                for (String studentIdStr : studentIdsArray) {
                    Long studentId = Long.valueOf(studentIdStr);

                    // Fetch the student from the User repository
                    Optional<User> studentOpt = userRepo.findById(studentId);
                    if (studentOpt.isPresent()) {
                        User student = studentOpt.get();

                        // Create a map for student details
                        Map<String, Object> studentInfo = new HashMap<>();
                        studentInfo.put("studentId", student.getId());
                        studentInfo.put("studentName", student.getName());

                        // Add the map to the list
                        studentDetails.add(studentInfo);
                    } else {
                        // Handle case where a student ID does not correspond to a valid user
                        Map<String, Object> unknownStudentInfo = new HashMap<>();
                        unknownStudentInfo.put("studentId", studentId);
                        unknownStudentInfo.put("studentName", "Unknown");
                        studentDetails.add(unknownStudentInfo);
                    }
                }
            }

            // Handle questions and options for each quiz
            List<Map<String, Object>> questionDetails = new ArrayList<>();
            List<Question> questions = questionRepo.findByQuizId(quiz.getId());
            for (Question question : questions) {
                Map<String, Object> questionInfo = new HashMap<>();
                questionInfo.put("questionId", question.getId());
                questionInfo.put("text", question.getText());
                questionInfo.put("optionA", question.getOptionA());
                questionInfo.put("optionB", question.getOptionB());
                questionInfo.put("optionC", question.getOptionC());
                questionInfo.put("optionD", question.getOptionD());

                // Add the question map to the list
                questionDetails.add(questionInfo);
            }

            // Create a map for each quiz's details
            Map<String, Object> quizInfo = new HashMap<>();
            quizInfo.put("quizName", quiz.getQuizName());
            quizInfo.put("students", studentDetails);
            quizInfo.put("questions", questionDetails);

            return quizInfo;
        }).collect(Collectors.toList());

        // Populate the course summary with all the gathered information
        courseSummary.put("courseTitle", course.getCourseTitle());
        courseSummary.put("courseType", course.getCourseType());
        courseSummary.put("totalEnrolledStudentsCount", totalEnrolledStudentsCount);
        courseSummary.put("courseEnrolledStudentsCount", courseEnrolledStudentsCount);
        courseSummary.put("quizzes", quizDetails);

        return courseSummary;
    }


    public Map<String, Object> getQuizDetailsAndStatisticsByTeacherIdAndStudentId(Long teacherId, Long studentId) {
        List<Quiz> quizzes = quizRepo.findByTeacherIdAndStudentId(teacherId, studentId);

        // Filter out quizzes that have no associated questions
        List<Quiz> quizzesWithQuestions = quizzes.stream()
                .filter(quiz -> questionRepo.existsByQuizId(quiz.getId()))
                .collect(Collectors.toList());

        // Overall statistics (after filtering quizzes with questions)
        int totalQuizzes = quizzesWithQuestions.size();
        long submittedCount = quizzesWithQuestions.stream()
                .filter(quiz -> answerRepo.existsByQuizIdAndStudentId(quiz.getId(), studentId))
                .count();
        long unsubmittedCount = totalQuizzes - submittedCount;

        double progress = (totalQuizzes > 0) ? ((double) submittedCount / totalQuizzes) * 100 : 0.0;

        // Detailed quiz info (only for quizzes that have associated questions)
        List<Map<String, Object>> quizDetails = quizzesWithQuestions.stream().map(quiz -> {
            boolean isSubmitted = answerRepo.existsByQuizIdAndStudentId(quiz.getId(), studentId);
            String status = isSubmitted ? "Submitted" : "Not Submitted";

            Map<String, Object> quizInfo = new HashMap<>();
            quizInfo.put("quizId", quiz.getId());
            quizInfo.put("courseTitle", quiz.getCourse().getCourseTitle());
            quizInfo.put("quizTitle", quiz.getQuizName());
            quizInfo.put("quizStatus", status);
            quizInfo.put("studentName", quiz.getStudent().getName());
            return quizInfo;
        }).collect(Collectors.toList());

        // Combine both the detailed quiz info and statistics
        Map<String, Object> response = new HashMap<>();
        response.put("totalQuizzes", totalQuizzes);
        response.put("submittedCount", submittedCount);
        response.put("unsubmittedCount", unsubmittedCount);
        response.put("progressPercentage", progress);
        response.put("quizzes", quizDetails);

        return response;
    }

    public void deleteQuizById(Long quizId) {
        quizRepo.deleteById(quizId);
    }

    public void updateQuizAndQuestions(Long quizId,
                                       String quizName, String text, String optionA,
                                       String optionB, String optionC, String optionD,
                                       String correctAnswer, LocalDateTime startDate, LocalDateTime endDate) {
        // Fetch quiz by id
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        // Update quiz name if provided
        if (quizName != null && !quizName.isEmpty()) {
            quiz.setQuizName(quizName);
        }

        // Update startDate and endDate if provided
        if (startDate != null) {
            quiz.setStartDate(startDate);
        }
        if (endDate != null) {
            quiz.setEndDate(endDate);
        }

        // Fetch all questions associated with the quiz
        List<Question> questions = questionRepo.findByQuizId(quizId);

        // Update question details for each question
        for (Question question : questions) {
            if (text != null) question.setText(text);
            if (optionA != null) question.setOptionA(optionA);
            if (optionB != null) question.setOptionB(optionB);
            if (optionC != null) question.setOptionC(optionC);
            if (optionD != null) question.setOptionD(optionD);
            if (correctAnswer != null) question.setCorrectAnswer(correctAnswer);
        }

        // Save both quiz and updated questions
        quizRepo.save(quiz);
        questionRepo.saveAll(questions);
    }


    public Map<String, Object> getQuizDetails(Long quizId) {
        List<Object[]> quizDetailsList = quizRepo.findQuizDetailsByQuizId(quizId);

        if (quizDetailsList == null || quizDetailsList.isEmpty()) {
            return null;
        }

        Quiz quiz = null;
        Course course = null;
        List<Map<String, Object>> questionList = new ArrayList<>();

        for (Object[] result : quizDetailsList) {
            if (result.length >= 3) {
                if (quiz == null) {
                    quiz = (Quiz) result[0];
                    course = (Course) result[1];
                }
                Question question = (Question) result[2];

                if (question != null) {
                    Map<String, Object> questionMap = new HashMap<>();
                    questionMap.put("text", question.getText());
                    questionMap.put("optionA", question.getOptionA());
                    questionMap.put("optionB", question.getOptionB());
                    questionMap.put("optionC", question.getOptionC());
                    questionMap.put("optionD", question.getOptionD());
                    questionMap.put("correctAnswer", question.getCorrectAnswer());
                    questionList.add(questionMap);
                }
            }
        }

        if (quiz == null || course == null) {
            return null;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("quizId", quiz.getId());
        response.put("quizName", quiz.getQuizName());
        response.put("startDate", quiz.getStartDate());
        response.put("endDate", quiz.getEndDate());
        response.put("courseId", course.getId());
        response.put("courseName", course.getCourseTitle());
        response.put("questions", questionList);

        return response;
    }


    public List<Map<String, Object>> getQuizDetailsByTeacherId(Long teacherId) {
        // Fetch quizzes where teacherId matches and lessonId, lessonModuleId, studentIds, studentId, and userId are null
        List<Quiz> quizzes = quizRepo.findByTeacherIdAndLessonIsNullAndLessonModuleIsNullAndStudentIdsIsNullAndStudentIsNullAndUserIsNull(teacherId);

        // Prepare the response
        List<Map<String, Object>> response = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            Map<String, Object> quizDetails = new HashMap<>();
            quizDetails.put("quizId", quiz.getId());
            quizDetails.put("quizName", quiz.getQuizName());
            quizDetails.put("courseId", quiz.getCourse().getId());
            quizDetails.put("courseTitle", quiz.getCourse().getCourseTitle());
            quizDetails.put("courseType", quiz.getCourse().getCourseType());
            response.add(quizDetails);
        }

        return response;
    }


}