package es.araujo.checktrans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChecklistPhaseCreateDTO {

    @NotBlank(message = "{phase.code.required}")
    @Size(max = 20, message = "{phase.code.max}")
    private String code;

    @NotNull(message = "{phase.order.required}")
    private Integer phaseOrder;

    @NotBlank(message = "{phase.name.required}")
    @Size(max = 200, message = "{phase.name.max}")
    private String name;

    @Size(max = 4000, message = "{phase.description.max}")
    private String description;

    public ChecklistPhaseCreateDTO() {
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
}
