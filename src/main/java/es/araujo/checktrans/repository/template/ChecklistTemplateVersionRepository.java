package es.araujo.checktrans.repository.template;

import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateVersionRepository extends JpaRepository<ChecklistTemplateVersion, Long> {

    List<ChecklistTemplateVersion> findByTemplateIdOrderByVersionNumberDesc(Long templateId);

    Optional<ChecklistTemplateVersion> findTopByTemplateIdOrderByVersionNumberDesc(Long templateId);
}
