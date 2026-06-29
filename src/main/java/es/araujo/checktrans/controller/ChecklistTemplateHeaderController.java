package es.araujo.checktrans.controller;

import es.araujo.checktrans.domain.enums.ChecklistHeaderType;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateHeaderCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateHeaderDTO;
import es.araujo.checktrans.service.ChecklistTemplateHeaderService;
import es.araujo.checktrans.service.ChecklistTemplateService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/templates/{templateId}/headers")
public class ChecklistTemplateHeaderController {

    private static final String VIEW_LIST = "header/list";
    private static final String VIEW_FORM = "header/form";

    private final ChecklistTemplateHeaderService headerService;
    private final ChecklistTemplateService templateService;

    public ChecklistTemplateHeaderController(ChecklistTemplateHeaderService headerService,
                                              ChecklistTemplateService templateService) {
        this.headerService = headerService;
        this.templateService = templateService;
    }

    @GetMapping
    public String list(@PathVariable Long templateId, Model model) {
        ChecklistTemplateDTO template = templateService.findById(templateId);
        List<ChecklistTemplateHeaderDTO> headers = headerService.findByTemplateId(templateId);
        model.addAttribute("template", template);
        model.addAttribute("headers", headers);
        return VIEW_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long templateId, Model model) {
        ChecklistTemplateDTO template = templateService.findById(templateId);
        model.addAttribute("template", template);
        model.addAttribute("header", new ChecklistTemplateHeaderCreateDTO());
        model.addAttribute("headerTypes", getAvailableHeaderTypes());
        return VIEW_FORM;
    }

    @PostMapping
    public String create(@PathVariable Long templateId,
                         @Valid @ModelAttribute("header") ChecklistTemplateHeaderCreateDTO createDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistTemplateDTO template = templateService.findById(templateId);
            model.addAttribute("template", template);
            model.addAttribute("headerTypes", getAvailableHeaderTypes());
            return VIEW_FORM;
        }

        headerService.create(templateId, createDTO);
        redirectAttributes.addFlashAttribute("success", "Cabecera creada correctamente");
        return "redirect:/templates/" + templateId + "/headers";
    }

    @GetMapping("/{headerId}/edit")
    public String showEditForm(@PathVariable Long templateId,
                                @PathVariable Long headerId,
                                Model model) {
        ChecklistTemplateDTO template = templateService.findById(templateId);
        ChecklistTemplateHeaderDTO headerDTO = headerService.findById(templateId, headerId);
        ChecklistTemplateHeaderCreateDTO editDTO = new ChecklistTemplateHeaderCreateDTO();
        editDTO.setCode(headerDTO.getCode());
        editDTO.setLabel(headerDTO.getLabel());
        editDTO.setHeaderOrder(headerDTO.getHeaderOrder());
        editDTO.setHeaderType(headerDTO.getHeaderType());
        editDTO.setOptions(headerDTO.getOptions());
        editDTO.setRequired(headerDTO.getRequired());

        model.addAttribute("template", template);
        model.addAttribute("header", editDTO);
        model.addAttribute("headerId", headerId);
        model.addAttribute("headerTypes", getAvailableHeaderTypes());
        return VIEW_FORM;
    }

    @PostMapping("/{headerId}/edit")
    public String update(@PathVariable Long templateId,
                         @PathVariable Long headerId,
                         @Valid @ModelAttribute("header") ChecklistTemplateHeaderCreateDTO updateDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistTemplateDTO template = templateService.findById(templateId);
            model.addAttribute("template", template);
            model.addAttribute("headerId", headerId);
            model.addAttribute("headerTypes", getAvailableHeaderTypes());
            return VIEW_FORM;
        }

        headerService.update(templateId, headerId, updateDTO);
        redirectAttributes.addFlashAttribute("success", "Cabecera actualizada correctamente");
        return "redirect:/templates/" + templateId + "/headers";
    }

    @PostMapping("/{headerId}/delete")
    public String delete(@PathVariable Long templateId,
                         @PathVariable Long headerId,
                         RedirectAttributes redirectAttributes) {
        headerService.delete(templateId, headerId);
        redirectAttributes.addFlashAttribute("success", "Cabecera eliminada correctamente");
        return "redirect:/templates/" + templateId + "/headers";
    }

    private List<String> getAvailableHeaderTypes() {
        return Arrays.stream(ChecklistHeaderType.values())
                .map(Enum::name)
                .toList();
    }
}
