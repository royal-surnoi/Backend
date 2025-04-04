package fusionIQ.AI.V2.fusionIq.testcontroller;
import fusionIQ.AI.V2.fusionIq.controller.PersonalDetailsController;
import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import fusionIQ.AI.V2.fusionIq.service.PersonalDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonalDetailsControllerTest {

    @InjectMocks
    private PersonalDetailsController personalDetailsController;

    @Mock
    private PersonalDetailsService personalDetailsService;

    private PersonalDetails personalDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        personalDetails = new PersonalDetails();
        personalDetails.setId(1L);
        personalDetails.setFirstName("John");
        personalDetails.setLastName("Doe");
    }

    @Test
    public void testCreatePersonalDetails() {
        when(personalDetailsService.savePersonalDetails(anyLong(), any(PersonalDetails.class))).thenReturn(personalDetails);

        ResponseEntity<PersonalDetails> response = personalDetailsController.createPersonalDetails(1L, personalDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personalDetails, response.getBody());

        verify(personalDetailsService, times(1)).savePersonalDetails(anyLong(), any(PersonalDetails.class));
    }

    @Test
    public void testGetPersonalDetailsById_Found() {
        when(personalDetailsService.getPersonalDetailsById(1L)).thenReturn(personalDetails);

        ResponseEntity<PersonalDetails> response = personalDetailsController.getPersonalDetailsById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personalDetails, response.getBody());

        verify(personalDetailsService, times(1)).getPersonalDetailsById(1L);
    }

    @Test
    public void testGetPersonalDetailsById_NotFound() {
        when(personalDetailsService.getPersonalDetailsById(1L)).thenReturn(null);

        ResponseEntity<PersonalDetails> response = personalDetailsController.getPersonalDetailsById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(personalDetailsService, times(1)).getPersonalDetailsById(1L);
    }

    @Test
    public void testGetAllPersonalDetails() {
        List<PersonalDetails> detailsList = Arrays.asList(personalDetails);
        when(personalDetailsService.getAllPersonalDetails()).thenReturn(detailsList);

        ResponseEntity<List<PersonalDetails>> response = personalDetailsController.getAllPersonalDetails();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(detailsList, response.getBody());

        verify(personalDetailsService, times(1)).getAllPersonalDetails();
    }

    @Test
    public void testDeletePersonalDetails() {
        doNothing().when(personalDetailsService).deletePersonalDetails(1L);

        ResponseEntity<Void> response = personalDetailsController.deletePersonalDetails(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(personalDetailsService, times(1)).deletePersonalDetails(1L);
    }

    @Test
    public void testUpdatePersonalDetails_Success() {
        when(personalDetailsService.updatePersonalDetails(anyLong(), any(PersonalDetails.class))).thenReturn(personalDetails);

        ResponseEntity<PersonalDetails> response = personalDetailsController.updatePersonalDetails(1L, personalDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personalDetails, response.getBody());

        verify(personalDetailsService, times(1)).updatePersonalDetails(anyLong(), any(PersonalDetails.class));
    }

    @Test
    public void testUpdatePersonalDetails_NotFound() {
        when(personalDetailsService.updatePersonalDetails(anyLong(), any(PersonalDetails.class))).thenThrow(new RuntimeException("Personal details not found"));

        ResponseEntity<PersonalDetails> response = personalDetailsController.updatePersonalDetails(1L, personalDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(personalDetailsService, times(1)).updatePersonalDetails(anyLong(), any(PersonalDetails.class));
    }
}
