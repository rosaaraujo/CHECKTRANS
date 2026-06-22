package es.araujo.checktrans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/demo-ui")
    public String demoUi() {
        return "demo-ui";
    }
}
