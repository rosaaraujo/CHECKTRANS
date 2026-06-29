package es.araujo.checktrans.domain.template;

import es.araujo.checktrans.domain.AuditableEntity;
import es.araujo.checktrans.domain.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "ChecklistTemplateItem")
@Table(name = "checklist_template_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"phase_id", "code"})
})
public class ChecklistItem extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private ChecklistPhase phase;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(name = "item_order", nullable = false)
    private Integer itemOrder;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 20)
    private ItemType itemType;

    @Column(nullable = false)
    private Boolean required = true;

    @Column(columnDefinition = "TEXT")
    private String options;

    public ChecklistItem() {
    }

    public ChecklistPhase getPhase() {
        return phase;
    }

    public void setPhase(ChecklistPhase phase) {
        this.phase = phase;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
