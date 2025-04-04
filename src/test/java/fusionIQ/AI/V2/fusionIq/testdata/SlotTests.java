package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Slot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SlotTests {

    @Test
    void testDefaultConstructor() {
        Slot slot = new Slot();

        // Verifying that the default constructor initializes fields correctly
        assertThat(slot.getId()).isNull();
        assertThat(slot.getSlotName()).isNull();
        assertThat(slot.getSlotTime()).isNull();
        assertThat(slot.getEndTime()).isNull();
        assertThat(slot.getBooked()).isFalse(); // Default value for booked is false
        assertThat(slot.getMockTestInterview()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        MockTestInterview mockTestInterview = new MockTestInterview();
        LocalDateTime slotTime = LocalDateTime.now();
        LocalDateTime endTime = slotTime.plusHours(1);

        Slot slot = new Slot(1L, "Morning Slot", slotTime, endTime, true, mockTestInterview);

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(slot.getId()).isEqualTo(1L);
        assertThat(slot.getSlotName()).isEqualTo("Morning Slot");
        assertThat(slot.getSlotTime()).isEqualTo(slotTime);
        assertThat(slot.getEndTime()).isEqualTo(endTime);
        assertThat(slot.getBooked()).isTrue();
        assertThat(slot.getMockTestInterview()).isEqualTo(mockTestInterview);
    }

    @Test
    void testSettersAndGetters() {
        Slot slot = new Slot();
        MockTestInterview mockTestInterview = new MockTestInterview();
        LocalDateTime slotTime = LocalDateTime.now();
        LocalDateTime endTime = slotTime.plusHours(1);

        slot.setId(1L);
        slot.setSlotName("Afternoon Slot");
        slot.setSlotTime(slotTime);
        slot.setEndTime(endTime);
        slot.setBooked(true);
        slot.setMockTestInterview(mockTestInterview);

        // Verifying the setters and getters
        assertThat(slot.getId()).isEqualTo(1L);
        assertThat(slot.getSlotName()).isEqualTo("Afternoon Slot");
        assertThat(slot.getSlotTime()).isEqualTo(slotTime);
        assertThat(slot.getEndTime()).isEqualTo(endTime);
        assertThat(slot.getBooked()).isTrue();
        assertThat(slot.getMockTestInterview()).isEqualTo(mockTestInterview);
    }

    @Test
    void testToString() {
        MockTestInterview mockTestInterview = new MockTestInterview();
        LocalDateTime slotTime = LocalDateTime.now();
        LocalDateTime endTime = slotTime.plusHours(1);

        Slot slot = new Slot(1L, "Evening Slot", slotTime, endTime, false, mockTestInterview);

        String expectedToString = "Slot{" +
                "id=" + slot.getId() +
                ", slotName='" + slot.getSlotName() + '\'' +
                ", slotTime=" + slot.getSlotTime() +
                ", EndTime=" + slot.getEndTime() +  // Ensure this matches the entity's field name
                ", booked=" + slot.getBooked() +
                ", mockTestInterview=" + slot.getMockTestInterview() +
                '}';

        // Verifying the toString method
        assertThat(slot.toString()).isEqualTo(expectedToString);
    }
}
