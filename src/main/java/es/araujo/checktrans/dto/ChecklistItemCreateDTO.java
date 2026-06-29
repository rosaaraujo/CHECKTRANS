package es.araujo.checktrans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChecklistItemCreateDTO {

    @NotBlank(message = "{item.code.required}")
    @Size(max = 20, message = "{item.code.max}")
    private String code;

    @NotNull(message = "{item.order.required}")
    private Integer itemOrder;

    @NotBlank(message = "{item.description.required}")
    @Size(max = 500, message = "{item.description.max}")
    private String description;

    @NotNull(message = "{item.type.required}")
    private String itemType;

    @NotNull(message = "{item.required.required}")
    private Boolean required;

    public ChecklistItemCreateDTO() {
        this.required = true;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}