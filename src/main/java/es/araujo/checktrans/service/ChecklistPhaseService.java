package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.template.ChecklistPhase;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import es.araujo.checktrans.dto.ChecklistPhaseCreateDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistPhaseRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistPhaseService {

    private static final Logger log = LoggerFactory.getLogger(ChecklistPhaseService.class);

    private final ChecklistPhaseRepository phaseRepository;
    private final ChecklistTemplateVersionRepository versionRepository;

    public ChecklistPhaseService(ChecklistPhaseRepository phaseRepository,
                                  ChecklistTemplateVersionRepository versionRepository) {
        this.phaseRepository = phaseRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional(readOnly = true)
    public List<ChecklistPhaseDTO> findByVersionId(Long versionId) {
        if (!versionRepository.existsById(versionId)) {
            throw new ResourceNotFoundException("ChecklistTemplateVersion", versionId);
        }
        return phaseRepository.findByVersionIdOrderByPhaseOrderAsc(versionId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChecklistPhaseDTO findById(Long versionId, Long phaseId) {
        ChecklistPhase phase = phaseRepository.findByVersionIdAndId(versionId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));
        return toDTO(phase);
    }

    public ChecklistPhaseDTO create(Long versionId, ChecklistPhaseCreateDTO createDTO) {
        log.debug("Creating phase in version {} with code: {}", versionId, createDTO.getCode());

        ChecklistTemplateVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplateVersion", versionId));

        if (phaseRepository.existsByVersionIdAndCode(versionId, createDTO.getCode())) {
            throw new DuplicateCodeException(createDTO.getCode());
        }

        ChecklistPhase phase = new ChecklistPhase();
        phase.setVersion(version);
        phase.setCode(createDTO.getCode());
        phase.setPhaseOrder(createDTO.getPhaseOrder());
        phase.setName(createDTO.getName());
        phase.setDescription(createDTO.getDescription());

        version.addPhase(phase);
        versionRepository.save(version);

        log.info("Phase {} created in version {}", phase.getCode(), versionId);
        return toDTO(phase);
    }

    public ChecklistPhaseDTO update(Long versionId, Long phaseId, ChecklistPhaseCreateDTO updateDTO) {
        log.debug("Updating phase {} in version {}", phaseId, versionId);

        ChecklistPhase phase = phaseRepository.findByVersionIdAndId(versionId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        if (!phase.getCode().equals(updateDTO.getCode())
                && phaseRepository.existsByVersionIdAndCode(versionId, updateDTO.getCode())) {
            throw new DuplicateCodeException(updateDTO.getCode());
        }

        phase.setCode(updateDTO.getCode());
        phase.setPhaseOrder(updateDTO.getPhaseOrder());
        phase.setName(updateDTO.getName());
        phase.setDescription(updateDTO.getDescription());

        phaseRepository.save(phase);
        log.info("Phase {} updated in version {}", phaseId, versionId);
        return toDTO(phase);
    }

    public void delete(Long versionId, Long phaseId) {
        log.debug("Deleting phase {} from version {}", phaseId, versionId);

        ChecklistTemplateVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplateVersion", versionId));

        ChecklistPhase phase = phaseRepository.findByVersionIdAndId(versionId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        version.removePhase(phase);
        versionRepository.save(version);
        log.info("Phase {} deleted from version {}", phaseId, versionId);
    }

    public void moveUp(Long versionId, Long phaseId) {
        ChecklistPhase current = phaseRepository.findByVersionIdAndId(versionId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        List<ChecklistPhase> phases = phaseRepository
                .findByVersionIdOrderByPhaseOrderAsc(versionId);

        int idx = phases.indexOf(current);
        if (idx <= 0) {
            return;
        }

        ChecklistPhase previous = phases.get(idx - 1);
        int tempOrder = current.getPhaseOrder();
        current.setPhaseOrder(previous.getPhaseOrder());
        previous.setPhaseOrder(tempOrder);

        phaseRepository.save(current);
        phaseRepository.save(previous);
        log.debug("Phase {} moved up in version {}", phaseId, versionId);
    }

    public void moveDown(Long versionId, Long phaseId) {
        ChecklistPhase current = phaseRepository.findByVersionIdAndId(versionId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistPhase", phaseId));

        List<ChecklistPhase> phases = phaseRepository
                .findByVersionIdOrderByPhaseOrderAsc(versionId);

        int idx = phases.indexOf(current);
        if (idx < 0 || idx >= phases.size() - 1) {
            return;
        }

        ChecklistPhase next = phases.get(idx + 1);
        int tempOrder = current.getPhaseOrder();
        current.setPhaseOrder(next.getPhaseOrder());
        next.setPhaseOrder(tempOrder);

        phaseRepository.save(current);
        phaseRepository.save(next);
        log.debug("Phase {} moved down in version {}", phaseId, versionId);
    }

    private ChecklistPhaseDTO toDTO(ChecklistPhase phase) {
        ChecklistPhaseDTO dto = new ChecklistPhaseDTO();
        dto.setId(phase.getId());
        dto.setVersionId(phase.getVersion().getId());
        dto.setCode(phase.getCode());
        dto.setPhaseOrder(phase.getPhaseOrder());
        dto.setName(phase.getName());
        dto.setDescription(phase.getDescription());
        dto.setCreatedAt(phase.getCreatedAt());
        dto.setUpdatedAt(phase.getUpdatedAt());
        return dto;
    }
}
