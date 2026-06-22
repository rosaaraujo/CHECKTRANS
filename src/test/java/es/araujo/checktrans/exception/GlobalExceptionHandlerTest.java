package es.araujo.checktrans.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@WebMvcTest(TestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn404ForResourceNotFound() throws Exception {
        mockMvc.perform(get("/test/resource/999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void shouldReturn409ForDuplicateCode() throws Exception {
        mockMvc.perform(get("/test/duplicate/CT-001"))
                .andExpect(status().isConflict())
                .andExpect(view().name("error/409"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void shouldReturn500ForGeneralError() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("details"));
    }
}

@Controller
class TestController {

    @GetMapping("/test/resource/{id}")
    public String throwNotFound(@PathVariable Long id) {
        throw new ResourceNotFoundException("Entity", id);
    }

    @GetMapping("/test/duplicate/{code}")
    public String throwDuplicate(@PathVariable String code) {
        throw new DuplicateCodeException(code);
    }

    @GetMapping("/test/error")
    public String throwGeneral() {
        throw new RuntimeException("Unexpected failure");
    }
}
