package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateItemRepository extends JpaRepository<ChecklistItem, Long> {

    List<ChecklistItem> findByPhaseIdOrderByItemOrderAsc(Long phaseId);

    Optional<ChecklistItem> findByPhaseIdAndId(Long phaseId, Long id);

    boolean existsByPhaseIdAndCode(Long phaseId, String code);

    Optional<ChecklistItem> findTopByPhaseIdOrderByItemOrderDesc(Long phaseId);
}
