package es.araujo.checktrans.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistItemService {

    private static final Logger log = LoggerFactory.getLogger(ChecklistItemService.class);

    private final ChecklistTemplateItemRepository itemRepository;
    private final ChecklistPhaseRepository phaseRepository;

    public ChecklistItemService(ChecklistTemplateItemRepository itemRepository,
                                 ChecklistPhaseRepository phaseRepository) {
        this.itemRepository = itemRepository;
        this.phaseRepository = phaseRepository;
    }

    @Transactional(readOnly = true)
    public List<ChecklistTemplateItemDTO> findByPhaseId(Long phaseId) {
        if (!phaseRepository.existsById(phaseId)) {
            throw new ResourceNotFoundException("ChecklistPhase", phaseId);
        }
        return itemRepository.findByPhaseIdOrderByItemOrderAsc(phaseId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChecklistTemplateItemDTO findById(Long phaseId, Long itemId) {
        ChecklistItem item = itemRepository.findByPhaseIdAndId(phaseId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem", itemId));
        return toDTO(item);
    }

    public ChecklistTemplateItemDTO create(Long phaseId, ChecklistTemplateItemCreateDTO createDTO) {
        log.debug("Creating item in phase {} with code: {}", phaseId, createDTO.getCode());

        ChecklistPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        if (itemRepository.existsByPhaseIdAndCode(phaseId, createDTO.getCode())) {
            throw new DuplicateCodeException(createDTO.getCode());
        }

        ChecklistItem item = new ChecklistItem();
        item.setPhase(phase);
        item.setCode(createDTO.getCode());
        item.setItemOrder(createDTO.getItemOrder());
        item.setDescription(createDTO.getDescription());
        item.setItemType(ItemType.valueOf(createDTO.getItemType()));
        item.setRequired(createDTO.getRequired());

        itemRepository.save(item);
        log.info("Item {} created in phase {}", item.getCode(), phaseId);
        return toDTO(item);
    }

    public ChecklistTemplateItemDTO update(Long phaseId, Long itemId, ChecklistTemplateItemCreateDTO updateDTO) {
        log.debug("Updating item {} in phase {}", itemId, phaseId);

        ChecklistItem item = itemRepository.findByPhaseIdAndId(phaseId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem", itemId));

        if (!item.getCode().equals(updateDTO.getCode())
                && itemRepository.existsByPhaseIdAndCode(phaseId, updateDTO.getCode())) {
            throw new DuplicateCodeException(updateDTO.getCode());
        }

        item.setCode(updateDTO.getCode());
        item.setItemOrder(updateDTO.getItemOrder());
        item.setDescription(updateDTO.getDescription());
        item.setItemType(ItemType.valueOf(updateDTO.getItemType()));
        item.setRequired(updateDTO.getRequired());

        itemRepository.save(item);
        log.info("Item {} updated in phase {}", itemId, phaseId);
        return toDTO(item);
    }

    public void delete(Long phaseId, Long itemId) {
        log.debug("Deleting item {} from phase {}", itemId, phaseId);

        ChecklistPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        ChecklistItem item = itemRepository.findByPhaseIdAndId(phaseId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem", itemId));

        phase.removeItem(item);
        phaseRepository.save(phase);
        log.info("Item {} deleted from phase {}", itemId, phaseId);
    }

    public void moveUp(Long phaseId, Long itemId) {
        ChecklistItem current = itemRepository.findByPhaseIdAndId(phaseId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem", itemId));

        List<ChecklistItem> items = itemRepository
                .findByPhaseIdOrderByItemOrderAsc(phaseId);

        int idx = items.indexOf(current);
        if (idx <= 0) {
            return;
        }

        ChecklistItem previous = items.get(idx - 1);
        int tempOrder = current.getItemOrder();
        current.setItemOrder(previous.getItemOrder());
        previous.setItemOrder(tempOrder);

        itemRepository.save(current);
        itemRepository.save(previous);
        log.debug("Item {} moved up in phase {}", itemId, phaseId);
    }

    public void moveDown(Long phaseId, Long itemId) {
        ChecklistItem current = itemRepository.findByPhaseIdAndId(phaseId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem", itemId));

        List<ChecklistItem> items = itemRepository
                .findByPhaseIdOrderByItemOrderAsc(phaseId);

        int idx = items.indexOf(current);
        if (idx < 0 || idx >= items.size() - 1) {
            return;
        }

        ChecklistItem next = items.get(idx + 1);
        int tempOrder = current.getItemOrder();
        current.setItemOrder(next.getItemOrder());
        next.setItemOrder(tempOrder);

        itemRepository.save(current);
        itemRepository.save(next);
        log.debug("Item {} moved down in phase {}", itemId, phaseId);
    }

    private ChecklistTemplateItemDTO toDTO(ChecklistItem item) {
        ChecklistTemplateItemDTO dto = new ChecklistTemplateItemDTO();
        dto.setId(item.getId());
        dto.setPhaseId(item.getPhase().getId());
        dto.setCode(item.getCode());
        dto.setItemOrder(item.getItemOrder());
        dto.setDescription(item.getDescription());
        dto.setItemType(item.getItemType().name());
        dto.setRequired(item.getRequired());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}