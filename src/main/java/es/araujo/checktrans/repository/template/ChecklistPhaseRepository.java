package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistPhase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistPhaseRepository extends JpaRepository<ChecklistPhase, Long> {

    List<ChecklistPhase> findByVersionIdOrderByPhaseOrderAsc(Long versionId);
}
