package es.araujo.checktrans.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.araujo.checktrans.config.ChecktransProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChecktransProperties checktransProperties;

    @Test
    void shouldReturnIndexPage() throws Exception {
        ChecktransProperties.App app = new ChecktransProperties.App();
        app.setName("CHECKTRANS");
        app.setVersion("1.0.0");
        org.mockito.Mockito.when(checktransProperties.getApp()).thenReturn(app);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("appName"))
                .andExpect(model().attribute("appName", "CHECKTRANS"))
                .andExpect(model().attributeExists("appVersion"))
                .andExpect(model().attribute("appVersion", "1.0.0"));
    }
}
