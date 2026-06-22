package es.araujo.checktrans.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChecklistItemDTOTest {

    @Test
    void shouldSetAndGetAllFields() {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        dto.setId(1L);
        dto.setDescription("Check brakes");
        dto.setIsPass(true);
        dto.setObservations("All good");
        dto.setItemOrder(1);

        assertEquals(1L, dto.getId());
        assertEquals("Check brakes", dto.getDescription());
        assertTrue(dto.getIsPass());
        assertEquals("All good", dto.getObservations());
        assertEquals(1, dto.getItemOrder());
    }

    @Test
    void shouldHandleNullFields() {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        assertNull(dto.getId());
        assertNull(dto.getDescription());
        assertNull(dto.getIsPass());
        assertNull(dto.getItemOrder());
    }

    @Test
    void shouldAllowNullIsPass() {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        dto.setIsPass(null);
        assertNull(dto.getIsPass());
    }
}
