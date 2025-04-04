package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Slot;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {
//    List<Slot> findByMockTestInterviewId(Long interviewId);

    List<Slot> findByMockTestInterviewIdAndBookedFalse(Long mockId);

    List<Slot> findByMockTestInterviewId(Long mockId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Slot> findById(Long id);

    Optional<Slot> findByMockTestInterviewIdAndId(Long mockTestInterviewId, Long slotId);


    @Query("SELECT s.id as slotId, m.id as mockId, c.id as courseId, s.slotName, s.slotTime, s.EndTime, " +
            "m.testType, c.courseTitle " +
            "FROM Slot s " +
            "JOIN s.mockTestInterview m " +
            "JOIN m.relatedCourseId c " +
            "WHERE m.id = :mockId AND s.booked = false")
    List<Object[]> findAvailableSlotDetailsByMockId(@Param("mockId") Long mockId);
    boolean existsByMockTestInterviewIdAndBooked(Long mockId, Boolean booked);

    @Transactional
    void deleteByMockTestInterview_Id(Long mockTestInterviewId); // Correct property reference


}
