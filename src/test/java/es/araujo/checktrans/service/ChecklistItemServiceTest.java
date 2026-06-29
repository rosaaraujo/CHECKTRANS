package es.araujo.checktrans.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.araujo.checktrans.domain.enums.ItemType;
import es.araujo.checktrans.domain.template.ChecklistItem;
import es.araujo.checktrans.domain.template.ChecklistPhase;
import es.araujo.checktrans.dto.ChecklistTemplateItemCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateItemDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistPhaseRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateItemRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChecklistItemServiceTest {

    @Mock
    private ChecklistTemplateItemRepository itemRepository;

    @Mock
    private ChecklistPhaseRepository phaseRepository;

    private ChecklistItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new ChecklistItemService(itemRepository, phaseRepository);
    }

    @Test
    void shouldFindItemsByPhaseId() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);
        item.setCode("ITEM-001");
        item.setItemOrder(1);
        item.setDescription("Verificar frenos");
        item.setItemType(ItemType.YES_NO_NA);
        item.setRequired(true);

        when(phaseRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.findByPhaseIdOrderByItemOrderAsc(1L)).thenReturn(List.of(item));

        List<ChecklistTemplateItemDTO> result = itemService.findByPhaseId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ITEM-001", result.get(0).getCode());
        assertEquals(1, result.get(0).getItemOrder());
        assertEquals("Verificar frenos", result.get(0).getDescription());
        assertEquals("YES_NO_NA", result.get(0).getItemType());
        assertTrue(result.get(0).getRequired());
    }

    @Test
    void shouldThrowExceptionWhenFindingItemsForNonExistentPhase() {
        when(phaseRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> itemService.findByPhaseId(999L));
    }

    @Test
    void shouldCreateItem() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistTemplateItemCreateDTO createDTO = new ChecklistTemplateItemCreateDTO();
        createDTO.setCode("ITEM-001");
        createDTO.setItemOrder(1);
        createDTO.setDescription("Verificar frenos");
        createDTO.setItemType("YES_NO_NA");
        createDTO.setRequired(true);

        when(phaseRepository.findById(1L)).thenReturn(Optional.of(phase));
        when(itemRepository.existsByPhaseIdAndCode(1L, "ITEM-001")).thenReturn(false);

        ChecklistTemplateItemDTO result = itemService.create(1L, createDTO);

        assertNotNull(result);
        assertEquals("ITEM-001", result.getCode());
        assertEquals(1, result.getItemOrder());
        assertEquals("Verificar frenos", result.getDescription());
        assertEquals("YES_NO_NA", result.getItemType());
        assertTrue(result.getRequired());
        verify(itemRepository).save(any(ChecklistItem.class));
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnCreate() {
        when(phaseRepository.findById(1L)).thenReturn(Optional.of(new ChecklistPhase()));
        when(itemRepository.existsByPhaseIdAndCode(1L, "ITEM-001")).thenReturn(true);

        ChecklistTemplateItemCreateDTO createDTO = new ChecklistTemplateItemCreateDTO();
        createDTO.setCode("ITEM-001");

        assertThrows(DuplicateCodeException.class, () -> itemService.create(1L, createDTO));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void shouldFindItemById() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);
        item.setCode("ITEM-001");
        item.setItemType(ItemType.YES_NO_NA);

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(item));

        ChecklistTemplateItemDTO result = itemService.findById(1L, 10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("ITEM-001", result.getCode());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        when(itemRepository.findByPhaseIdAndId(1L, 999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> itemService.findById(1L, 999L));
    }

    @Test
    void shouldUpdateItem() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);
        item.setCode("ITEM-001");
        item.setItemOrder(1);
        item.setDescription("Original");
        item.setItemType(ItemType.YES_NO_NA);
        item.setRequired(true);

        ChecklistTemplateItemCreateDTO updateDTO = new ChecklistTemplateItemCreateDTO();
        updateDTO.setCode("ITEM-002");
        updateDTO.setItemOrder(2);
        updateDTO.setDescription("Actualizado");
        updateDTO.setItemType("YES_NO_NA");
        updateDTO.setRequired(false);

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(item));
        when(itemRepository.existsByPhaseIdAndCode(1L, "ITEM-002")).thenReturn(false);
        when(itemRepository.save(any(ChecklistItem.class))).thenReturn(item);

        ChecklistTemplateItemDTO result = itemService.update(1L, 10L, updateDTO);

        assertNotNull(result);
        assertEquals("ITEM-002", result.getCode());
        assertEquals(2, result.getItemOrder());
        assertEquals("Actualizado", result.getDescription());
        assertFalse(result.getRequired());
    }

    @Test
    void shouldThrowExceptionWhenCodeExistsOnUpdate() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);
        item.setCode("ITEM-001");

        ChecklistTemplateItemCreateDTO updateDTO = new ChecklistTemplateItemCreateDTO();
        updateDTO.setCode("ITEM-002");

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(item));
        when(itemRepository.existsByPhaseIdAndCode(1L, "ITEM-002")).thenReturn(true);

        assertThrows(DuplicateCodeException.class, () -> itemService.update(1L, 10L, updateDTO));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameCode() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);
        item.setCode("ITEM-001");
        item.setDescription("Original");
        item.setItemType(ItemType.YES_NO_NA);
        item.setRequired(true);

        ChecklistTemplateItemCreateDTO updateDTO = new ChecklistTemplateItemCreateDTO();
        updateDTO.setCode("ITEM-001");
        updateDTO.setItemOrder(1);
        updateDTO.setDescription("Actualizado");
        updateDTO.setItemType("YES_NO_NA");
        updateDTO.setRequired(true);

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(ChecklistItem.class))).thenReturn(item);

        ChecklistTemplateItemDTO result = itemService.update(1L, 10L, updateDTO);

        assertNotNull(result);
        assertEquals("ITEM-001", result.getCode());
        assertEquals("Actualizado", result.getDescription());
        verify(itemRepository, never()).existsByPhaseIdAndCode(any(), any());
    }

    @Test
    void shouldDeleteItem() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem item = new ChecklistItem();
        item.setId(10L);
        item.setPhase(phase);

        when(phaseRepository.findById(1L)).thenReturn(Optional.of(phase));
        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(item));

        itemService.delete(1L, 10L);

        assertFalse(phase.getItems().contains(item));
        verify(phaseRepository).save(phase);
    }

    @Test
    void shouldMoveItemUp() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem first = new ChecklistItem();
        first.setId(10L);
        first.setPhase(phase);
        first.setItemOrder(1);

        ChecklistItem second = new ChecklistItem();
        second.setId(11L);
        second.setPhase(phase);
        second.setItemOrder(2);

        when(itemRepository.findByPhaseIdAndId(1L, 11L)).thenReturn(Optional.of(second));
        when(itemRepository.findByPhaseIdOrderByItemOrderAsc(1L))
                .thenReturn(List.of(first, second));

        itemService.moveUp(1L, 11L);

        assertEquals(1, second.getItemOrder());
        assertEquals(2, first.getItemOrder());
        verify(itemRepository).save(first);
        verify(itemRepository).save(second);
    }

    @Test
    void shouldNotMoveFirstItemUp() {
        ChecklistItem first = new ChecklistItem();
        first.setId(10L);
        first.setItemOrder(1);

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(first));
        when(itemRepository.findByPhaseIdOrderByItemOrderAsc(1L))
                .thenReturn(List.of(first));

        itemService.moveUp(1L, 10L);

        verify(itemRepository, never()).save(any());
    }

    @Test
    void shouldMoveItemDown() {
        ChecklistPhase phase = new ChecklistPhase();
        phase.setId(1L);

        ChecklistItem first = new ChecklistItem();
        first.setId(10L);
        first.setPhase(phase);
        first.setItemOrder(1);

        ChecklistItem second = new ChecklistItem();
        second.setId(11L);
        second.setPhase(phase);
        second.setItemOrder(2);

        when(itemRepository.findByPhaseIdAndId(1L, 10L)).thenReturn(Optional.of(first));
        when(itemRepository.findByPhaseIdOrderByItemOrderAsc(1L))
                .thenReturn(List.of(first, second));

        itemService.moveDown(1L, 10L);

        assertEquals(2, first.getItemOrder());
        assertEquals(1, second.getItemOrder());
        verify(itemRepository).save(first);
        verify(itemRepository).save(second);
    }

    @Test
    void shouldNotMoveLastItemDown() {
        ChecklistItem last = new ChecklistItem();
        last.setId(11L);
        last.setItemOrder(2);

        when(itemRepository.findByPhaseIdAndId(1L, 11L)).thenReturn(Optional.of(last));
        when(itemRepository.findByPhaseIdOrderByItemOrderAsc(1L))
                .thenReturn(List.of(new ChecklistItem(), last));

        itemService.moveDown(1L, 11L);

        verify(itemRepository, never()).save(any());
    }
}