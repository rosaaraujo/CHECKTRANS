package es.araujo.checktrans.repository.template;

import static org.junit.jupiter.api.Assertions.*;

import es.araujo.checktrans.domain.enums.ChecklistExecutionStatus;
import es.araujo.checktrans.domain.enums.ItemType;
import es.araujo.checktrans.domain.template.ChecklistItem;
import es.araujo.checktrans.domain.template.ChecklistPhase;
import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ChecklistTemplateRepositoryTest {

    @Autowired
    private ChecklistTemplateRepository templateRepository;

    @Autowired
    private ChecklistTemplateVersionRepository versionRepository;

    @Autowired
    private ChecklistPhaseRepository phaseRepository;

    @Autowired
    private ChecklistTemplateItemRepository itemRepository;

    private ChecklistTemplate createSampleTemplate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setCode("TMP-001");
        template.setName("Inspección General");
        template.setDescription("Plantilla para inspección general de transportes");
        template.setActive(true);
        return templateRepository.save(template);
    }

    private ChecklistTemplateVersion createSampleVersion(ChecklistTemplate template) {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setTemplate(template);
        version.setVersionNumber(1);
        version.setStatus(ChecklistExecutionStatus.CREADA);
        version.setEffectiveDate(LocalDate.now());
        return versionRepository.save(version);
    }

    private ChecklistPhase createSamplePhase(ChecklistTemplateVersion version) {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setVersion(version);
        phase.setPhaseOrder(1);
        phase.setName("Documentación");
        phase.setDescription("Verificación de documentos");
        return phaseRepository.save(phase);
    }

    private ChecklistItem createSampleItem(ChecklistPhase phase) {
        ChecklistItem item = new ChecklistItem();
        item.setPhase(phase);
        item.setItemOrder(1);
        item.setDescription("¿El vehículo tiene documentación en regla?");
        item.setItemType(ItemType.YES_NO_NA);
        item.setRequired(true);
        return itemRepository.save(item);
    }

    @Test
    void shouldSaveAndFindTemplate() {
        ChecklistTemplate saved = createSampleTemplate();

        assertNotNull(saved.getId());
        assertTrue(saved.getActive());
        assertEquals("TMP-001", saved.getCode());
        assertEquals("Inspección General", saved.getName());
    }

    @Test
    void shouldFindByCode() {
        createSampleTemplate();

        ChecklistTemplate found = templateRepository.findByCode("TMP-001").orElse(null);
        assertNotNull(found);
        assertEquals("TMP-001", found.getCode());
    }

    @Test
    void shouldCheckCodeExistence() {
        createSampleTemplate();

        assertTrue(templateRepository.existsByCode("TMP-001"));
        assertFalse(templateRepository.existsByCode("NONEXISTENT"));
    }

    @Test
    void shouldFindActiveTemplates() {
        createSampleTemplate();

        ChecklistTemplate inactive = new ChecklistTemplate();
        inactive.setCode("TMP-002");
        inactive.setName("Inactiva");
        inactive.setActive(false);
        templateRepository.save(inactive);

        List<ChecklistTemplate> active = templateRepository.findByActiveTrue();
        assertEquals(1, active.size());
        assertEquals("TMP-001", active.get(0).getCode());
    }

    @Test
    void shouldSaveTemplateWithVersion() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);

        assertNotNull(version.getId());
        assertEquals(1, version.getVersionNumber());
        assertEquals(ChecklistExecutionStatus.CREADA, version.getStatus());
        assertNotNull(version.getEffectiveDate());
    }

    @Test
    void shouldFindVersionsByTemplateId() {
        ChecklistTemplate template = createSampleTemplate();
        createSampleVersion(template);

        ChecklistTemplateVersion version2 = new ChecklistTemplateVersion();
        version2.setTemplate(template);
        version2.setVersionNumber(2);
        version2.setStatus(ChecklistExecutionStatus.COMPROBADA);
        versionRepository.save(version2);

        List<ChecklistTemplateVersion> versions = versionRepository
                .findByTemplateIdOrderByVersionNumberDesc(template.getId());
        assertEquals(2, versions.size());
        assertEquals(2, versions.get(0).getVersionNumber());
        assertEquals(1, versions.get(1).getVersionNumber());
    }

    @Test
    void shouldFindLatestVersion() {
        ChecklistTemplate template = createSampleTemplate();
        createSampleVersion(template);

        ChecklistTemplateVersion version2 = new ChecklistTemplateVersion();
        version2.setTemplate(template);
        version2.setVersionNumber(2);
        versionRepository.save(version2);

        ChecklistTemplateVersion latest = versionRepository
                .findTopByTemplateIdOrderByVersionNumberDesc(template.getId()).orElse(null);
        assertNotNull(latest);
        assertEquals(2, latest.getVersionNumber());
    }

    @Test
    void shouldSavePhase() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);
        ChecklistPhase phase = createSamplePhase(version);

        assertNotNull(phase.getId());
        assertEquals(1, phase.getPhaseOrder());
        assertEquals("Documentación", phase.getName());
    }

    @Test
    void shouldFindPhasesByVersionId() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);
        createSamplePhase(version);

        ChecklistPhase phase2 = new ChecklistPhase();
        phase2.setVersion(version);
        phase2.setPhaseOrder(2);
        phase2.setName("Estado del vehículo");
        phaseRepository.save(phase2);

        List<ChecklistPhase> phases = phaseRepository.findByVersionIdOrderByPhaseOrderAsc(version.getId());
        assertEquals(2, phases.size());
        assertEquals(1, phases.get(0).getPhaseOrder());
        assertEquals(2, phases.get(1).getPhaseOrder());
    }

    @Test
    void shouldSaveItem() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);
        ChecklistPhase phase = createSamplePhase(version);
        ChecklistItem item = createSampleItem(phase);

        assertNotNull(item.getId());
        assertEquals(1, item.getItemOrder());
        assertEquals(ItemType.YES_NO_NA, item.getItemType());
        assertTrue(item.getRequired());
    }

    @Test
    void shouldFindItemsByPhaseId() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);
        ChecklistPhase phase = createSamplePhase(version);
        createSampleItem(phase);

        ChecklistItem item2 = new ChecklistItem();
        item2.setPhase(phase);
        item2.setItemOrder(2);
        item2.setDescription("¿Cuál es la matrícula?");
        item2.setItemType(ItemType.TEXT);
        item2.setRequired(true);
        itemRepository.save(item2);

        List<ChecklistItem> items = itemRepository.findByPhaseIdOrderByItemOrderAsc(phase.getId());
        assertEquals(2, items.size());
        assertEquals(1, items.get(0).getItemOrder());
        assertEquals(ItemType.YES_NO_NA, items.get(0).getItemType());
        assertEquals(2, items.get(1).getItemOrder());
        assertEquals(ItemType.TEXT, items.get(1).getItemType());
    }

    @Test
    void shouldCascadeDeleteTemplate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setCode("TMP-CASCADE");
        template.setName("Cascade Test");
        template.setActive(true);

        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setVersionNumber(1);
        version.setStatus(ChecklistExecutionStatus.CREADA);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setPhaseOrder(1);
        phase.setName("Phase 1");

        ChecklistItem item = new ChecklistItem();
        item.setItemOrder(1);
        item.setDescription("Item 1");
        item.setItemType(ItemType.TEXT);
        item.setRequired(true);

        template.addVersion(version);
        version.addPhase(phase);
        phase.addItem(item);

        template = templateRepository.save(template);

        Long templateId = template.getId();
        Long versionId = template.getVersions().get(0).getId();
        Long phaseId = template.getVersions().get(0).getPhases().get(0).getId();

        templateRepository.delete(template);

        assertFalse(templateRepository.findById(templateId).isPresent());
        assertFalse(versionRepository.findById(versionId).isPresent());
        assertFalse(phaseRepository.findById(phaseId).isPresent());
    }

    @Test
    void shouldSupportItemTypeVariants() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = createSampleVersion(template);
        ChecklistPhase phase = createSamplePhase(version);

        for (ItemType type : ItemType.values()) {
            ChecklistItem item = new ChecklistItem();
            item.setPhase(phase);
            item.setItemOrder(type.ordinal() + 1);
            item.setDescription("Item " + type);
            item.setItemType(type);
            item.setRequired(true);
            itemRepository.save(item);
        }

        List<ChecklistItem> items = itemRepository.findByPhaseIdOrderByItemOrderAsc(phase.getId());
        assertEquals(ItemType.values().length, items.size());
    }

    @Test
    void shouldSupportExecutionStatusTransition() {
        ChecklistTemplate template = createSampleTemplate();
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setTemplate(template);
        version.setVersionNumber(1);
        version.setStatus(ChecklistExecutionStatus.CREADA);
        version = versionRepository.save(version);

        version.setStatus(ChecklistExecutionStatus.VERIFICADA);
        version = versionRepository.save(version);
        assertEquals(ChecklistExecutionStatus.VERIFICADA, version.getStatus());

        version.setStatus(ChecklistExecutionStatus.COMPROBADA);
        version = versionRepository.save(version);
        assertEquals(ChecklistExecutionStatus.COMPROBADA, version.getStatus());

        version.setStatus(ChecklistExecutionStatus.ANULADA);
        version = versionRepository.save(version);
        assertEquals(ChecklistExecutionStatus.ANULADA, version.getStatus());
    }
}
