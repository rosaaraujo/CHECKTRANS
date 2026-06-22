package es.araujo.checktrans.controller;

import es.araujo.checktrans.config.ChecktransProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ChecktransProperties properties;

    public HomeController(ChecktransProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", properties.getApp().getName());
        model.addAttribute("appVersion", properties.getApp().getVersion());
        return "index";
    }
}
