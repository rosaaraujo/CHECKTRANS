package es.araujo.checktrans.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class ChecklistTemplateServiceTest {

    @Mock
    private ChecklistTemplateRepository templateRepository;

    private ChecklistTemplateService templateService;

    @BeforeEach
    void setUp() {
        templateService = new ChecklistTemplateService(templateRepository);
    }

    @Test
    void shouldCreateTemplate() {
        ChecklistTemplateCreateDTO createDTO = new ChecklistTemplateCreateDTO();
        createDTO.setCode("TMP-001");
        createDTO.setName("Inspeccion General");
        createDTO.setDescription("Descripcion");

        when(templateRepository.existsByCode("TMP-001")).thenReturn(false);
        when(templateRepository.save(any(ChecklistTemplate.class))).thenAnswer(invocation -> {
            ChecklistTemplate saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ChecklistTemplateDTO result = templateService.create(createDTO);

        assertNotNull(result);
        assertEquals("TMP-001", result.getCode());
        assertEquals("Inspeccion General", result.getName());
        assertTrue(result.getActive());
        verify(templateRepository).save(any(ChecklistTemplate.class));
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnCreate() {
        ChecklistTemplateCreateDTO createDTO = new ChecklistTemplateCreateDTO();
        createDTO.setCode("TMP-001");

        when(templateRepository.existsByCode("TMP-001")).thenReturn(true);

        assertThrows(DuplicateCodeException.class, () -> templateService.create(createDTO));
        verify(templateRepository, never()).save(any());
    }

    @Test
    void shouldFindById() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");
        template.setName("Inspeccion General");
        template.setActive(true);

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));

        ChecklistTemplateDTO result = templateService.findById(1L);

        assertNotNull(result);
        assertEquals("TMP-001", result.getCode());
        assertEquals("Inspeccion General", result.getName());
        assertTrue(result.getActive());
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(templateRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> templateService.findById(999L));
    }

    @Test
    void shouldUpdateTemplate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");
        template.setName("Inspeccion General");
        template.setActive(true);

        ChecklistTemplateCreateDTO updateDTO = new ChecklistTemplateCreateDTO();
        updateDTO.setCode("TMP-002");
        updateDTO.setName("Inspeccion Detallada");
        updateDTO.setDescription("Nueva descripcion");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(templateRepository.existsByCode("TMP-002")).thenReturn(false);
        when(templateRepository.save(any(ChecklistTemplate.class))).thenReturn(template);

        ChecklistTemplateDTO result = templateService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("TMP-002", result.getCode());
        assertEquals("Inspeccion Detallada", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnUpdate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");
        template.setName("Original");

        ChecklistTemplateCreateDTO updateDTO = new ChecklistTemplateCreateDTO();
        updateDTO.setCode("TMP-002");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(templateRepository.existsByCode("TMP-002")).thenReturn(true);

        assertThrows(DuplicateCodeException.class, () -> templateService.update(1L, updateDTO));
        verify(templateRepository, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameCode() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");
        template.setName("Original");

        ChecklistTemplateCreateDTO updateDTO = new ChecklistTemplateCreateDTO();
        updateDTO.setCode("TMP-001");
        updateDTO.setName("Actualizado");
        updateDTO.setDescription("Desc");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(templateRepository.save(any(ChecklistTemplate.class))).thenReturn(template);

        ChecklistTemplateDTO result = templateService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("TMP-001", result.getCode());
        verify(templateRepository, never()).existsByCode(any());
    }

    @Test
    void shouldDeactivateTemplate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setActive(true);

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(templateRepository.save(any(ChecklistTemplate.class))).thenReturn(template);

        templateService.deactivate(1L);

        assertFalse(template.getActive());
        verify(templateRepository).save(template);
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNotFound() {
        when(templateRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> templateService.deactivate(999L));
    }

    @Test
    void shouldReturnPaginatedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ChecklistTemplate> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(templateRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ChecklistTemplateDTO> result = templateService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSearchByName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ChecklistTemplate> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(templateRepository.findByNameContainingIgnoreCase("inspeccion", pageable)).thenReturn(emptyPage);

        Page<ChecklistTemplateDTO> result = templateService.searchByName("inspeccion", pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
