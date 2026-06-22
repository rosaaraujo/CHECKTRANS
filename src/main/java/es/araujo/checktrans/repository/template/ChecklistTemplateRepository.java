package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Long> {

    Optional<ChecklistTemplate> findByCode(String code);

    List<ChecklistTemplate> findByActiveTrue();

    boolean existsByCode(String code);

    Page<ChecklistTemplate> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
