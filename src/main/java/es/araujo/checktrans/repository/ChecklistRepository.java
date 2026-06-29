package es.araujo.checktrans.repository;

import es.araujo.checktrans.domain.Checklist;
import es.araujo.checktrans.domain.enums.ChecklistStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    boolean existsByCode(String code);

    Page<Checklist> findByInspectorNameContainingIgnoreCase(String inspectorName, Pageable pageable);

    List<Checklist> findByInspectorNameContainingIgnoreCaseOrderByCheckDateDesc(String inspectorName);

    List<Checklist> findByStatusOrderByCheckDateDesc(ChecklistStatus status);

    List<Checklist> findByCheckDateBetweenOrderByCheckDateDesc(LocalDateTime from, LocalDateTime to);
}
