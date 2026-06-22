package es.araujo.checktrans.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChecklistItemTest {

    @Test
    void shouldCreateChecklistItem() {
        ChecklistItem item = new ChecklistItem();
        item.setDescription("Frenos en buen estado");
        item.setIsPass(true);
        item.setItemOrder(1);

        assertEquals("Frenos en buen estado", item.getDescription());
        assertTrue(item.getIsPass());
        assertEquals(1, item.getItemOrder());
    }

    @Test
    void shouldAllowNullResult() {
        ChecklistItem item = new ChecklistItem();
        item.setDescription("Luz de giro");
        item.setItemOrder(2);

        assertNull(item.getIsPass());
    }
}
