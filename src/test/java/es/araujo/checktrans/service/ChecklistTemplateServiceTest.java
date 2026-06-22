package es.araujo.checktrans.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateVersionDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
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

    @Mock
    private ChecklistTemplateVersionRepository versionRepository;

    private ChecklistTemplateService templateService;

    @BeforeEach
    void setUp() {
        templateService = new ChecklistTemplateService(templateRepository, versionRepository);
    }

    @Test
    void shouldCreateTemplateWithInitialVersion() {
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
        assertEquals(1, result.getCurrentVersionNumber());
        assertNotNull(result.getCurrentVersionDate());
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
    void shouldUpdateTemplateAndCreateNewVersion() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");
        template.setName("Inspeccion General");
        template.setActive(true);

        ChecklistTemplateVersion existingVersion = new ChecklistTemplateVersion();
        existingVersion.setVersionNumber(1);
        existingVersion.setActiveVersion(true);
        template.addVersion(existingVersion);

        ChecklistTemplateCreateDTO updateDTO = new ChecklistTemplateCreateDTO();
        updateDTO.setCode("TMP-002");
        updateDTO.setName("Inspeccion Detallada");
        updateDTO.setDescription("Nueva descripcion");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(templateRepository.existsByCode("TMP-002")).thenReturn(false);
        when(versionRepository.findTopByTemplateIdOrderByVersionNumberDesc(1L))
                .thenReturn(Optional.of(existingVersion));
        when(templateRepository.save(any(ChecklistTemplate.class))).thenReturn(template);

        ChecklistTemplateDTO result = templateService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("TMP-002", result.getCode());
        assertEquals("Inspeccion Detallada", result.getName());
        assertEquals(2, result.getCurrentVersionNumber());
        verify(templateRepository).save(any(ChecklistTemplate.class));
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

        ChecklistTemplateVersion existingVersion = new ChecklistTemplateVersion();
        existingVersion.setVersionNumber(1);
        existingVersion.setActiveVersion(true);
        template.addVersion(existingVersion);

        ChecklistTemplateCreateDTO updateDTO = new ChecklistTemplateCreateDTO();
        updateDTO.setCode("TMP-001");
        updateDTO.setName("Actualizado");
        updateDTO.setDescription("Desc");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(versionRepository.findTopByTemplateIdOrderByVersionNumberDesc(1L))
                .thenReturn(Optional.of(existingVersion));
        when(templateRepository.save(any(ChecklistTemplate.class))).thenReturn(template);

        ChecklistTemplateDTO result = templateService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("TMP-001", result.getCode());
        assertEquals(2, result.getCurrentVersionNumber());
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

    @Test
    void shouldFindVersionsByTemplateId() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");

        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(10L);
        version.setTemplate(template);
        version.setVersionNumber(1);
        version.setActiveVersion(true);

        when(templateRepository.existsById(1L)).thenReturn(true);
        when(versionRepository.findByTemplateIdOrderByVersionNumberDesc(1L))
                .thenReturn(List.of(version));

        List<ChecklistTemplateVersionDTO> result = templateService.findVersionsByTemplateId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals(1, result.get(0).getVersionNumber());
        assertTrue(result.get(0).getActiveVersion());
    }

    @Test
    void shouldThrowExceptionWhenFindingVersionsForNonExistentTemplate() {
        when(templateRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> templateService.findVersionsByTemplateId(999L));
    }

    @Test
    void shouldFindVersionById() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);
        template.setCode("TMP-001");

        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(10L);
        version.setTemplate(template);
        version.setVersionNumber(2);

        when(templateRepository.existsById(1L)).thenReturn(true);
        when(versionRepository.findById(10L)).thenReturn(Optional.of(version));

        ChecklistTemplateVersionDTO result = templateService.findVersionById(1L, 10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(2, result.getVersionNumber());
    }

    @Test
    void shouldThrowExceptionWhenVersionNotFound() {
        when(templateRepository.existsById(1L)).thenReturn(true);
        when(versionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> templateService.findVersionById(1L, 999L));
    }

    @Test
    void shouldThrowExceptionWhenVersionNotBelongingToTemplate() {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setId(1L);

        ChecklistTemplate otherTemplate = new ChecklistTemplate();
        otherTemplate.setId(2L);

        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(10L);
        version.setTemplate(otherTemplate);

        when(templateRepository.existsById(1L)).thenReturn(true);
        when(versionRepository.findById(10L)).thenReturn(Optional.of(version));

        assertThrows(ResourceNotFoundException.class,
                () -> templateService.findVersionById(1L, 10L));
    }
}
