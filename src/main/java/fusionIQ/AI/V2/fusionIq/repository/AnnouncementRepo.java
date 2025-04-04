package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Announcement;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    void deleteByCourseId(Long id);


}
