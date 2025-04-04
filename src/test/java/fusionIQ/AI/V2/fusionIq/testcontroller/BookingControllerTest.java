// package fusionIQ.AI.V2.fusionIq.testcontroller;





// import fusionIQ.AI.V2.fusionIq.controller.BookingController;
// import fusionIQ.AI.V2.fusionIq.data.BookedMockTestInterview;
// import fusionIQ.AI.V2.fusionIq.data.BookingRequest;
// import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
// import fusionIQ.AI.V2.fusionIq.data.Slot;
// import fusionIQ.AI.V2.fusionIq.service.SlotService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// public class BookingControllerTest {

//     private MockMvc mockMvc;

//     @Mock
//     private SlotService slotService;

//     @InjectMocks
//     private BookingController bookingController;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
//     }

//     @Test
//     public void testBookSlot() throws Exception {
//         BookedMockTestInterview bookedMockTestInterview = new BookedMockTestInterview();
//         bookedMockTestInterview.setId(1L);

//         when(slotService.bookSlot(any(BookingRequest.class))).thenReturn(bookedMockTestInterview);

//         mockMvc.perform(post("/api/bookings/book")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"slotId\":1, \"userId\":1}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("{\"id\":1}"));
//     }

//     @Test
//     public void testGetBookedSlots() throws Exception {
//         BookedMockTestInterview bookedMockTestInterview = new BookedMockTestInterview();
//         bookedMockTestInterview.setId(1L);

//         when(slotService.getBookedSlots(1L)).thenReturn(Collections.singletonList(bookedMockTestInterview));

//         mockMvc.perform(get("/api/bookings/booked/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("[{\"id\":1}]"));
//     }


//     @Test
//     public void testGetBookedSlotsByUserId_NoContent() throws Exception {
//         when(slotService.getBookedSlotByUserId(1L)).thenReturn(Collections.emptyList());

//         mockMvc.perform(get("/api/bookings/getBookedDetails/1"))
//                 .andExpect(status().isNoContent());
//     }

//     @Test
//     public void testGetBookedSlotsByUserId_WithContent() throws Exception {
//         BookedMockTestInterview bookedMockTestInterview = new BookedMockTestInterview();
//         bookedMockTestInterview.setId(1L);

//         when(slotService.getBookedSlotByUserId(1L)).thenReturn(Collections.singletonList(bookedMockTestInterview));

//         mockMvc.perform(get("/api/bookings/getBookedDetails/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("[{\"id\":1}]"));
//     }
// }
