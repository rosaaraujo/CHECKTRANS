package es.araujo.checktrans.repository;

import static org.junit.jupiter.api.Assertions.*;

import es.araujo.checktrans.domain.Checklist;
import es.araujo.checktrans.domain.enums.ChecklistStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ChecklistRepositoryTest {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Test
    void shouldSaveAndFindChecklist() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-001");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();

        Checklist saved = checklistRepository.save(checklist);

        assertNotNull(saved.getId());
        assertTrue(saved.getId() > 0);
    }

    @Test
    void shouldCheckCodeExistence() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-UNIQUE");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();
        checklistRepository.save(checklist);

        assertTrue(checklistRepository.existsByCode("CT-UNIQUE"));
        assertFalse(checklistRepository.existsByCode("NONEXISTENT"));
    }

    @Test
    void shouldFindByInspectorName() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-002");
        checklist.setInspectorName("Juan Perez");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();
        checklistRepository.save(checklist);

        List<Checklist> result = checklistRepository
                .findByInspectorNameContainingIgnoreCaseOrderByCheckDateDesc("juan");

        assertEquals(1, result.size());
        assertEquals("CT-002", result.get(0).getCode());
    }

    @Test
    void shouldFindByInspector() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-003");
        checklist.setInspectorName("Maria Lopez");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();
        checklistRepository.save(checklist);

        List<Checklist> result = checklistRepository
                .findByInspectorNameContainingIgnoreCaseOrderByCheckDateDesc("maria");

        assertEquals(1, result.size());
        assertEquals("CT-003", result.get(0).getCode());
    }
}
