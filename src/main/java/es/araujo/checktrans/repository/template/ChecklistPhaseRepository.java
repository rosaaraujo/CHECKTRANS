package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistPhase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistPhaseRepository extends JpaRepository<ChecklistPhase, Long> {

    List<ChecklistPhase> findByVersionIdOrderByPhaseOrderAsc(Long versionId);

    Optional<ChecklistPhase> findByVersionIdAndId(Long versionId, Long id);

    boolean existsByVersionIdAndCode(Long versionId, String code);

    Optional<ChecklistPhase> findTopByVersionIdOrderByPhaseOrderDesc(Long versionId);
}
