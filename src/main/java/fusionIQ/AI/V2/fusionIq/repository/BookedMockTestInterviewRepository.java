package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.BookedMockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookedMockTestInterviewRepository extends JpaRepository<BookedMockTestInterview, Long> {

    List<BookedMockTestInterview> findBySlotMockTestInterviewId(Long mockTestInterviewId);
    List<BookedMockTestInterview> findByUserId(Long userId);

    Optional<BookedMockTestInterview> findBySlot(Slot slot);


    @Query("SELECT b FROM BookedMockTestInterview b " +
            "JOIN FETCH b.slot s " +
            "JOIN FETCH s.mockTestInterview m " +
            "WHERE b.user.id = :userId")
    List<BookedMockTestInterview> findBookedSlotsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT b FROM BookedMockTestInterview b " +
            "JOIN FETCH b.slot s " +
            "JOIN FETCH s.mockTestInterview m " +
            "LEFT JOIN FETCH m.project p " +
            "LEFT JOIN FETCH m.assignment a " +
            "LEFT JOIN FETCH m.trainingRoom tr " + // Assuming MockTestInterview is mapped to TrainingRoom
            "WHERE b.user.id = :userId")
    List<BookedMockTestInterview> findBookedSlotsWithDetailsByUserId(@Param("userId") Long userId);




}
