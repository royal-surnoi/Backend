package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseVideoTrailerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CourseVideoTrailerService {

    @Autowired
    CourseVideoTrailerRepo courseVideoTrailerRepo;
    @Autowired
    CourseRepo courseRepo;

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "fusion-chat-bk1";
    private final String folderName = "CourseTrailers/";


    public CourseVideoTrailer uploadTrailer(MultipartFile file, Long courseId, String videoTrailerTitle, String videoTrailerDescription) throws IOException {
        // Generate a unique key for the S3 object
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Convert MultipartFile to a File object
        File convertedFile = convertMultiPartToFile(file);

        // Upload the file to S3
        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
        String s3Url = amazonS3.getUrl(bucketName, key).toString();

        // Delete the temporary file after upload
        if (!convertedFile.delete()) {
            System.err.println("Failed to delete file: " + convertedFile.getAbsolutePath());
        }


        // Create a new CourseVideoTrailer entity
        CourseVideoTrailer videoTrailer = new CourseVideoTrailer();
        videoTrailer.setVideoTrailerTitle(videoTrailerTitle);
        videoTrailer.setS3Key(key);
        videoTrailer.setS3Url(s3Url);
        videoTrailer.setVideoTrailerDescription(videoTrailerDescription);

        // Fetch the course and associate it with the video trailer
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + courseId));
        videoTrailer.setCourse(course);

        // Save and return the CourseVideoTrailer entity
        return courseVideoTrailerRepo.save(videoTrailer);
    }

    // Helper method to convert MultipartFile to File
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } // try-with-resources ensures fos is closed automatically

        return convFile;
    }

    public List<CourseVideoTrailer> getVideoTrailersByCourseId(Long courseId) {
        return courseVideoTrailerRepo.findByCourseId(courseId);
    }

    public void deleteVideoTrailerById(Long id) {
        if (courseVideoTrailerRepo.existsById(id)) {
            courseVideoTrailerRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("CourseVideoTrailer not found with id: " + id);
        }
    }
}