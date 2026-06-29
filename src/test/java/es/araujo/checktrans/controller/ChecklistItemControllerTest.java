package es.araujo.checktrans.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.araujo.checktrans.dto.ChecklistTemplateItemDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.service.ChecklistItemService;
import es.araujo.checktrans.service.ChecklistPhaseService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChecklistItemController.class)
class ChecklistItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChecklistItemService itemService;

    @MockitoBean
    private ChecklistPhaseService phaseService;

    @Test
    void shouldShowListPage() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);
        phaseDto.setName("Fase 1");

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);
        when(itemService.findByPhaseId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/templates/1/versions/1/phases/1/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("item/list"))
                .andExpect(model().attributeExists("phase"))
                .andExpect(model().attributeExists("items"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);

        mockMvc.perform(get("/templates/1/versions/1/phases/1/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("item/form"))
                .andExpect(model().attributeExists("phase"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("itemTypes"));
    }

    @Test
    void shouldCreateItem() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items")
                        .param("code", "ITEM-001")
                        .param("itemOrder", "1")
                        .param("description", "Verificar frenos")
                        .param("itemType", "YES_NO_NA")
                        .param("required", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases/1/items"));
    }

    @Test
    void shouldReturnFormOnValidationErrors() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items")
                        .param("code", "")
                        .param("itemOrder", "")
                        .param("description", "")
                        .param("itemType", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("item/form"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);

        ChecklistTemplateItemDTO itemDto = new ChecklistTemplateItemDTO();
        itemDto.setId(10L);
        itemDto.setPhaseId(1L);
        itemDto.setCode("ITEM-001");
        itemDto.setItemOrder(1);
        itemDto.setDescription("Verificar frenos");
        itemDto.setItemType("YES_NO_NA");
        itemDto.setRequired(true);

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);
        when(itemService.findById(1L, 10L)).thenReturn(itemDto);

        mockMvc.perform(get("/templates/1/versions/1/phases/1/items/10/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("item/form"))
                .andExpect(model().attributeExists("phase"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("itemId"))
                .andExpect(model().attributeExists("itemTypes"));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        mockMvc.perform(post("/templates/1/versions/1/phases/1/items/10/edit")
                        .param("code", "ITEM-002")
                        .param("itemOrder", "2")
                        .param("description", "Actualizado")
                        .param("itemType", "YES_NO_NA")
                        .param("required", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases/1/items"));
    }

    @Test
    void shouldReturnEditFormOnValidationErrors() throws Exception {
        ChecklistPhaseDTO phaseDto = new ChecklistPhaseDTO();
        phaseDto.setId(1L);
        phaseDto.setVersionId(1L);

        when(phaseService.findById(1L, 1L)).thenReturn(phaseDto);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items/10/edit")
                        .param("code", "")
                        .param("itemOrder", "")
                        .param("description", "")
                        .param("itemType", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("item/form"))
                .andExpect(model().attributeExists("itemId"));
    }

    @Test
    void shouldDeleteItem() throws Exception {
        doNothing().when(itemService).delete(1L, 10L);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items/10/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases/1/items"));
    }

    @Test
    void shouldMoveItemUp() throws Exception {
        doNothing().when(itemService).moveUp(1L, 10L);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items/10/moveUp"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases/1/items"));
    }

    @Test
    void shouldMoveItemDown() throws Exception {
        doNothing().when(itemService).moveDown(1L, 11L);

        mockMvc.perform(post("/templates/1/versions/1/phases/1/items/11/moveDown"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates/1/versions/1/phases/1/items"));
    }
}