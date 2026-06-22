package es.araujo.checktrans.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ChecklistCreateDTOTest {

    @Test
    void shouldSetAndGetFields() {
        LocalDateTime now = LocalDateTime.now();
        ChecklistCreateDTO dto = new ChecklistCreateDTO();
        dto.setCode("CT-001");
        dto.setTransportPlate("1234ABC");
        dto.setTransportType("TRUCK");
        dto.setInspectorName("Inspector");
        dto.setCheckDate(now);
        dto.setObservations("Some notes");

        assertEquals("CT-001", dto.getCode());
        assertEquals("1234ABC", dto.getTransportPlate());
        assertEquals("TRUCK", dto.getTransportType());
        assertEquals("Inspector", dto.getInspectorName());
        assertEquals(now, dto.getCheckDate());
        assertEquals("Some notes", dto.getObservations());
    }

    @Test
    void shouldDefaultItemsToEmptyList() {
        ChecklistCreateDTO dto = new ChecklistCreateDTO();
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    void shouldSetItems() {
        ChecklistCreateDTO dto = new ChecklistCreateDTO();

        ChecklistItemDTO item = new ChecklistItemDTO();
        item.setDescription("Test item");
        item.setItemOrder(1);
        dto.setItems(List.of(item));

        assertEquals(1, dto.getItems().size());
        assertEquals("Test item", dto.getItems().get(0).getDescription());
    }
}
