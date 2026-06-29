package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.Checklist;
import es.araujo.checktrans.domain.ChecklistHeaderValue;
import es.araujo.checktrans.domain.ChecklistItem;
import es.araujo.checktrans.domain.enums.ChecklistStatus;
import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.domain.template.ChecklistTemplateHeader;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import es.araujo.checktrans.dto.ChecklistCreateDTO;
import es.araujo.checktrans.dto.ChecklistDTO;
import es.araujo.checktrans.dto.ChecklistHeaderValueDTO;
import es.araujo.checktrans.dto.ChecklistItemDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.ChecklistRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateHeaderRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
import java.util.ArrayList;
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
    private final ChecklistTemplateRepository templateRepository;
    private final ChecklistTemplateVersionRepository versionRepository;
    private final ChecklistTemplateHeaderRepository headerRepository;

    public ChecklistService(ChecklistRepository checklistRepository,
                            ChecklistTemplateRepository templateRepository,
                            ChecklistTemplateVersionRepository versionRepository,
                            ChecklistTemplateHeaderRepository headerRepository) {
        this.checklistRepository = checklistRepository;
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
        this.headerRepository = headerRepository;
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
        checklist.setInspectorName(createDTO.getInspectorName());
        checklist.setCheckDate(createDTO.getCheckDate());
        checklist.setObservations(createDTO.getObservations());
        checklist.setStatus(ChecklistStatus.DRAFT);

        if (createDTO.getTemplateId() != null) {
            ChecklistTemplate template = templateRepository.findById(createDTO.getTemplateId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plantilla", createDTO.getTemplateId()));
            checklist.setTemplateId(template.getId());
            checklist.setTemplateName(template.getName());

            ChecklistTemplateVersion activeVersion = versionRepository
                    .findTopByTemplateIdOrderByVersionNumberDesc(template.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Versi\u00f3n activa no encontrada"));

            int globalOrder = 1;
            for (es.araujo.checktrans.domain.template.ChecklistPhase phase
                    : activeVersion.getPhases()) {
                for (es.araujo.checktrans.domain.template.ChecklistItem templateItem : phase.getItems()) {
                    ChecklistItem item = new ChecklistItem();
                    item.setCode(templateItem.getCode());
                    item.setDescription(templateItem.getDescription());
                    item.setItemType(templateItem.getItemType().name());
                    item.setRequired(templateItem.getRequired());
                    item.setItemOrder(globalOrder++);
                    item.setPhaseOrder(phase.getPhaseOrder());
                    item.setPhaseName(phase.getName());
                    checklist.addItem(item);
                }
            }

            List<ChecklistTemplateHeader> headers = headerRepository
                    .findByTemplateIdOrderByHeaderOrderAsc(template.getId());
            for (ChecklistTemplateHeader header : headers) {
                ChecklistHeaderValue headerValue = new ChecklistHeaderValue();
                headerValue.setHeaderId(header.getId());
                headerValue.setHeaderCode(header.getCode());
                headerValue.setHeaderLabel(header.getLabel());
                headerValue.setHeaderType(header.getHeaderType().name());
                headerValue.setHeaderOrder(header.getHeaderOrder());
                headerValue.setValue(null);
                checklist.addHeaderValue(headerValue);
            }
        }
        if (createDTO.getItems() != null) {
            for (ChecklistItemDTO itemDTO : createDTO.getItems()) {
                ChecklistItem item = new ChecklistItem();
                item.setDescription(itemDTO.getDescription());
                item.setIsPass(itemDTO.getIsPass());
                item.setObservations(itemDTO.getObservations());
                item.setItemOrder(itemDTO.getItemOrder());
                item.setPhaseOrder(0);
                item.setPhaseName("");
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

    private ChecklistDTO toDTO(Checklist checklist) {
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(checklist.getId());
        dto.setCode(checklist.getCode());
        dto.setInspectorName(checklist.getInspectorName());
        dto.setCheckDate(checklist.getCheckDate());
        dto.setStatus(checklist.getStatus());
        dto.setObservations(checklist.getObservations());
        dto.setCreatedAt(checklist.getCreatedAt());
        dto.setUpdatedAt(checklist.getUpdatedAt());
        dto.setTemplateId(checklist.getTemplateId());
        dto.setTemplateName(checklist.getTemplateName());

        if (checklist.getItems() != null) {
            dto.setItems(checklist.getItems().stream()
                    .map(this::toItemDTO)
                    .toList());
        }

        if (checklist.getHeaderValues() != null) {
            dto.setHeaderValues(checklist.getHeaderValues().stream()
                    .map(this::toHeaderValueDTO)
                    .toList());
        }

        return dto;
    }

    private ChecklistItemDTO toItemDTO(ChecklistItem item) {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        dto.setId(item.getId());
        dto.setCode(item.getCode());
        dto.setDescription(item.getDescription());
        dto.setIsPass(item.getIsPass());
        dto.setObservations(item.getObservations());
        dto.setItemOrder(item.getItemOrder());
        dto.setItemType(item.getItemType());
        dto.setRequired(item.getRequired());
        dto.setPhaseOrder(item.getPhaseOrder());
        dto.setPhaseName(item.getPhaseName());
        return dto;
    }

    private ChecklistHeaderValueDTO toHeaderValueDTO(ChecklistHeaderValue headerValue) {
        ChecklistHeaderValueDTO dto = new ChecklistHeaderValueDTO();
        dto.setId(headerValue.getId());
        dto.setHeaderId(headerValue.getHeaderId());
        dto.setHeaderCode(headerValue.getHeaderCode());
        dto.setHeaderLabel(headerValue.getHeaderLabel());
        dto.setHeaderType(headerValue.getHeaderType());
        dto.setValue(headerValue.getValue());
        dto.setHeaderOrder(headerValue.getHeaderOrder());
        return dto;
    }
}
