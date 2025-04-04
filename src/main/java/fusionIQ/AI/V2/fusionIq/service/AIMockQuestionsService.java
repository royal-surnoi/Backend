package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.AIMock;
import fusionIQ.AI.V2.fusionIq.data.AIMockQuestions;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.AIMockQuestionsRepository;
import fusionIQ.AI.V2.fusionIq.repository.AIMockRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AIMockQuestionsService {

    @Autowired
    private AIMockQuestionsRepository aiMockQuestionsRepo;

    @Autowired
    private AIMockRepo aiMockRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "fusion-chat-bk1"; // Your bucket name
    // Define separate folders for questions and answers
    private final String questionFolderName = "InterviewRecordings/Questions/";
    private final String answerFolderName = "InterviewRecordings/Answers/";

    // Create a new question associated with a user and an AI mock
    public AIMockQuestions addQuestion(Long userId, Long aiMockId, String questionText) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        AIMock aiMock = aiMockRepo.findById(aiMockId)
                .orElseThrow(() -> new RuntimeException("AIMock not found with id: " + aiMockId));

        AIMockQuestions aiMockQuestions = new AIMockQuestions();
        aiMockQuestions.setUser(user);
        aiMockQuestions.setAiMock(aiMock);
        aiMockQuestions.setQuestion(questionText);

        return aiMockQuestionsRepo.save(aiMockQuestions);
    }

    // Upload audio for a question directly to S3 and update the question entity
    public AIMockQuestions uploadQuestionAudio(Long questionId, MultipartFile audioFile) throws IOException {
        AIMockQuestions question = aiMockQuestionsRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        S3UploadResult result = uploadFileToS3(audioFile, questionFolderName);

        question.setQ_s3Key(result.getS3Key());
        question.setQ_s3Url(result.getS3Url());

        return aiMockQuestionsRepo.save(question);
    }

    // Upload audio for an answer directly to S3 and update the question entity
    public AIMockQuestions uploadAnswerAudio(Long questionId, MultipartFile file) throws IOException {
        AIMockQuestions question = aiMockQuestionsRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        S3UploadResult result = uploadFileToS3(file, answerFolderName);

        question.setA_s3Key(result.getS3Key());
        question.setA_s3Url(result.getS3Url());

        return aiMockQuestionsRepo.save(question);
    }

    // Update the answer text for a specific question
    public AIMockQuestions saveAnswer(Long questionId, AIMockQuestions answerDetails) {
        AIMockQuestions question = aiMockQuestionsRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        question.setAnswer(answerDetails.getAnswer());
        return aiMockQuestionsRepo.save(question);
    }

    // Helper method to convert a MultipartFile to a File and upload it to S3
    private S3UploadResult uploadFileToS3(MultipartFile file, String folderName) throws IOException {
        // Generate a unique key for the S3 object
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Convert MultipartFile to File
        File convertedFile = convertMultiPartToFile(file);

        // Upload the file to S3 without setting ACL
        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));

        // Get the S3 URL for the uploaded file
        String s3Url = amazonS3.getUrl(bucketName, key).toString();

        // Delete the temporary file after upload
        if (!convertedFile.delete()) {
            System.err.println("Failed to delete temporary file: " + convertedFile.getAbsolutePath());
        }

        return new S3UploadResult(key, s3Url);
    }


    // Helper method to convert a MultipartFile to a File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    // Inner class to encapsulate the S3 upload result
    private static class S3UploadResult {
        private final String s3Key;
        private final String s3Url;

        public S3UploadResult(String s3Key, String s3Url) {
            this.s3Key = s3Key;
            this.s3Url = s3Url;
        }

        public String getS3Key() {
            return s3Key;
        }

        public String getS3Url() {
            return s3Url;
        }
    }
}
