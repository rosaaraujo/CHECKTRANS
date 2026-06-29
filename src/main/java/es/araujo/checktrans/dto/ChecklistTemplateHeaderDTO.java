package es.araujo.checktrans.dto;

import java.time.LocalDateTime;

public class ChecklistTemplateHeaderDTO {

    private Long id;
    private Long templateId;
    private String code;
    private String label;
    private Integer headerOrder;
    private String headerType;
    private String options;
    private Boolean required;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChecklistTemplateHeaderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getHeaderOrder() {
        return headerOrder;
    }

    public void setHeaderOrder(Integer headerOrder) {
        this.headerOrder = headerOrder;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
