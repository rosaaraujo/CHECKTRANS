package es.araujo.checktrans.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateWithMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Custom message");
        assertEquals("Custom message", ex.getMessage());
    }

    @Test
    void shouldCreateWithResourceAndId() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Checklist", 42L);
        assertEquals("Checklist not found with id: 42", ex.getMessage());
    }
}
