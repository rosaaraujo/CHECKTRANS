package es.araujo.checktrans.controller;

import es.araujo.checktrans.dto.ChecklistTemplateCreateDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.service.ChecklistTemplateService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/templates")
public class ChecklistTemplateController {

    private static final String REDIRECT_TEMPLATES = "redirect:/templates";
    private static final String VIEW_LIST = "template/list";
    private static final String VIEW_FORM = "template/form";
    private static final String VIEW_DETAIL = "template/detail";

    private final ChecklistTemplateService templateService;

    public ChecklistTemplateController(ChecklistTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ChecklistTemplateDTO> templatePage;

        if (search != null && !search.isBlank()) {
            templatePage = templateService.searchByName(search, pageable);
            model.addAttribute("search", search);
        } else {
            templatePage = templateService.findAll(pageable);
        }

        model.addAttribute("templates", templatePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", templatePage.getTotalPages());
        model.addAttribute("totalItems", templatePage.getTotalElements());
        return VIEW_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("template", new ChecklistTemplateCreateDTO());
        return VIEW_FORM;
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("template") ChecklistTemplateCreateDTO createDTO,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return VIEW_FORM;
        }

        ChecklistTemplateDTO created = templateService.create(createDTO);
        redirectAttributes.addFlashAttribute("success", "Plantilla creada correctamente");
        return REDIRECT_TEMPLATES + "/" + created.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ChecklistTemplateDTO template = templateService.findById(id);
        model.addAttribute("template", template);
        return VIEW_DETAIL;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ChecklistTemplateDTO template = templateService.findById(id);
        ChecklistTemplateCreateDTO editDTO = new ChecklistTemplateCreateDTO();
        editDTO.setCode(template.getCode());
        editDTO.setName(template.getName());
        editDTO.setDescription(template.getDescription());
        model.addAttribute("template", editDTO);
        model.addAttribute("templateId", id);
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("template") ChecklistTemplateCreateDTO updateDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("templateId", id);
            return VIEW_FORM;
        }

        templateService.update(id, updateDTO);
        redirectAttributes.addFlashAttribute("success", "Plantilla actualizada correctamente");
        return REDIRECT_TEMPLATES + "/" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        templateService.deactivate(id);
        redirectAttributes.addFlashAttribute("success", "Plantilla desactivada correctamente");
        return REDIRECT_TEMPLATES;
    }
}
