package es.araujo.checktrans.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChecklistCreateDTO {

    @NotBlank(message = "{checklist.code.required}")
    @Size(max = 20, message = "{checklist.code.max}")
    private String code;

    @NotBlank(message = "{checklist.transportPlate.required}")
    @Size(max = 15, message = "{checklist.transportPlate.max}")
    private String transportPlate;

    @NotBlank(message = "{checklist.transportType.required}")
    @Size(max = 20, message = "{checklist.transportType.max}")
    private String transportType;

    @NotBlank(message = "{checklist.inspectorName.required}")
    @Size(max = 100, message = "{checklist.inspectorName.max}")
    private String inspectorName;

    @NotNull(message = "{checklist.checkDate.required}")
    private LocalDateTime checkDate;

    @Size(max = 2000, message = "{checklist.observations.max}")
    private String observations;

    @Valid
    private List<ChecklistItemDTO> items = new ArrayList<>();

    public ChecklistCreateDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTransportPlate() {
        return transportPlate;
    }

    public void setTransportPlate(String transportPlate) {
        this.transportPlate = transportPlate;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<ChecklistItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItemDTO> items) {
        this.items = items;
    }
}
