package es.araujo.checktrans.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.araujo.checktrans.domain.template.ChecklistPhase;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import es.araujo.checktrans.dto.ChecklistPhaseCreateDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistPhaseRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChecklistPhaseServiceTest {

    @Mock
    private ChecklistPhaseRepository phaseRepository;

    @Mock
    private ChecklistTemplateVersionRepository versionRepository;

    private ChecklistPhaseService phaseService;

    @BeforeEach
    void setUp() {
        phaseService = new ChecklistPhaseService(phaseRepository, versionRepository);
    }

    @Test
    void shouldFindPhasesByVersionId() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);
        phase.setCode("FASE-001");
        phase.setPhaseOrder(1);
        phase.setName("Documentacion");

        when(versionRepository.existsById(1L)).thenReturn(true);
        when(phaseRepository.findByVersionIdOrderByPhaseOrderAsc(1L)).thenReturn(List.of(phase));

        List<ChecklistPhaseDTO> result = phaseService.findByVersionId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FASE-001", result.get(0).getCode());
        assertEquals(1, result.get(0).getPhaseOrder());
        assertEquals("Documentacion", result.get(0).getName());
    }

    @Test
    void shouldThrowExceptionWhenFindingPhasesForNonExistentVersion() {
        when(versionRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> phaseService.findByVersionId(999L));
    }

    @Test
    void shouldCreatePhase() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhaseCreateDTO createDTO = new ChecklistPhaseCreateDTO();
        createDTO.setCode("FASE-001");
        createDTO.setPhaseOrder(1);
        createDTO.setName("Documentacion");
        createDTO.setDescription("Verificacion");

        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(phaseRepository.existsByVersionIdAndCode(1L, "FASE-001")).thenReturn(false);

        ChecklistPhaseDTO result = phaseService.create(1L, createDTO);

        assertNotNull(result);
        assertEquals("FASE-001", result.getCode());
        assertEquals(1, result.getPhaseOrder());
        assertEquals("Documentacion", result.getName());
        verify(versionRepository).save(any(ChecklistTemplateVersion.class));
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnCreate() {
        when(versionRepository.findById(1L)).thenReturn(Optional.of(new ChecklistTemplateVersion()));
        when(phaseRepository.existsByVersionIdAndCode(1L, "FASE-001")).thenReturn(true);

        ChecklistPhaseCreateDTO createDTO = new ChecklistPhaseCreateDTO();
        createDTO.setCode("FASE-001");

        assertThrows(DuplicateCodeException.class, () -> phaseService.create(1L, createDTO));
        verify(versionRepository, never()).save(any());
    }

    @Test
    void shouldFindPhaseById() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);
        phase.setCode("FASE-001");

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(phase));

        ChecklistPhaseDTO result = phaseService.findById(1L, 10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("FASE-001", result.getCode());
    }

    @Test
    void shouldThrowExceptionWhenPhaseNotFound() {
        when(phaseRepository.findByVersionIdAndId(1L, 999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> phaseService.findById(1L, 999L));
    }

    @Test
    void shouldUpdatePhase() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);
        phase.setCode("FASE-001");
        phase.setPhaseOrder(1);
        phase.setName("Original");

        ChecklistPhaseCreateDTO updateDTO = new ChecklistPhaseCreateDTO();
        updateDTO.setCode("FASE-002");
        updateDTO.setPhaseOrder(2);
        updateDTO.setName("Actualizado");
        updateDTO.setDescription("Nueva descripcion");

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(phase));
        when(phaseRepository.existsByVersionIdAndCode(1L, "FASE-002")).thenReturn(false);
        when(phaseRepository.save(any(ChecklistPhase.class))).thenReturn(phase);

        ChecklistPhaseDTO result = phaseService.update(1L, 10L, updateDTO);

        assertNotNull(result);
        assertEquals("FASE-002", result.getCode());
        assertEquals(2, result.getPhaseOrder());
        assertEquals("Actualizado", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnUpdate() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);
        phase.setCode("FASE-001");

        ChecklistPhaseCreateDTO updateDTO = new ChecklistPhaseCreateDTO();
        updateDTO.setCode("FASE-002");

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(phase));
        when(phaseRepository.existsByVersionIdAndCode(1L, "FASE-002")).thenReturn(true);

        assertThrows(DuplicateCodeException.class, () -> phaseService.update(1L, 10L, updateDTO));
        verify(phaseRepository, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameCode() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);
        phase.setCode("FASE-001");
        phase.setName("Original");

        ChecklistPhaseCreateDTO updateDTO = new ChecklistPhaseCreateDTO();
        updateDTO.setCode("FASE-001");
        updateDTO.setPhaseOrder(1);
        updateDTO.setName("Actualizado");

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(phase));
        when(phaseRepository.save(any(ChecklistPhase.class))).thenReturn(phase);

        ChecklistPhaseDTO result = phaseService.update(1L, 10L, updateDTO);

        assertNotNull(result);
        assertEquals("FASE-001", result.getCode());
        assertEquals("Actualizado", result.getName());
        verify(phaseRepository, never()).existsByVersionIdAndCode(any(), any());
    }

    @Test
    void shouldDeletePhase() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(10L);
        phase.setVersion(version);

        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(phase));

        phaseService.delete(1L, 10L);

        assertFalse(version.getPhases().contains(phase));
        verify(versionRepository).save(version);
    }

    @Test
    void shouldMovePhaseUp() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase first = new ChecklistPhase();
        first.setId(10L);
        first.setVersion(version);
        first.setPhaseOrder(1);

        ChecklistPhase second = new ChecklistPhase();
        second.setId(11L);
        second.setVersion(version);
        second.setPhaseOrder(2);

        when(phaseRepository.findByVersionIdAndId(1L, 11L)).thenReturn(Optional.of(second));
        when(phaseRepository.findByVersionIdOrderByPhaseOrderAsc(1L))
                .thenReturn(List.of(first, second));

        phaseService.moveUp(1L, 11L);

        assertEquals(1, second.getPhaseOrder());
        assertEquals(2, first.getPhaseOrder());
        verify(phaseRepository).save(first);
        verify(phaseRepository).save(second);
    }

    @Test
    void shouldNotMoveFirstPhaseUp() {
        ChecklistPhase first = new ChecklistPhase();
        first.setId(10L);
        first.setPhaseOrder(1);

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(first));
        when(phaseRepository.findByVersionIdOrderByPhaseOrderAsc(1L))
                .thenReturn(List.of(first));

        phaseService.moveUp(1L, 10L);

        verify(phaseRepository, never()).save(any());
    }

    @Test
    void shouldMovePhaseDown() {
        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setId(1L);

        ChecklistPhase first = new ChecklistPhase();
        first.setId(10L);
        first.setVersion(version);
        first.setPhaseOrder(1);

        ChecklistPhase second = new ChecklistPhase();
        second.setId(11L);
        second.setVersion(version);
        second.setPhaseOrder(2);

        when(phaseRepository.findByVersionIdAndId(1L, 10L)).thenReturn(Optional.of(first));
        when(phaseRepository.findByVersionIdOrderByPhaseOrderAsc(1L))
                .thenReturn(List.of(first, second));

        phaseService.moveDown(1L, 10L);

        assertEquals(2, first.getPhaseOrder());
        assertEquals(1, second.getPhaseOrder());
        verify(phaseRepository).save(first);
        verify(phaseRepository).save(second);
    }

    @Test
    void shouldNotMoveLastPhaseDown() {
        ChecklistPhase last = new ChecklistPhase();
        last.setId(11L);
        last.setPhaseOrder(2);

        when(phaseRepository.findByVersionIdAndId(1L, 11L)).thenReturn(Optional.of(last));
        when(phaseRepository.findByVersionIdOrderByPhaseOrderAsc(1L))
                .thenReturn(List.of(new ChecklistPhase(), last));

        phaseService.moveDown(1L, 11L);

        verify(phaseRepository, never()).save(any());
    }
}
