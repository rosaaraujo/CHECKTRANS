package es.araujo.checktrans.controller;

import es.araujo.checktrans.domain.enums.ChecklistStatus;
import es.araujo.checktrans.dto.ChecklistCreateDTO;
import es.araujo.checktrans.dto.ChecklistDTO;
import es.araujo.checktrans.dto.ChecklistItemDTO;
import es.araujo.checktrans.dto.ChecklistTemplateDTO;
import es.araujo.checktrans.dto.PhaseGroupDTO;
import es.araujo.checktrans.service.ChecklistService;
import es.araujo.checktrans.service.ChecklistTemplateService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
@RequestMapping("/checklists")
public class ChecklistController {

    private static final String REDIRECT_CHECKLISTS = "redirect:/checklists";
    private static final String VIEW_LIST = "checklist/list";
    private static final String VIEW_FORM = "checklist/form";
    private static final String VIEW_DETAIL = "checklist/detail";

    private final ChecklistService checklistService;
    private final ChecklistTemplateService templateService;

    public ChecklistController(ChecklistService checklistService,
                                ChecklistTemplateService templateService) {
        this.checklistService = checklistService;
        this.templateService = templateService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "checkDate"));
        Page<ChecklistDTO> checklistPage;

        if (search != null && !search.isBlank()) {
            checklistPage = checklistService.searchByInspector(search, pageable);
            model.addAttribute("search", search);
        } else {
            checklistPage = checklistService.findAll(pageable);
        }

        model.addAttribute("checklists", checklistPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", checklistPage.getTotalPages());
        model.addAttribute("totalItems", checklistPage.getTotalElements());
        return VIEW_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("checklist", new ChecklistCreateDTO());
        model.addAttribute("statuses", ChecklistStatus.values());
        List<ChecklistTemplateDTO> templates = templateService.findAll(Pageable.unpaged()).getContent();
        model.addAttribute("templates", templates);
        return VIEW_FORM;
    }

    @GetMapping("/from-template/{templateId}")
    public String showCreateFromTemplate(@PathVariable Long templateId, Model model) {
        ChecklistTemplateDTO template = templateService.findById(templateId);
        ChecklistCreateDTO createDTO = new ChecklistCreateDTO();
        createDTO.setTemplateId(templateId);
        model.addAttribute("checklist", createDTO);
        model.addAttribute("statuses", ChecklistStatus.values());
        List<ChecklistTemplateDTO> templates = templateService.findAll(Pageable.unpaged()).getContent();
        model.addAttribute("templates", templates);
        model.addAttribute("selectedTemplate", template);
        return VIEW_FORM;
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("checklist") ChecklistCreateDTO createDTO,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ChecklistStatus.values());
            return VIEW_FORM;
        }

        ChecklistDTO created = checklistService.create(createDTO);
        redirectAttributes.addFlashAttribute("success", "Lista de chequeo creada correctamente");
        return REDIRECT_CHECKLISTS + "/" + created.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ChecklistDTO checklist = checklistService.findById(id);
        model.addAttribute("checklist", checklist);

        List<ChecklistItemDTO> items = checklist.getItems();
        if (items != null && !items.isEmpty()) {
            Map<Integer, List<ChecklistItemDTO>> grouped = items.stream()
                    .collect(Collectors.groupingBy(
                            i -> i.getPhaseOrder() != null ? i.getPhaseOrder() : 0));
            List<PhaseGroupDTO> phaseGroups = new ArrayList<>();
            for (Map.Entry<Integer, List<ChecklistItemDTO>> entry : grouped.entrySet()) {
                Integer order = entry.getKey();
                List<ChecklistItemDTO> groupItems = entry.getValue();
                String name = groupItems.stream()
                        .map(ChecklistItemDTO::getPhaseName)
                        .filter(n -> n != null && !n.isBlank())
                        .findFirst().orElse("");
                phaseGroups.add(new PhaseGroupDTO(order, name, groupItems));
            }
            phaseGroups.sort(Comparator.comparing(PhaseGroupDTO::getPhaseOrder));
            model.addAttribute("phaseGroups", phaseGroups);
        }

        return VIEW_DETAIL;
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam ChecklistStatus status,
                               RedirectAttributes redirectAttributes) {
        checklistService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Estado actualizado correctamente");
        return REDIRECT_CHECKLISTS + "/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        checklistService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Lista de chequeo eliminada correctamente");
        return REDIRECT_CHECKLISTS;
    }
}
