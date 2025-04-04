//package fusionIQ.AI.V2.fusionIq.testservice;
//
//
//
//import fusionIQ.AI.V2.fusionIq.data.BookedMockTestInterview;
//import fusionIQ.AI.V2.fusionIq.data.BookingRequest;
//import fusionIQ.AI.V2.fusionIq.data.Slot;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import fusionIQ.AI.V2.fusionIq.repository.BookedMockTestInterviewRepository;
//import fusionIQ.AI.V2.fusionIq.repository.SlotRepository;
//import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
//import fusionIQ.AI.V2.fusionIq.service.SlotService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class SlotServiceTest {
//
//    @Mock
//    private SlotRepository slotRepository;
//
//    @Mock
//    private BookedMockTestInterviewRepository bookedMockTestInterviewRepository;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @InjectMocks
//    private SlotService slotService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testFindAllByInterviewId() {
//        Slot slot = new Slot();
//        slot.setId(1L);
//
//        when(slotRepository.findByMockTestInterviewId(1L)).thenReturn(List.of(slot));
//
//        List<Slot> slots = slotService.findAllByInterviewId(1L);
//
//        assertEquals(1, slots.size());
//        verify(slotRepository, times(1)).findByMockTestInterviewId(1L);
//    }
//
//    @Test
//    public void testGetAvailableSlots() {
//        Slot slot = new Slot();
//        slot.setId(1L);
//
//        when(slotRepository.findByMockTestInterviewIdAndBookedFalse(1L)).thenReturn(List.of(slot));
//
//        List<Slot> slots = slotService.getAvailableSlots(1L);
//
//        assertEquals(1, slots.size());
//        verify(slotRepository, times(1)).findByMockTestInterviewIdAndBookedFalse(1L);
//    }
//
//    @Test
//    public void testBookSlot_Success() {
//        BookingRequest request = new BookingRequest();
//        request.setSlotId(1L);
//        request.setUserId(1L);
//
//        Slot slot = new Slot();
//        slot.setId(1L);
//        slot.setBooked(false);
//
//        User user = new User();
//        user.setId(1L);
//
//        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
//        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
//        when(bookedMockTestInterviewRepository.save(any(BookedMockTestInterview.class))).thenReturn(new BookedMockTestInterview());
//
//        BookedMockTestInterview bookedSlot = slotService.bookSlot(request);
//
//        assertNotNull(bookedSlot);
//        assertTrue(slot.getBooked());
//        verify(slotRepository, times(1)).save(slot);
//        verify(bookedMockTestInterviewRepository, times(1)).save(any(BookedMockTestInterview.class));
//    }
//
//    @Test
//    public void testBookSlot_SlotAlreadyBooked() {
//        BookingRequest request = new BookingRequest();
//        request.setSlotId(1L);
//        request.setUserId(1L);
//
//        Slot slot = new Slot();
//        slot.setId(1L);
//        slot.setBooked(true);
//
//        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> slotService.bookSlot(request));
//        assertEquals("Slot already booked", exception.getMessage());
//    }
//
//    @Test
//    public void testGetBookedSlotsByUserId() {
//        BookedMockTestInterview bookedMockTestInterview = new BookedMockTestInterview();
//        bookedMockTestInterview.setId(1L);
//
//        when(bookedMockTestInterviewRepository.findByUserId(1L)).thenReturn(List.of(bookedMockTestInterview));
//
//        List<BookedMockTestInterview> bookedSlots = slotService.getBookedSlotsByUserId(1L);
//
//        assertEquals(1, bookedSlots.size());
//        verify(bookedMockTestInterviewRepository, times(1)).findByUserId(1L);
//    }
//}
