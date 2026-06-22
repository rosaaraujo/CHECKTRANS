package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateItemRepository extends JpaRepository<ChecklistItem, Long> {

    List<ChecklistItem> findByPhaseIdOrderByItemOrderAsc(Long phaseId);
}
