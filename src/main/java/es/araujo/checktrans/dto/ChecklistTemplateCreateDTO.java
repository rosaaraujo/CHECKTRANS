package es.araujo.checktrans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChecklistTemplateCreateDTO {

    @NotBlank(message = "{template.code.required}")
    @Size(max = 20, message = "{template.code.max}")
    private String code;

    @NotBlank(message = "{template.name.required}")
    @Size(max = 200, message = "{template.name.max}")
    private String name;

    @Size(max = 4000, message = "{template.description.max}")
    private String description;

    public ChecklistTemplateCreateDTO() {
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
}
