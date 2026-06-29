package es.araujo.checktrans.domain.template;

import es.araujo.checktrans.domain.AuditableEntity;
import es.araujo.checktrans.domain.enums.ChecklistHeaderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "checklist_template_headers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"template_id", "code"})
})
public class ChecklistTemplateHeader extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ChecklistTemplate template;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String label;

    @Column(nullable = false)
    private Integer headerOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChecklistHeaderType headerType;

    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(nullable = false)
    private Boolean required = false;

    public ChecklistTemplateHeader() {
    }

    public ChecklistTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ChecklistTemplate template) {
        this.template = template;
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

    public ChecklistHeaderType getHeaderType() {
        return headerType;
    }

    public void setHeaderType(ChecklistHeaderType headerType) {
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
}
