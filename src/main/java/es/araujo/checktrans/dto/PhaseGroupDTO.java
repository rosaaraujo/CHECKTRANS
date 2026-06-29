package es.araujo.checktrans.dto;

import java.util.List;

public class PhaseGroupDTO {

    private Integer phaseOrder;
    private String phaseName;
    private List<ChecklistItemDTO> items;

    public PhaseGroupDTO() {
    }

    public PhaseGroupDTO(Integer phaseOrder, String phaseName, List<ChecklistItemDTO> items) {
        this.phaseOrder = phaseOrder;
        this.phaseName = phaseName;
        this.items = items;
    }

    public Integer getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(Integer phaseOrder) {
        this.phaseOrder = phaseOrder;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public List<ChecklistItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItemDTO> items) {
        this.items = items;
    }
}
