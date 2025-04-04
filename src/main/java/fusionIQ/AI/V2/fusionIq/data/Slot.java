package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String slotName;


    private LocalDateTime slotTime;


    private LocalDateTime EndTime;


    private Boolean booked = false;

    @ManyToOne
    @JoinColumn(name = "mock_id", nullable = false)
    private MockTestInterview mockTestInterview;

    public Slot() {}

    public Slot(Long id, String slotName, LocalDateTime slotTime, LocalDateTime endTime, Boolean booked, MockTestInterview mockTestInterview) {
        this.id = id;
        this.slotName = slotName;
        this.slotTime = slotTime;
        this.EndTime = endTime;
        this.booked = booked;
        this.mockTestInterview = mockTestInterview;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public LocalDateTime getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(LocalDateTime slotTime) {
        this.slotTime = slotTime;
    }

    public Boolean getBooked() {
        return booked;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    public MockTestInterview getMockTestInterview() {
        return mockTestInterview;
    }


    public LocalDateTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.EndTime = endTime;
    }

    public void setMockTestInterview(MockTestInterview mockTestInterview) {
        this.mockTestInterview = mockTestInterview;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", slotName='" + slotName + '\'' +
                ", slotTime=" + slotTime +
                ", EndTime=" + EndTime +
                ", booked=" + booked +
                ", mockTestInterview=" + mockTestInterview +
                '}';
    }
}
