package es.araujo.checktrans.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChecktransPropertiesTest {

    @Autowired
    private ChecktransProperties properties;

    @Test
    void shouldLoadDefaultAppProperties() {
        assertEquals("CHECKTRANS", properties.getApp().getName());
        assertEquals("1.0.0", properties.getApp().getVersion());
        assertEquals("es_ES", properties.getApp().getLocale());
        assertEquals("Europe/Madrid", properties.getApp().getTimezone());
    }

    @Test
    void shouldLoadDefaultPaginationProperties() {
        assertEquals(10, properties.getPagination().getDefaultPageSize());
        assertEquals(100, properties.getPagination().getMaxPageSize());
    }

    @Test
    void shouldLoadAuditPropertiesFromTestProfile() {
        assertFalse(properties.getAudit().isEnabled());
        assertTrue(properties.getAudit().isIncludeCreatedAt());
        assertTrue(properties.getAudit().isIncludeUpdatedAt());
    }

    @Test
    void shouldLoadDefaultChecklistProperties() {
        assertEquals("CT-", properties.getChecklist().getCodePrefix());
        assertEquals(50, properties.getChecklist().getMaxItems());
        assertEquals(2000, properties.getChecklist().getMaxObservationsLength());
        assertEquals("DRAFT, COMPLETED, APPROVED, REJECTED", properties.getChecklist().getAllowedStatus());
    }
}
