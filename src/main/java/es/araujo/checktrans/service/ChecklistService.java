package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.Checklist;
import es.araujo.checktrans.domain.ChecklistItem;
import es.araujo.checktrans.domain.enums.ChecklistStatus;
import es.araujo.checktrans.dto.ChecklistCreateDTO;
import es.araujo.checktrans.dto.ChecklistDTO;
import es.araujo.checktrans.dto.ChecklistItemDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.ChecklistRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistService {

    private static final Logger log = LoggerFactory.getLogger(ChecklistService.class);

    private final ChecklistRepository checklistRepository;

    public ChecklistService(ChecklistRepository checklistRepository) {
        this.checklistRepository = checklistRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDTO> findAll(Pageable pageable) {
        log.debug("Finding all checklists with pagination: {}", pageable);
        return checklistRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ChecklistDTO findById(Long id) {
        log.debug("Finding checklist by id: {}", id);
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist", id));
        return toDTO(checklist);
    }

    public ChecklistDTO create(ChecklistCreateDTO createDTO) {
        log.debug("Creating checklist with code: {}", createDTO.getCode());

        if (checklistRepository.existsByCode(createDTO.getCode())) {
            throw new DuplicateCodeException(createDTO.getCode());
        }

        Checklist checklist = new Checklist();
        checklist.setCode(createDTO.getCode());
        checklist.setTransportPlate(createDTO.getTransportPlate());
        checklist.setTransportType(createDTO.getTransportType());
        checklist.setInspectorName(createDTO.getInspectorName());
        checklist.setCheckDate(createDTO.getCheckDate());
        checklist.setObservations(createDTO.getObservations());
        checklist.setStatus(ChecklistStatus.DRAFT);

        if (createDTO.getItems() != null) {
            for (ChecklistItemDTO itemDTO : createDTO.getItems()) {
                ChecklistItem item = new ChecklistItem();
                item.setDescription(itemDTO.getDescription());
                item.setIsPass(itemDTO.getIsPass());
                item.setObservations(itemDTO.getObservations());
                item.setItemOrder(itemDTO.getItemOrder());
                checklist.addItem(item);
            }
        }

        Checklist saved = checklistRepository.save(checklist);
        log.info("Checklist created with id: {}", saved.getId());
        return toDTO(saved);
    }

    public ChecklistDTO updateStatus(Long id, ChecklistStatus newStatus) {
        log.debug("Updating status for checklist {} to {}", id, newStatus);
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist", id));
        checklist.setStatus(newStatus);
        Checklist saved = checklistRepository.save(checklist);
        log.info("Checklist {} status updated to {}", id, newStatus);
        return toDTO(saved);
    }

    public void delete(Long id) {
        log.debug("Deleting checklist: {}", id);
        if (!checklistRepository.existsById(id)) {
            throw new ResourceNotFoundException("Checklist", id);
        }
        checklistRepository.deleteById(id);
        log.info("Checklist deleted: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDTO> searchByInspector(String inspectorName, Pageable pageable) {
        return checklistRepository
                .findByInspectorNameContainingIgnoreCase(inspectorName, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDTO> searchByPlate(String plate, Pageable pageable) {
        return checklistRepository
                .findByTransportPlateContainingIgnoreCase(plate, pageable)
                .map(this::toDTO);
    }

    private ChecklistDTO toDTO(Checklist checklist) {
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(checklist.getId());
        dto.setCode(checklist.getCode());
        dto.setTransportPlate(checklist.getTransportPlate());
        dto.setTransportType(checklist.getTransportType());
        dto.setInspectorName(checklist.getInspectorName());
        dto.setCheckDate(checklist.getCheckDate());
        dto.setStatus(checklist.getStatus());
        dto.setObservations(checklist.getObservations());
        dto.setCreatedAt(checklist.getCreatedAt());
        dto.setUpdatedAt(checklist.getUpdatedAt());

        if (checklist.getItems() != null) {
            dto.setItems(checklist.getItems().stream()
                    .map(this::toItemDTO)
                    .toList());
        }

        return dto;
    }

    private ChecklistItemDTO toItemDTO(ChecklistItem item) {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setIsPass(item.getIsPass());
        dto.setObservations(item.getObservations());
        dto.setItemOrder(item.getItemOrder());
        return dto;
    }
}
