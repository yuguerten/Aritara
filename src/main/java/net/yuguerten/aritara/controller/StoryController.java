package net.yuguerten.aritara.controller;

import lombok.RequiredArgsConstructor;
import net.yuguerten.aritara.dto.StoryRequestDTO;
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
        model.addAttribute("storyRequest", new StoryRequestDTO());
        return "home";
    }

    @PostMapping("/story/generate")
    public String generateStory(@ModelAttribute StoryRequestDTO storyRequestDTO, Model model) {
        try {
            String story = storyService.generateStory(
                    storyRequestDTO.getPlot(),
                    storyRequestDTO.getTitle(),
                    storyRequestDTO.getStoryLength(),
                    storyRequestDTO.getGenre(),
                    storyRequestDTO.getWritingStyle(),
                    storyRequestDTO.getCharacterName(),
                    storyRequestDTO.getCharacterDescription(),
                    storyRequestDTO.getSettingDescription(),
                    storyRequestDTO.getAudience());
            model.addAttribute("story", story);
            return "storyResult";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while generating the story: " + e.getMessage());
            return "error";
        }
    }

}
