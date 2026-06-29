package es.araujo.checktrans.service;

import es.araujo.checktrans.domain.enums.ChecklistHeaderType;
import es.araujo.checktrans.domain.template.ChecklistTemplate;
import es.araujo.checktrans.domain.template.ChecklistTemplateHeader;
import es.araujo.checktrans.dto.ChecklistTemplateHeaderCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateHeaderDTO;
import es.araujo.checktrans.exception.DuplicateCodeException;
import es.araujo.checktrans.exception.ResourceNotFoundException;
import es.araujo.checktrans.repository.template.ChecklistTemplateHeaderRepository;
import es.araujo.checktrans.repository.template.ChecklistTemplateRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistTemplateHeaderService {

    private final ChecklistTemplateHeaderRepository headerRepository;
    private final ChecklistTemplateRepository templateRepository;

    public ChecklistTemplateHeaderService(ChecklistTemplateHeaderRepository headerRepository,
                                           ChecklistTemplateRepository templateRepository) {
        this.headerRepository = headerRepository;
        this.templateRepository = templateRepository;
    }

    @Transactional(readOnly = true)
    public List<ChecklistTemplateHeaderDTO> findByTemplateId(Long templateId) {
        if (!templateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("Plantilla no encontrada");
        }
        return headerRepository.findByTemplateIdOrderByHeaderOrderAsc(templateId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ChecklistTemplateHeaderDTO findById(Long templateId, Long headerId) {
        ChecklistTemplateHeader header = headerRepository.findByTemplateIdAndId(templateId, headerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabecera no encontrada"));
        return toDTO(header);
    }

    public ChecklistTemplateHeaderDTO create(Long templateId, ChecklistTemplateHeaderCreateDTO createDTO) {
        ChecklistTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Plantilla no encontrada"));

        if (headerRepository.existsByTemplateIdAndCode(templateId, createDTO.getCode())) {
            throw new DuplicateCodeException("El c\u00f3digo de cabecera ya existe en esta plantilla");
        }

        ChecklistTemplateHeader header = new ChecklistTemplateHeader();
        header.setTemplate(template);
        header.setCode(createDTO.getCode());
        header.setLabel(createDTO.getLabel());
        header.setHeaderOrder(createDTO.getHeaderOrder());
        header.setHeaderType(ChecklistHeaderType.valueOf(createDTO.getHeaderType()));
        header.setOptions(createDTO.getOptions());
        header.setRequired(createDTO.getRequired() != null && createDTO.getRequired());

        header = headerRepository.save(header);
        return toDTO(header);
    }

    public ChecklistTemplateHeaderDTO update(Long templateId, Long headerId,
                                              ChecklistTemplateHeaderCreateDTO updateDTO) {
        ChecklistTemplateHeader header = headerRepository.findByTemplateIdAndId(templateId, headerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabecera no encontrada"));

        if (!header.getCode().equals(updateDTO.getCode())
                && headerRepository.existsByTemplateIdAndCode(templateId, updateDTO.getCode())) {
            throw new DuplicateCodeException("El c\u00f3digo de cabecera ya existe en esta plantilla");
        }

        header.setCode(updateDTO.getCode());
        header.setLabel(updateDTO.getLabel());
        header.setHeaderOrder(updateDTO.getHeaderOrder());
        header.setHeaderType(ChecklistHeaderType.valueOf(updateDTO.getHeaderType()));
        header.setOptions(updateDTO.getOptions());
        header.setRequired(updateDTO.getRequired() != null && updateDTO.getRequired());

        header = headerRepository.save(header);
        return toDTO(header);
    }

    public void delete(Long templateId, Long headerId) {
        ChecklistTemplateHeader header = headerRepository.findByTemplateIdAndId(templateId, headerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabecera no encontrada"));
        headerRepository.delete(header);
    }

    private ChecklistTemplateHeaderDTO toDTO(ChecklistTemplateHeader header) {
        ChecklistTemplateHeaderDTO dto = new ChecklistTemplateHeaderDTO();
        dto.setId(header.getId());
        dto.setTemplateId(header.getTemplate().getId());
        dto.setCode(header.getCode());
        dto.setLabel(header.getLabel());
        dto.setHeaderOrder(header.getHeaderOrder());
        dto.setHeaderType(header.getHeaderType().name());
        dto.setOptions(header.getOptions());
        dto.setRequired(header.getRequired());
        dto.setCreatedAt(header.getCreatedAt());
        dto.setUpdatedAt(header.getUpdatedAt());
        return dto;
    }
}
