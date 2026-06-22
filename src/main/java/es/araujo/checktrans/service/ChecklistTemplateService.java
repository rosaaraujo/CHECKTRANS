package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.enums.ChecklistExecutionStatus;
import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.domain.template.ChecklistTemplateVersion;
import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateVersionDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateVersionRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistTemplateService {

    private static final Logger log = LoggerFactory.getLogger(ChecklistTemplateService.class);

    private final ChecklistTemplateRepository templateRepository;
    private final ChecklistTemplateVersionRepository versionRepository;

    public ChecklistTemplateService(ChecklistTemplateRepository templateRepository,
                                    ChecklistTemplateVersionRepository versionRepository) {
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChecklistTemplateDTO> findAll(Pageable pageable) {
        log.debug("Finding all templates with pagination: {}", pageable);
        return templateRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ChecklistTemplateDTO findById(Long id) {
        log.debug("Finding template by id: {}", id);
        ChecklistTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", id));
        return toDTO(template);
    }

    public ChecklistTemplateDTO create(ChecklistTemplateCreateDTO createDTO) {
        log.debug("Creating template with code: {}", createDTO.getCode());

        if (templateRepository.existsByCode(createDTO.getCode())) {
            throw new DuplicateCodeException(createDTO.getCode());
        }

        ChecklistTemplate template = new ChecklistTemplate();
        template.setCode(createDTO.getCode());
        template.setName(createDTO.getName());
        template.setDescription(createDTO.getDescription());
        template.setActive(true);

        ChecklistTemplateVersion version = new ChecklistTemplateVersion();
        version.setVersionNumber(1);
        version.setActiveVersion(true);
        version.setStatus(ChecklistExecutionStatus.CREADA);
        version.setPublicationDate(LocalDate.now());

        template.addVersion(version);
        template = templateRepository.save(template);

        ChecklistTemplateDTO dto = toDTO(template);
        dto.setCurrentVersionNumber(1);
        dto.setCurrentVersionDate(version.getPublicationDate());
        log.info("Template created with id: {}, initial version 1", template.getId());
        return dto;
    }

    public ChecklistTemplateDTO update(Long id, ChecklistTemplateCreateDTO updateDTO) {
        log.debug("Updating template: {}", id);

        ChecklistTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", id));

        if (!template.getCode().equals(updateDTO.getCode())
                && templateRepository.existsByCode(updateDTO.getCode())) {
            throw new DuplicateCodeException(updateDTO.getCode());
        }

        template.setCode(updateDTO.getCode());
        template.setName(updateDTO.getName());
        template.setDescription(updateDTO.getDescription());

        Integer nextVersionNumber = versionRepository
                .findTopByTemplateIdOrderByVersionNumberDesc(id)
                .map(v -> v.getVersionNumber() + 1)
                .orElse(1);

        ChecklistTemplateVersion newVersion = new ChecklistTemplateVersion();
        newVersion.setVersionNumber(nextVersionNumber);
        newVersion.setActiveVersion(true);
        newVersion.setStatus(ChecklistExecutionStatus.CREADA);
        newVersion.setPublicationDate(LocalDate.now());

        template.addVersion(newVersion);

        List<ChecklistTemplateVersion> allVersions = template.getVersions();
        for (ChecklistTemplateVersion v : allVersions) {
            if (!v.equals(newVersion)) {
                v.setActiveVersion(false);
            }
        }

        templateRepository.save(template);
        log.info("Template {} updated, version {} created", id, nextVersionNumber);
        return toDTO(template);
    }

    public void deactivate(Long id) {
        log.debug("Deactivating template: {}", id);
        ChecklistTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", id));
        template.setActive(false);
        templateRepository.save(template);
        log.info("Template {} deactivated", id);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistTemplateDTO> searchByName(String name, Pageable pageable) {
        return templateRepository
                .findByNameContainingIgnoreCase(name, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ChecklistTemplateVersionDTO> findVersionsByTemplateId(Long templateId) {
        if (!templateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("ChecklistTemplate", templateId);
        }
        return versionRepository
                .findByTemplateIdOrderByVersionNumberDesc(templateId)
                .stream()
                .map(this::toVersionDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChecklistTemplateVersionDTO findVersionById(Long templateId, Long versionId) {
        if (!templateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("ChecklistTemplate", templateId);
        }
        ChecklistTemplateVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplateVersion", versionId));
        if (!version.getTemplate().getId().equals(templateId)) {
            throw new ResourceNotFoundException("ChecklistTemplateVersion", versionId);
        }
        return toVersionDTO(version);
    }

    private ChecklistTemplateDTO toDTO(ChecklistTemplate template) {
        ChecklistTemplateDTO dto = new ChecklistTemplateDTO();
        dto.setId(template.getId());
        dto.setCode(template.getCode());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setActive(template.getActive());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setUpdatedAt(template.getUpdatedAt());

        template.getVersions().stream()
                .filter(ChecklistTemplateVersion::getActiveVersion)
                .findFirst()
                .ifPresent(v -> {
                    dto.setCurrentVersionNumber(v.getVersionNumber());
                    dto.setCurrentVersionDate(v.getPublicationDate());
                });

        return dto;
    }

    private ChecklistTemplateVersionDTO toVersionDTO(ChecklistTemplateVersion version) {
        ChecklistTemplateVersionDTO dto = new ChecklistTemplateVersionDTO();
        dto.setId(version.getId());
        dto.setTemplateId(version.getTemplate().getId());
        dto.setTemplateCode(version.getTemplate().getCode());
        dto.setTemplateName(version.getTemplate().getName());
        dto.setVersionNumber(version.getVersionNumber());
        dto.setActiveVersion(version.getActiveVersion());
        dto.setStatus(version.getStatus().name());
        dto.setPublicationDate(version.getPublicationDate());
        dto.setEffectiveDate(version.getEffectiveDate());
        dto.setCreatedAt(version.getCreatedAt());
        dto.setUpdatedAt(version.getUpdatedAt());
        return dto;
    }
}
