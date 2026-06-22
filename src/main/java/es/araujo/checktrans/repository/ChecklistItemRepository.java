package es.araujo.checktrans.repository;

import es.araujo.checktrans.domain.ChecklistItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    List<ChecklistItem> findByChecklistIdOrderByItemOrderAsc(Long checklistId);

    void deleteByChecklistId(Long checklistId);
}
