package es.araujo.checktrans.controller;

import es.araujo.checktrans.dto.ChecklistPhaseCreateDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateVersionDTO;
import es.araujo.checktrans.service.ChecklistPhaseService;
import es.araujo.checktrans.service.ChecklistTemplateService;
import jakarta.validation.Valid;
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
@RequestMapping("/templates/{templateId}/versions/{versionId}/phases")
public class ChecklistPhaseController {

    private static final String VIEW_LIST = "phase/list";
    private static final String VIEW_FORM = "phase/form";

    private final ChecklistPhaseService phaseService;
    private final ChecklistTemplateService templateService;

    public ChecklistPhaseController(ChecklistPhaseService phaseService,
                                     ChecklistTemplateService templateService) {
        this.phaseService = phaseService;
        this.templateService = templateService;
    }

    @GetMapping
    public String list(@PathVariable Long templateId,
                       @PathVariable Long versionId,
                       Model model) {
        ChecklistTemplateVersionDTO version = templateService.findVersionById(templateId, versionId);
        List<ChecklistPhaseDTO> phases = phaseService.findByVersionId(versionId);
        model.addAttribute("version", version);
        model.addAttribute("phases", phases);
        return VIEW_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long templateId,
                                 @PathVariable Long versionId,
                                 Model model) {
        ChecklistTemplateVersionDTO version = templateService.findVersionById(templateId, versionId);
        model.addAttribute("version", version);
        model.addAttribute("phase", new ChecklistPhaseCreateDTO());
        return VIEW_FORM;
    }

    @PostMapping
    public String create(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @Valid @ModelAttribute("phase") ChecklistPhaseCreateDTO createDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistTemplateVersionDTO version = templateService.findVersionById(templateId, versionId);
            model.addAttribute("version", version);
            return VIEW_FORM;
        }

        phaseService.create(versionId, createDTO);
        redirectAttributes.addFlashAttribute("success", "Fase creada correctamente");
        return redirectToPhases(templateId, versionId);
    }

    @GetMapping("/{phaseId}/edit")
    public String showEditForm(@PathVariable Long templateId,
                               @PathVariable Long versionId,
                               @PathVariable Long phaseId,
                               Model model) {
        ChecklistTemplateVersionDTO version = templateService.findVersionById(templateId, versionId);
        ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
        ChecklistPhaseCreateDTO editDTO = new ChecklistPhaseCreateDTO();
        editDTO.setCode(phase.getCode());
        editDTO.setPhaseOrder(phase.getPhaseOrder());
        editDTO.setName(phase.getName());
        editDTO.setDescription(phase.getDescription());
        model.addAttribute("version", version);
        model.addAttribute("phase", editDTO);
        model.addAttribute("phaseId", phaseId);
        return VIEW_FORM;
    }

    @PostMapping("/{phaseId}/edit")
    public String update(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         @Valid @ModelAttribute("phase") ChecklistPhaseCreateDTO updateDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistTemplateVersionDTO version = templateService.findVersionById(templateId, versionId);
            model.addAttribute("version", version);
            model.addAttribute("phaseId", phaseId);
            return VIEW_FORM;
        }

        phaseService.update(versionId, phaseId, updateDTO);
        redirectAttributes.addFlashAttribute("success", "Fase actualizada correctamente");
        return redirectToPhases(templateId, versionId);
    }

    @PostMapping("/{phaseId}/delete")
    public String delete(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         RedirectAttributes redirectAttributes) {
        phaseService.delete(versionId, phaseId);
        redirectAttributes.addFlashAttribute("success", "Fase eliminada correctamente");
        return redirectToPhases(templateId, versionId);
    }

    @PostMapping("/{phaseId}/moveUp")
    public String moveUp(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         RedirectAttributes redirectAttributes) {
        phaseService.moveUp(versionId, phaseId);
        redirectAttributes.addFlashAttribute("success", "Orden actualizado");
        return redirectToPhases(templateId, versionId);
    }

    @PostMapping("/{phaseId}/moveDown")
    public String moveDown(@PathVariable Long templateId,
                           @PathVariable Long versionId,
                           @PathVariable Long phaseId,
                           RedirectAttributes redirectAttributes) {
        phaseService.moveDown(versionId, phaseId);
        redirectAttributes.addFlashAttribute("success", "Orden actualizado");
        return redirectToPhases(templateId, versionId);
    }

    private String redirectToPhases(Long templateId, Long versionId) {
        return "redirect:/templates/" + templateId + "/versions/" + versionId + "/phases";
    }
}
