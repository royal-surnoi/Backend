package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.CourseVideoController;
import fusionIQ.AI.V2.fusionIq.data.Video;
import fusionIQ.AI.V2.fusionIq.data.VideoProgress;
import fusionIQ.AI.V2.fusionIq.service.CourseVideoService;
import fusionIQ.AI.V2.fusionIq.service.VideoProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseVideoControllerTest {

    @Mock
    private CourseVideoService courseVideoService;

    @Mock
    private VideoProgressService videoProgressService;

    @InjectMocks
    private CourseVideoController courseVideoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadVideo_Success() throws IOException {
        Long courseId = 1L;
        Long lessonId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Video video = new Video();
        video.setS3Url("http://example.com/video");

        when(courseVideoService.uploadVideo(file, courseId, lessonId)).thenReturn(video);

        ResponseEntity<String> response = courseVideoController.uploadVideo(courseId, lessonId, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File uploaded successfully, URL: http://example.com/video", response.getBody());
        verify(courseVideoService, times(1)).uploadVideo(file, courseId, lessonId);
    }

    @Test
    void testUploadVideo_Failure() throws IOException {
        Long courseId = 1L;
        Long lessonId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(courseVideoService.uploadVideo(file, courseId, lessonId)).thenThrow(new IOException("Upload failed"));

        ResponseEntity<String> response = courseVideoController.uploadVideo(courseId, lessonId, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File upload failed: Upload failed", response.getBody());
        verify(courseVideoService, times(1)).uploadVideo(file, courseId, lessonId);
    }

    @Test
    void testGetVideosByCourse_Success() {
        Long courseId = 1L;
        Video video1 = new Video();
        Video video2 = new Video();
        List<Video> videos = Arrays.asList(video1, video2);

        when(courseVideoService.getVideosByCourse(courseId)).thenReturn(videos);

        ResponseEntity<List<Video>> response = courseVideoController.getVideosByCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseVideoService, times(1)).getVideosByCourse(courseId);
    }




    @Test
    void testUpdateVideo_Success() throws IOException {
        Long videoId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        String description = "Updated description";

        Video updatedVideo = new Video();
        updatedVideo.setVideoDescription(description);

        when(courseVideoService.updateVideo(videoId, file, description)).thenReturn(updatedVideo);

        ResponseEntity<Video> response = courseVideoController.updateVideo(videoId, file, description);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedVideo, response.getBody());
        verify(courseVideoService, times(1)).updateVideo(videoId, file, description);
    }

    @Test
    void testUpdateVideo_Failure() throws IOException {
        Long videoId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        String description = "Updated description";

        when(courseVideoService.updateVideo(videoId, file, description)).thenThrow(new IOException("Update failed"));

        ResponseEntity<Video> response = courseVideoController.updateVideo(videoId, file, description);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(courseVideoService, times(1)).updateVideo(videoId, file, description);
    }

    @Test
    void testDeleteVideo_Success() {
        Long videoId = 1L;

        doNothing().when(courseVideoService).deleteVideo(videoId);

        ResponseEntity<String> response = courseVideoController.deleteVideo(videoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Video deleted successfully", response.getBody());
        verify(courseVideoService, times(1)).deleteVideo(videoId);
    }






    @Test
    void testGetUserCourseProgress_Success() {
        Long userId = 1L;
        Long courseId = 1L;
        double expectedProgress = 75.0;

        when(videoProgressService.calculateProgressPercentage(userId, courseId)).thenReturn(expectedProgress);

        ResponseEntity<Double> response = courseVideoController.getUserCourseProgress(userId, courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProgress, response.getBody());
        verify(videoProgressService, times(1)).calculateProgressPercentage(userId, courseId);
    }

    @Test
    void testGetUserCourseProgress_Failure() {
        Long userId = 1L;
        Long courseId = 1L;

        when(videoProgressService.calculateProgressPercentage(userId, courseId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Double> response = courseVideoController.getUserCourseProgress(userId, courseId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(videoProgressService, times(1)).calculateProgressPercentage(userId, courseId);
    }

    @Test
    void testSaveOrUpdateProgress_Success() {
        Long userId = 1L;
        Long videoId = 1L;
        double progress = 50.0;

        doNothing().when(courseVideoService).saveOrUpdateProgress(userId, videoId, progress);

        ResponseEntity<String> response = courseVideoController.saveOrUpdateProgress(userId, videoId, progress);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Progress saved or updated successfully", response.getBody());
        verify(courseVideoService, times(1)).saveOrUpdateProgress(userId, videoId, progress);
    }

    @Test
    void testSaveOrUpdateProgress_Failure() {
        Long userId = 1L;
        Long videoId = 1L;
        double progress = 50.0;

        doThrow(new RuntimeException("Error")).when(courseVideoService).saveOrUpdateProgress(userId, videoId, progress);

        ResponseEntity<String> response = courseVideoController.saveOrUpdateProgress(userId, videoId, progress);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error saving or updating progress: Error", response.getBody());
        verify(courseVideoService, times(1)).saveOrUpdateProgress(userId, videoId, progress);
    }

    // Add additional test cases for other endpoints as needed
}
