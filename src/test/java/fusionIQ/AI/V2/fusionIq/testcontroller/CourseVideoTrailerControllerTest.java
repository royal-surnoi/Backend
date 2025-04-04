package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.CourseVideoTrailerController;
import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import fusionIQ.AI.V2.fusionIq.service.CourseVideoTrailerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseVideoTrailerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseVideoTrailerService courseVideoTrailerService;

    @InjectMocks
    private CourseVideoTrailerController courseVideoTrailerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseVideoTrailerController).build();
    }

    @Test
    public void testUploadVideoTrailer() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, "video content".getBytes());
        CourseVideoTrailer videoTrailer = new CourseVideoTrailer();
        videoTrailer.setS3Url("http://s3.url/video.mp4");

        when(courseVideoTrailerService.uploadTrailer(any(), any(), any(), any())).thenReturn(videoTrailer);

        mockMvc.perform(multipart("/api/course/uploadTrailer/1")
                        .file(file)
                        .param("title", "Sample Video Trailer")
                        .param("description", "Sample Description"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Video trailer uploaded successfully\",\"url\":\"http://s3.url/video.mp4\"}"));
    }

    @Test
    public void testGetVideoTrailersByCourseId() throws Exception {
        CourseVideoTrailer videoTrailer = new CourseVideoTrailer();
        videoTrailer.setId(1L);
        videoTrailer.setS3Url("http://s3.url/video.mp4");

        when(courseVideoTrailerService.getVideoTrailersByCourseId(1L)).thenReturn(Collections.singletonList(videoTrailer));

        mockMvc.perform(get("/api/course/getCourseTrailer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"s3Url\":\"http://s3.url/video.mp4\"}]"));
    }

    @Test
    public void testGetVideoTrailersByCourseId_NoContent() throws Exception {
        when(courseVideoTrailerService.getVideoTrailersByCourseId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/course/getCourseTrailer/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteVideoTrailerById() throws Exception {
        mockMvc.perform(delete("/api/course/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteVideoTrailerById_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("CourseVideoTrailer not found with id: 1"))
                .when(courseVideoTrailerService).deleteVideoTrailerById(1L);

        mockMvc.perform(delete("/api/course/1"))
                .andExpect(status().isNotFound());
    }
}
