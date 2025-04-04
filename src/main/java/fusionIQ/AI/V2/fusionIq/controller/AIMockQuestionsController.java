package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AIMockQuestions;
import fusionIQ.AI.V2.fusionIq.service.AIMockQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AIMockQuestionsController {

    @Autowired
    private AIMockQuestionsService aiMockQuestionsService;

    @PostMapping("/createQuestion")
    public ResponseEntity<AIMockQuestions> createQuestion(
            @RequestParam Long userId,
            @RequestParam Long aiMockId,
            @RequestParam String question,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile) throws IOException {

        // Create the question
        AIMockQuestions savedQuestion = aiMockQuestionsService.addQuestion(userId, aiMockId, question);

        // If an audio file is provided, upload and update the question
        if (audioFile != null && !audioFile.isEmpty()) {
            savedQuestion = aiMockQuestionsService.uploadQuestionAudio(savedQuestion.getId(), audioFile);
        }

        return ResponseEntity.ok(savedQuestion);
    }


//    @PostMapping("/add")
//    public ResponseEntity<AIMockQuestions> addQuestion(
//            @RequestParam Long userId,
//            @RequestParam Long aiMockId,
//            @RequestParam String question) {
//
//        AIMockQuestions savedQuestion = aiMockQuestionsService.addQuestion(userId, aiMockId, question);
//        return ResponseEntity.ok(savedQuestion);
//    }
//
//
//    //  Upload/Update audio file for a specific AI mock question
//    @PutMapping("/uploadAudio/{id}")
//    public ResponseEntity<AIMockQuestions> uploadAudio(
//            @PathVariable Long id,
//            @RequestParam("audioFile") MultipartFile audioFile) throws IOException {
//        AIMockQuestions updatedQuestion = aiMockQuestionsService.uploadQuestionAudio(id, audioFile);
//        return ResponseEntity.ok(updatedQuestion);
//    }

    //  Update the answer for a specific question
    @PutMapping("/submitAnswer/{id}")
    public ResponseEntity<AIMockQuestions> submitAnswer(
            @PathVariable Long id,
            @RequestBody AIMockQuestions answer) {
        AIMockQuestions savedAnswer = aiMockQuestionsService.saveAnswer(id, answer);
        return ResponseEntity.ok(savedAnswer);
    }

    //  Upload/Update audio file for an answer
    @PutMapping("/answerAudio/{id}")
    public ResponseEntity<AIMockQuestions> uploadAnswerAudio(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        AIMockQuestions updatedAnswer = aiMockQuestionsService.uploadAnswerAudio(id, file);
        return ResponseEntity.ok(updatedAnswer);
    }
}