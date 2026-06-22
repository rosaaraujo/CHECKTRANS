package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
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

    public ChecklistTemplateService(ChecklistTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
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

        ChecklistTemplate saved = templateRepository.save(template);
        log.info("Template created with id: {}", saved.getId());
        return toDTO(saved);
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

        ChecklistTemplate saved = templateRepository.save(template);
        log.info("Template {} updated", id);
        return toDTO(saved);
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

    private ChecklistTemplateDTO toDTO(ChecklistTemplate template) {
        ChecklistTemplateDTO dto = new ChecklistTemplateDTO();
        dto.setId(template.getId());
        dto.setCode(template.getCode());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setActive(template.getActive());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setUpdatedAt(template.getUpdatedAt());
        return dto;
    }
}
