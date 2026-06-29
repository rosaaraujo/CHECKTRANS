package es.araujo.checktrans.repository;

import es.araujo.checktrans.domain.ChecklistHeaderValue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistHeaderValueRepository extends JpaRepository<ChecklistHeaderValue, Long> {

    List<ChecklistHeaderValue> findByChecklistIdOrderByHeaderOrderAsc(Long checklistId);

    void deleteByChecklistId(Long checklistId);
}
