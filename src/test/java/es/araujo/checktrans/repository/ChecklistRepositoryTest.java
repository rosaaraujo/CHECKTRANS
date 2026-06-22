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
        checklist.setTransportPlate("1234ABC");
        checklist.setTransportType("TRUCK");
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
        checklist.setTransportPlate("1234ABC");
        checklist.setTransportType("TRUCK");
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
        checklist.setTransportPlate("5678DEF");
        checklist.setTransportType("VAN");
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
    void shouldFindByPlate() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-003");
        checklist.setTransportPlate("9999ZZZ");
        checklist.setTransportType("TRAILER");
        checklist.setInspectorName("Maria Lopez");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();
        checklistRepository.save(checklist);

        List<Checklist> result = checklistRepository
                .findByTransportPlateContainingIgnoreCaseOrderByCheckDateDesc("9999");

        assertEquals(1, result.size());
        assertEquals("CT-003", result.get(0).getCode());
    }
}
