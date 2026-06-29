package es.araujo.checktrans.domain.template;

import es.araujo.checktrans.domain.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_templates")
public class ChecklistTemplate extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistTemplateVersion> versions = new ArrayList<>();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistTemplateHeader> headers = new ArrayList<>();

    public ChecklistTemplate() {
    }

    public void addVersion(ChecklistTemplateVersion version) {
        versions.add(version);
        version.setTemplate(this);
    }

    public void removeVersion(ChecklistTemplateVersion version) {
        versions.remove(version);
        version.setTemplate(null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ChecklistTemplateVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<ChecklistTemplateVersion> versions) {
        this.versions = versions;
    }

    public List<ChecklistTemplateHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ChecklistTemplateHeader> headers) {
        this.headers = headers;
    }

    public void addHeader(ChecklistTemplateHeader header) {
        headers.add(header);
        header.setTemplate(this);
    }

    public void removeHeader(ChecklistTemplateHeader header) {
        headers.remove(header);
        header.setTemplate(null);
    }
}
