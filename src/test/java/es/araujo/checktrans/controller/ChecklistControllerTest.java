package es.araujo.checktrans.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.araujo.checktrans.domain.enums.ChecklistStatus;
import es.araujo.checktrans.dto.ChecklistDTO;
import es.araujo.checktrans.service.ChecklistService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChecklistController.class)
class ChecklistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChecklistService checklistService;

    @Test
    void shouldShowListPage() throws Exception {
        Page<ChecklistDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(checklistService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/checklists"))
                .andExpect(status().isOk())
                .andExpect(view().name("checklist/list"))
                .andExpect(model().attributeExists("checklists"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/checklists/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("checklist/form"))
                .andExpect(model().attributeExists("checklist"));
    }

    @Test
    void shouldShowDetailPage() throws Exception {
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(1L);
        dto.setCode("CT-001");
        dto.setTransportPlate("1234ABC");
        dto.setTransportType("TRUCK");
        dto.setInspectorName("Inspector 1");
        dto.setCheckDate(LocalDateTime.now());
        dto.setStatus(ChecklistStatus.DRAFT);

        when(checklistService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/checklists/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("checklist/detail"))
                .andExpect(model().attributeExists("checklist"));
    }

    @Test
    void shouldRedirectAfterDelete() throws Exception {
        doNothing().when(checklistService).delete(1L);

        mockMvc.perform(post("/checklists/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/checklists"));
    }

    @Test
    void shouldRedirectAfterStatusUpdate() throws Exception {
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(1L);
        dto.setStatus(ChecklistStatus.COMPLETED);

        when(checklistService.updateStatus(1L, ChecklistStatus.COMPLETED)).thenReturn(dto);

        mockMvc.perform(post("/checklists/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/checklists/1"));
    }
}
