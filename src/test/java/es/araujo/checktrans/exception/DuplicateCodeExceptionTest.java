package es.araujo.checktrans.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DuplicateCodeExceptionTest {

    @Test
    void shouldCreateWithCode() {
        DuplicateCodeException ex = new DuplicateCodeException("CT-001");
        assertTrue(ex.getMessage().contains("CT-001"));
        assertTrue(ex.getMessage().contains("already exists"));
    }
}
