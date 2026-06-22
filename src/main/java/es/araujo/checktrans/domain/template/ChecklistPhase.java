package es.araujo.checktrans.domain.template;

import es.araujo.checktrans.domain.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_phases", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"version_id", "code"})
})
public class ChecklistPhase extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private ChecklistTemplateVersion version;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(name = "phase_order", nullable = false)
    private Integer phaseOrder;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items = new ArrayList<>();

    public ChecklistPhase() {
    }

    public void addItem(ChecklistItem item) {
        items.add(item);
        item.setPhase(this);
    }

    public void removeItem(ChecklistItem item) {
        items.remove(item);
        item.setPhase(null);
    }

    public ChecklistTemplateVersion getVersion() {
        return version;
    }

    public void setVersion(ChecklistTemplateVersion version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(Integer phaseOrder) {
        this.phaseOrder = phaseOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ChecklistItem> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItem> items) {
        this.items = items;
    }
}
