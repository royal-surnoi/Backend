package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.FileUploadController;
import fusionIQ.AI.V2.fusionIq.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFile_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        String fileUrl = "http://example.com/file";

        when(chatService.uploadFile(file)).thenReturn(fileUrl);

        ResponseEntity<String> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileUrl, response.getBody());
        verify(chatService, times(1)).uploadFile(file);
    }

    @Test
    void testUploadFile_Failure() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(chatService.uploadFile(file)).thenThrow(new IOException("Upload failed"));

        ResponseEntity<String> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload file: Upload failed", response.getBody());
        verify(chatService, times(1)).uploadFile(file);
    }
}
