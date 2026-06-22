package es.araujo.checktrans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChecklistItemDTO {

    private Long id;

    @NotBlank(message = "{checklist.item.description.required}")
    @Size(max = 255, message = "{checklist.item.description.max}")
    private String description;

    private Boolean isPass;

    @Size(max = 2000, message = "{checklist.item.observations.max}")
    private String observations;

    @NotNull(message = "{checklist.item.order.required}")
    private Integer itemOrder;

    public ChecklistItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
}
