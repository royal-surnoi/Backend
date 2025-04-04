package fusionIQ.AI.V2.fusionIq.testservice;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseVideoTrailerRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseVideoTrailerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CourseVideoTrailerServiceTest {

    @Mock
    private CourseVideoTrailerRepo courseVideoTrailerRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private CourseVideoTrailerService courseVideoTrailerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadTrailer() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "video content".getBytes());
        Course course = new Course();
        course.setId(1L);

        CourseVideoTrailer videoTrailer = new CourseVideoTrailer();
        videoTrailer.setS3Url("http://s3.url/video.mp4");

        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(courseVideoTrailerRepo.save(any(CourseVideoTrailer.class))).thenReturn(videoTrailer);
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL("http://s3.url/video.mp4"));

        CourseVideoTrailer savedTrailer = courseVideoTrailerService.uploadTrailer(file, 1L, "Sample Title", "Sample Description");

        assertEquals("http://s3.url/video.mp4", savedTrailer.getS3Url());
    }

    @Test
    public void testGetVideoTrailersByCourseId() {
        courseVideoTrailerService.getVideoTrailersByCourseId(1L);
        verify(courseVideoTrailerRepo, times(1)).findByCourseId(1L);
    }

    @Test
    public void testDeleteVideoTrailerById() {
        when(courseVideoTrailerRepo.existsById(1L)).thenReturn(true);
        courseVideoTrailerService.deleteVideoTrailerById(1L);
        verify(courseVideoTrailerRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteVideoTrailerById_NotFound() {
        when(courseVideoTrailerRepo.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> courseVideoTrailerService.deleteVideoTrailerById(1L));
    }
}



