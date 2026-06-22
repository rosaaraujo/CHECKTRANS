package es.araujo.checktrans.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.araujo.checktrans.dto.ChecklistPhaseCreateDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.dto.ChecklistTemplateVersionDTO;
import es.araujo.checktrans.service.ChecklistPhaseService;
import es.araujo.checktrans.service.ChecklistTemplateService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChecklistPhaseController.class)
class ChecklistPhaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChecklistPhaseService phaseService;

    @MockitoBean
    private ChecklistTemplateService templateService;

    @Test
    void shouldShowListPage() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);
        versionDto.setVersionNumber(1);

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);
        when(phaseService.findByVersionId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/templates/1/versions/1/phases"))
                .andExpect(status().isOk())
                .andExpect(view().name("phase/list"))
                .andExpect(model().attributeExists("version"))
                .andExpect(model().attributeExists("phases"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);

        mockMvc.perform(get("/templates/1/versions/1/phases/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("phase/form"))
                .andExpect(model().attributeExists("version"))
                .andExpect(model().attributeExists("phase"));
    }

    @Test
    void shouldCreatePhase() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);

        mockMvc.perform(post("/templates/1/versions/1/phases")
                        .param("code", "FASE-001")
                        .param("phaseOrder", "1")
                        .param("name", "Documentacion")
                        .param("description", "Verificacion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases"));
    }

    @Test
    void shouldReturnFormOnValidationErrors() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);

        mockMvc.perform(post("/templates/1/versions/1/phases")
                        .param("code", "")
                        .param("phaseOrder", "")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("phase/form"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);

        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(10L);
        phaseDto.setVersionId(1L);
        phaseDto.setCode("FASE-001");
        phaseDto.setPhaseOrder(1);
        phaseDto.setName("Documentacion");

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);
        when(phaseService.findById(1L, 10L)).thenReturn(phaseDto);

        mockMvc.perform(get("/templates/1/versions/1/phases/10/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("phase/form"))
                .andExpect(model().attributeExists("version"))
                .andExpect(model().attributeExists("phase"))
                .andExpect(model().attributeExists("phaseId"));
    }

    @Test
    void shouldUpdatePhase() throws Exception {
        mockMvc.perform(post("/templates/1/versions/1/phases/10/edit")
                        .param("code", "FASE-002")
                        .param("phaseOrder", "2")
                        .param("name", "Actualizado"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases"));
    }

    @Test
    void shouldReturnEditFormOnValidationErrors() throws Exception {
        ChecklistTemplateVersionDTO versionDto = new ChecklistTemplateVersionDTO();
        versionDto.setId(1L);
        versionDto.setTemplateId(1L);

        when(templateService.findVersionById(1L, 1L)).thenReturn(versionDto);

        mockMvc.perform(post("/templates/1/versions/1/phases/10/edit")
                        .param("code", "")
                        .param("phaseOrder", "")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("phase/form"))
                .andExpect(model().attributeExists("phaseId"));
    }

    @Test
    void shouldDeletePhase() throws Exception {
        doNothing().when(phaseService).delete(1L, 10L);

        mockMvc.perform(post("/templates/1/versions/1/phases/10/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases"));
    }

    @Test
    void shouldMovePhaseUp() throws Exception {
        doNothing().when(phaseService).moveUp(1L, 10L);

        mockMvc.perform(post("/templates/1/versions/1/phases/10/moveUp"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases"));
    }

    @Test
    void shouldMovePhaseDown() throws Exception {
        doNothing().when(phaseService).moveDown(1L, 11L);

        mockMvc.perform(post("/templates/1/versions/1/phases/11/moveDown"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases"));
    }
}
