package es.araujo.checktrans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChecklistTemplateHeaderCreateDTO {

    @NotBlank(message = "{header.code.required}")
    @Size(max = 20, message = "{header.code.max}")
    private String code;

    @NotBlank(message = "{header.label.required}")
    @Size(max = 200, message = "{header.label.max}")
    private String label;

    @NotNull(message = "{header.order.required}")
    private Integer headerOrder;

    @NotBlank(message = "{header.type.required}")
    private String headerType;

    @Size(max = 4000, message = "{header.options.max}")
    private String options;

    @NotNull(message = "{header.required.required}")
    private Boolean required = false;

    public ChecklistTemplateHeaderCreateDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getHeaderOrder() {
        return headerOrder;
    }

    public void setHeaderOrder(Integer headerOrder) {
        this.headerOrder = headerOrder;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
