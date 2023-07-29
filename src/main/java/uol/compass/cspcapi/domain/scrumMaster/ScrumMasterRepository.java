package uol.compass.cspcapi.domain.scrumMaster;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrumMasterRepository extends JpaRepository<ScrumMaster, Long> {
    List<ScrumMaster> findAllByIdIn(List<Long> scrumMastersIds);
}
