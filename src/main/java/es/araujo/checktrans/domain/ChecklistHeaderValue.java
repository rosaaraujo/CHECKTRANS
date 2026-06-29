package es.araujo.checktrans.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "checklist_header_values")
public class ChecklistHeaderValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    @JsonIgnore
    private Checklist checklist;

    @Column(name = "header_id")
    private Long headerId;

    @Column(nullable = false, length = 20)
    private String headerCode;

    @Column(nullable = false, length = 200)
    private String headerLabel;

    @Column(nullable = false, length = 20)
    private String headerType;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String value;

    @Column(nullable = false)
    private Integer headerOrder;

    public ChecklistHeaderValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(String headerCode) {
        this.headerCode = headerCode;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getHeaderOrder() {
        return headerOrder;
    }

    public void setHeaderOrder(Integer headerOrder) {
        this.headerOrder = headerOrder;
    }
}
