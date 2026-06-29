package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistTemplateHeader;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistTemplateHeaderRepository extends JpaRepository<ChecklistTemplateHeader, Long> {

    List<ChecklistTemplateHeader> findByTemplateIdOrderByHeaderOrderAsc(Long templateId);

    Optional<ChecklistTemplateHeader> findByTemplateIdAndId(Long templateId, Long id);

    boolean existsByTemplateIdAndCode(Long templateId, String code);

    void deleteByTemplateId(Long templateId);
}
