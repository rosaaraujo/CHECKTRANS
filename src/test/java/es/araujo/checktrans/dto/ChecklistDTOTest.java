package es.araujo.checktrans.dto;

import static org.junit.jupiter.api.Assertions.*;

import es.araujo.checktrans.domain.enums.ChecklistStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ChecklistDTOTest {

    @Test
    void shouldSetAndGetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(1L);
        dto.setCode("CT-001");
        dto.setInspectorName("Inspector");
        dto.setCheckDate(now);
        dto.setStatus(ChecklistStatus.DRAFT);
        dto.setObservations("Observations");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        ChecklistItemDTO item = new ChecklistItemDTO();
        item.setDescription("Item 1");
        item.setItemOrder(1);
        dto.setItems(List.of(item));

        assertEquals(1L, dto.getId());
        assertEquals("CT-001", dto.getCode());
        assertEquals("Inspector", dto.getInspectorName());
        assertEquals(now, dto.getCheckDate());
        assertEquals(ChecklistStatus.DRAFT, dto.getStatus());
        assertEquals("Observations", dto.getObservations());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void shouldHandleNullFields() {
        ChecklistDTO dto = new ChecklistDTO();
        assertNull(dto.getId());
        assertNull(dto.getCode());
        assertNull(dto.getItems());
    }
}
