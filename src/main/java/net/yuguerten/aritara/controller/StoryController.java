package net.yuguerten.aritara.controller;

import lombok.RequiredArgsConstructor;
import net.yuguerten.aritara.model.StoryRequest;
import net.yuguerten.aritara.service.StoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("storyRequest", new StoryRequest());
        return "index";
    }

    @PostMapping("/story/generate")
    public String generateStory(@ModelAttribute StoryRequest storyRequest, Model model) {
        try {
            String story = storyService.askLLM(
                    storyRequest.getPlot(),
                    storyRequest.getStoryLength(),
                    storyRequest.getGenre(),
                    storyRequest.getCharacterName(),
                    storyRequest.getCharacterDescription(),
                    storyRequest.getAudience());
            model.addAttribute("story", story);
            return "storyResult";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while generating the story: " + e.getMessage());
            return "error";
        }
    }

}
