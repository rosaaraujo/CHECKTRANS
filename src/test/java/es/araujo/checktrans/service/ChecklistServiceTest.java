package es.araujo.checktrans.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.araujo.checktrans.domain.Checklist;
import es.araujo.checktrans.domain.enums.ChecklistStatus;
import es.araujo.checktrans.dto.ChecklistCreateDTO;
import es.araujo.checktrans.dto.ChecklistDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.ChecklistRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateHeaderRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {

    @Mock
    private ChecklistRepository checklistRepository;

    @Mock
    private ChecklistTemplateRepository templateRepository;

    @Mock
    private ChecklistTemplateVersionRepository versionRepository;

    @Mock
    private ChecklistTemplateHeaderRepository headerRepository;

    private ChecklistService checklistService;

    @BeforeEach
    void setUp() {
        checklistService = new ChecklistService(checklistRepository, templateRepository, versionRepository, headerRepository);
    }

    @Test
    void shouldCreateChecklist() {
        ChecklistCreateDTO createDTO = new ChecklistCreateDTO();
        createDTO.setCode("CT-001");
        createDTO.setInspectorName("Inspector 1");
        createDTO.setCheckDate(LocalDateTime.now());

        when(checklistRepository.existsByCode("CT-001")).thenReturn(false);
        when(checklistRepository.save(any(Checklist.class))).thenAnswer(invocation -> {
            Checklist saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.onCreate();
            return saved;
        });

        ChecklistDTO result = checklistService.create(createDTO);

        assertNotNull(result);
        assertEquals("CT-001", result.getCode());
        assertEquals(ChecklistStatus.DRAFT, result.getStatus());
        verify(checklistRepository).save(any(Checklist.class));
    }

    @Test
    void shouldThrowExceptionWhenCodeExists() {
        ChecklistCreateDTO createDTO = new ChecklistCreateDTO();
        createDTO.setCode("CT-001");

        when(checklistRepository.existsByCode("CT-001")).thenReturn(true);

        assertThrows(DuplicateCodeException.class, () -> checklistService.create(createDTO));
        verify(checklistRepository, never()).save(any());
    }

    @Test
    void shouldFindChecklistById() {
        Checklist checklist = new Checklist();
        checklist.setId(1L);
        checklist.setCode("CT-001");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();

        when(checklistRepository.findById(1L)).thenReturn(Optional.of(checklist));

        ChecklistDTO result = checklistService.findById(1L);

        assertNotNull(result);
        assertEquals("CT-001", result.getCode());
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(checklistRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> checklistService.findById(999L));
    }

    @Test
    void shouldUpdateStatus() {
        Checklist checklist = new Checklist();
        checklist.setId(1L);
        checklist.setCode("CT-001");
        checklist.setInspectorName("Inspector 1");
        checklist.setCheckDate(LocalDateTime.now());
        checklist.setStatus(ChecklistStatus.DRAFT);
        checklist.onCreate();

        when(checklistRepository.findById(1L)).thenReturn(Optional.of(checklist));
        when(checklistRepository.save(any(Checklist.class))).thenReturn(checklist);

        ChecklistDTO result = checklistService.updateStatus(1L, ChecklistStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(ChecklistStatus.COMPLETED, result.getStatus());
    }

    @Test
    void shouldDeleteChecklist() {
        when(checklistRepository.existsById(1L)).thenReturn(true);

        checklistService.delete(1L);

        verify(checklistRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNotFound() {
        when(checklistRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> checklistService.delete(999L));
    }

    @Test
    void shouldReturnPaginatedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Checklist> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(checklistRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ChecklistDTO> result = checklistService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
