package net.yuguerten.aritara.controller;

import net.yuguerten.aritara.dto.LoginDTO;
import net.yuguerten.aritara.dto.StoryRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("storyRequestDTO", new StoryRequestDTO());
        return "home";
    }
}
