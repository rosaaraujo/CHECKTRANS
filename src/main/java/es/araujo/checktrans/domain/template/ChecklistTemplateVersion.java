package es.araujo.checktrans.domain.template;

import es.araujo.checktrans.domain.AuditableEntity;
import es.araujo.checktrans.domain.enums.ChecklistExecutionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_template_versions")
public class ChecklistTemplateVersion extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ChecklistTemplate template;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChecklistExecutionStatus status = ChecklistExecutionStatus.CREADA;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistPhase> phases = new ArrayList<>();

    public ChecklistTemplateVersion() {
    }

    public void addPhase(ChecklistPhase phase) {
        phases.add(phase);
        phase.setVersion(this);
    }

    public void removePhase(ChecklistPhase phase) {
        phases.remove(phase);
        phase.setVersion(null);
    }

    public ChecklistTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ChecklistTemplate template) {
        this.template = template;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ChecklistExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ChecklistExecutionStatus status) {
        this.status = status;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<ChecklistPhase> getPhases() {
        return phases;
    }

    public void setPhases(List<ChecklistPhase> phases) {
        this.phases = phases;
    }
}
