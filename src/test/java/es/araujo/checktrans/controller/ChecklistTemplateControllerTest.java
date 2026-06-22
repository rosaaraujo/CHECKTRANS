package es.araujo.checktrans.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.service.ChecklistTemplateService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChecklistTemplateController.class)
class ChecklistTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChecklistTemplateService templateService;

    @Test
    void shouldShowListPage() throws Exception {
        Page<ChecklistTemplateDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(templateService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(view().name("template/list"))
                .andExpect(model().attributeExists("templates"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/templates/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("template/form"))
                .andExpect(model().attributeExists("template"));
    }

    @Test
    void shouldCreateTemplate() throws Exception {
        ChecklistTemplateDTO dto = new ChecklistTemplateDTO();
        dto.setId(1L);
        dto.setCode("TMP-001");
        dto.setName("Inspeccion General");

        when(templateService.create(any(ChecklistTemplateCreateDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/templates")
                        .param("code", "TMP-001")
                        .param("name", "Inspeccion General")
                        .param("description", "Descripcion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1"));
    }

    @Test
    void shouldReturnFormOnValidationErrors() throws Exception {
        mockMvc.perform(post("/templates")
                        .param("code", "")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("template/form"));
    }

    @Test
    void shouldShowDetailPage() throws Exception {
        ChecklistTemplateDTO dto = new ChecklistTemplateDTO();
        dto.setId(1L);
        dto.setCode("TMP-001");
        dto.setName("Inspeccion General");

        when(templateService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/templates/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("template/detail"))
                .andExpect(model().attributeExists("template"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        ChecklistTemplateDTO dto = new ChecklistTemplateDTO();
        dto.setId(1L);
        dto.setCode("TMP-001");
        dto.setName("Inspeccion General");

        when(templateService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/templates/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("template/form"))
                .andExpect(model().attributeExists("template"))
                .andExpect(model().attributeExists("templateId"));
    }

    @Test
    void shouldUpdateTemplate() throws Exception {
        when(templateService.update(any(Long.class), any(ChecklistTemplateCreateDTO.class)))
                .thenReturn(new ChecklistTemplateDTO());

        mockMvc.perform(post("/templates/1/edit")
                        .param("code", "TMP-002")
                        .param("name", "Inspeccion Detallada")
                        .param("description", "Nueva descripcion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1"));
    }

    @Test
    void shouldReturnEditFormOnValidationErrors() throws Exception {
        mockMvc.perform(post("/templates/1/edit")
                        .param("code", "")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("template/form"))
                .andExpect(model().attributeExists("templateId"));
    }

    @Test
    void shouldDeactivateTemplate() throws Exception {
        doNothing().when(templateService).deactivate(1L);

        mockMvc.perform(post("/templates/1/deactivate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates"));
    }
}
