package es.araujo.checktrans.controller;

import es.araujo.checktrans.domain.enums.ItemType;
import es.araujo.checktrans.dto.ChecklistTemplateItemCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateItemDTO;
import es.araujo.checktrans.dto.ChecklistPhaseDTO;
import es.araujo.checktrans.service.ChecklistItemService;
import es.araujo.checktrans.service.ChecklistPhaseService;
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
@RequestMapping("/templates/{templateId}/versions/{versionId}/phases/{phaseId}/items")
public class ChecklistItemController {

    private static final String VIEW_LIST = "item/list";
    private static final String VIEW_FORM = "item/form";

    private final ChecklistItemService itemService;
    private final ChecklistPhaseService phaseService;

    public ChecklistItemController(ChecklistItemService itemService,
                                    ChecklistPhaseService phaseService) {
        this.itemService = itemService;
        this.phaseService = phaseService;
    }

    @GetMapping
    public String list(@PathVariable Long templateId,
                       @PathVariable Long versionId,
                       @PathVariable Long phaseId,
                       Model model) {
        ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
        List<ChecklistTemplateItemDTO> items = itemService.findByPhaseId(phaseId);
        model.addAttribute("templateId", templateId);
        model.addAttribute("versionId", versionId);
        model.addAttribute("phase", phase);
        model.addAttribute("items", items);
        return VIEW_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long templateId,
                                 @PathVariable Long versionId,
                                 @PathVariable Long phaseId,
                                 Model model) {
        ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
        model.addAttribute("templateId", templateId);
        model.addAttribute("versionId", versionId);
        model.addAttribute("phase", phase);
        model.addAttribute("item", new ChecklistTemplateItemCreateDTO());
        model.addAttribute("itemTypes", getAvailableItemTypes());
        return VIEW_FORM;
    }

    @PostMapping
    public String create(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         @Valid @ModelAttribute("item") ChecklistTemplateItemCreateDTO createDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
            model.addAttribute("templateId", templateId);
            model.addAttribute("versionId", versionId);
            model.addAttribute("phase", phase);
            model.addAttribute("itemTypes", getAvailableItemTypes());
            return VIEW_FORM;
        }

        itemService.create(phaseId, createDTO);
        redirectAttributes.addFlashAttribute("success", "Item creado correctamente");
        return redirectToItems(templateId, versionId, phaseId);
    }

    @GetMapping("/{itemId}/edit")
    public String showEditForm(@PathVariable Long templateId,
                               @PathVariable Long versionId,
                               @PathVariable Long phaseId,
                               @PathVariable Long itemId,
                               Model model) {
        ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
        ChecklistTemplateItemDTO itemDto = itemService.findById(phaseId, itemId);
        ChecklistTemplateItemCreateDTO editDTO = new ChecklistTemplateItemCreateDTO();
        editDTO.setCode(itemDto.getCode());
        editDTO.setItemOrder(itemDto.getItemOrder());
        editDTO.setDescription(itemDto.getDescription());
        editDTO.setItemType(itemDto.getItemType());
        editDTO.setRequired(itemDto.getRequired());
        model.addAttribute("templateId", templateId);
        model.addAttribute("versionId", versionId);
        model.addAttribute("phase", phase);
        model.addAttribute("item", editDTO);
        model.addAttribute("itemId", itemId);
        model.addAttribute("itemTypes", getAvailableItemTypes());
        return VIEW_FORM;
    }

    @PostMapping("/{itemId}/edit")
    public String update(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         @PathVariable Long itemId,
                         @Valid @ModelAttribute("item") ChecklistTemplateItemCreateDTO updateDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ChecklistPhaseDTO phase = phaseService.findById(versionId, phaseId);
            model.addAttribute("templateId", templateId);
            model.addAttribute("versionId", versionId);
            model.addAttribute("phase", phase);
            model.addAttribute("itemId", itemId);
            model.addAttribute("itemTypes", getAvailableItemTypes());
            return VIEW_FORM;
        }

        itemService.update(phaseId, itemId, updateDTO);
        redirectAttributes.addFlashAttribute("success", "Item actualizado correctamente");
        return redirectToItems(templateId, versionId, phaseId);
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         @PathVariable Long itemId,
                         RedirectAttributes redirectAttributes) {
        itemService.delete(phaseId, itemId);
        redirectAttributes.addFlashAttribute("success", "Item eliminado correctamente");
        return redirectToItems(templateId, versionId, phaseId);
    }

    @PostMapping("/{itemId}/moveUp")
    public String moveUp(@PathVariable Long templateId,
                         @PathVariable Long versionId,
                         @PathVariable Long phaseId,
                         @PathVariable Long itemId,
                         RedirectAttributes redirectAttributes) {
        itemService.moveUp(phaseId, itemId);
        redirectAttributes.addFlashAttribute("success", "Orden actualizado");
        return redirectToItems(templateId, versionId, phaseId);
    }

    @PostMapping("/{itemId}/moveDown")
    public String moveDown(@PathVariable Long templateId,
                           @PathVariable Long versionId,
                           @PathVariable Long phaseId,
                           @PathVariable Long itemId,
                           RedirectAttributes redirectAttributes) {
        itemService.moveDown(phaseId, itemId);
        redirectAttributes.addFlashAttribute("success", "Orden actualizado");
        return redirectToItems(templateId, versionId, phaseId);
    }

    private String redirectToItems(Long templateId, Long versionId, Long phaseId) {
        return "redirect:/templates/" + templateId + "/versions/" + versionId + "/phases/" + phaseId + "/items";
    }

    private List<String> getAvailableItemTypes() {
        return Arrays.asList(ItemType.YES_NO_NA.name());
    }
}