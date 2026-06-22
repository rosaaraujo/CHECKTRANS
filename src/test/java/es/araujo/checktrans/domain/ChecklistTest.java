package es.araujo.checktrans.domain;

import static org.junit.jupiter.api.Assertions.*;

import es.araujo.checktrans.domain.enums.ChecklistStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ChecklistTest {

    @Test
    void shouldCreateChecklistWithDefaultStatus() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-001");
        checklist.setTransportPlate("1234ABC");
        checklist.setTransportType("TRUCK");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());

        checklist.onCreate();

        assertEquals(ChecklistStatus.DRAFT, checklist.getStatus());
        assertNotNull(checklist.getCreatedAt());
        assertNotNull(checklist.getUpdatedAt());
    }

    @Test
    void shouldAddItemToChecklist() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-001");
        checklist.setTransportPlate("1234ABC");
        checklist.setTransportType("TRUCK");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());

        ChecklistItem item = new ChecklistItem();
        item.setDescription("Llantas en buen estado");
        item.setItemOrder(1);

        checklist.addItem(item);

        assertEquals(1, checklist.getItems().size());
        assertSame(checklist, item.getChecklist());
    }

    @Test
    void shouldRemoveItemFromChecklist() {
        Checklist checklist = new Checklist();
        checklist.setCode("CT-001");
        checklist.setTransportPlate("1234ABC");
        checklist.setTransportType("TRUCK");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());

        ChecklistItem item = new ChecklistItem();
        item.setDescription("Llantas en buen estado");
        item.setItemOrder(1);
        checklist.addItem(item);

        checklist.removeItem(item);

        assertTrue(checklist.getItems().isEmpty());
        assertNull(item.getChecklist());
    }

    @Test
    void shouldUpdateTimestamps() {
        Checklist checklist = new Checklist();
        checklist.onCreate();

        LocalDateTime createdAt = checklist.getCreatedAt();
        LocalDateTime updatedAt = checklist.getUpdatedAt();

        checklist.onUpdate();

        assertEquals(createdAt, checklist.getCreatedAt());
        assertTrue(checklist.getUpdatedAt().isAfter(updatedAt) || checklist.getUpdatedAt().equals(updatedAt));
    }
}
