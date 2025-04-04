// package fusionIQ.AI.V2.fusionIq.testservice;

// import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
// import fusionIQ.AI.V2.fusionIq.data.User;
// import fusionIQ.AI.V2.fusionIq.repository.PersonalDetailsRepo;
// import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
// import fusionIQ.AI.V2.fusionIq.service.PersonalDetailsService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// public class PersonalDetailsServiceTest {

//     @InjectMocks
//     private PersonalDetailsService personalDetailsService;

//     @Mock
//     private PersonalDetailsRepo personalDetailsRepo;

//     @Mock
//     private UserRepo userRepo;

//     private User user;
//     private PersonalDetails personalDetails;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);

//         user = new User();
//         user.setId(1L);
//         user.setName("John Doe");

//         personalDetails = new PersonalDetails();
//         personalDetails.setId(1L);
//         personalDetails.setFirstName("John");
//         personalDetails.setLastName("Doe");
//         personalDetails.setUser(user);
//     }

//     @Test
//     public void testSavePersonalDetails_UserExists() {
//         when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
//         when(personalDetailsRepo.save(any(PersonalDetails.class))).thenReturn(personalDetails);

//         PersonalDetails savedDetails = personalDetailsService.savePersonalDetails(user.getId(), personalDetails);

//         assertNotNull(savedDetails);
//         assertEquals("John", savedDetails.getFirstName());
//         assertEquals(user, savedDetails.getUser());

//         verify(personalDetailsRepo, times(1)).save(any(PersonalDetails.class));
//         verify(userRepo, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testSavePersonalDetails_UserNotFound() {
//         when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

//         Exception exception = assertThrows(RuntimeException.class, () -> {
//             personalDetailsService.savePersonalDetails(user.getId(), personalDetails);
//         });

//         assertEquals("User not found", exception.getMessage());
//         verify(personalDetailsRepo, times(0)).save(any(PersonalDetails.class));
//     }

//     @Test
//     public void testGetPersonalDetailsById_DetailsExist() {
//         when(personalDetailsRepo.findById(personalDetails.getId())).thenReturn(Optional.of(personalDetails));

//         PersonalDetails fetchedDetails = personalDetailsService.getPersonalDetailsById(personalDetails.getId());

//         assertNotNull(fetchedDetails);
//         assertEquals("John", fetchedDetails.getFirstName());

//         verify(personalDetailsRepo, times(1)).findById(personalDetails.getId());
//     }

//     @Test
//     public void testGetPersonalDetailsById_DetailsNotFound() {
//         when(personalDetailsRepo.findById(personalDetails.getId())).thenReturn(Optional.empty());

//         PersonalDetails fetchedDetails = personalDetailsService.getPersonalDetailsById(personalDetails.getId());

//         assertNull(fetchedDetails);
//         verify(personalDetailsRepo, times(1)).findById(personalDetails.getId());
//     }

//     @Test
//     public void testUpdatePersonalDetails_DetailsExist() {
//         PersonalDetails updatedDetails = new PersonalDetails();
//         updatedDetails.setFirstName("Jane");
//         updatedDetails.setLastName("Doe");

//         when(personalDetailsRepo.findById(personalDetails.getId())).thenReturn(Optional.of(personalDetails));
//         when(personalDetailsRepo.save(any(PersonalDetails.class))).thenReturn(personalDetails);

//         PersonalDetails result = personalDetailsService.updatePersonalDetails(personalDetails.getId(), updatedDetails);

//         assertNotNull(result);
//         assertEquals("Jane", result.getFirstName());
//         assertEquals("Doe", result.getLastName());

//         verify(personalDetailsRepo, times(1)).findById(personalDetails.getId());
//         verify(personalDetailsRepo, times(1)).save(any(PersonalDetails.class));
//     }

//     @Test
//     public void testUpdatePersonalDetails_DetailsNotFound() {
//         when(personalDetailsRepo.findById(personalDetails.getId())).thenReturn(Optional.empty());

//         PersonalDetails updatedDetails = new PersonalDetails();
//         updatedDetails.setFirstName("Jane");

//         Exception exception = assertThrows(RuntimeException.class, () -> {
//             personalDetailsService.updatePersonalDetails(personalDetails.getId(), updatedDetails);
//         });

//         assertEquals("Personal details not found", exception.getMessage());
//         verify(personalDetailsRepo, times(1)).findById(personalDetails.getId());
//         verify(personalDetailsRepo, times(0)).save(any(PersonalDetails.class));
//     }

//     @Test
//     public void testDeletePersonalDetails() {
//         doNothing().when(personalDetailsRepo).deleteById(personalDetails.getId());

//         personalDetailsService.deletePersonalDetails(personalDetails.getId());

//         verify(personalDetailsRepo, times(1)).deleteById(personalDetails.getId());
//     }

//     @Test
//     public void testGetAllPersonalDetails() {
//         when(personalDetailsRepo.findAll()).thenReturn(List.of(personalDetails));

//         List<PersonalDetails> detailsList = personalDetailsService.getAllPersonalDetails();

//         assertNotNull(detailsList);
//         assertEquals(1, detailsList.size());
//         verify(personalDetailsRepo, times(1)).findAll();
//     }
// }

