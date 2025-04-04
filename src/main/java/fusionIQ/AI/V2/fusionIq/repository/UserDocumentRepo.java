package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.UserDocunment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDocumentRepo  extends JpaRepository<UserDocunment, Long> {
    List<UserDocunment> findByUser_Id(Long userId);
}
