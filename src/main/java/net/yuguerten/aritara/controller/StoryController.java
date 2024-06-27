package net.yuguerten.aritara.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.yuguerten.aritara.dto.StoryRequestDTO;
import net.yuguerten.aritara.dto.StoryResponseDTO;
import net.yuguerten.aritara.model.Story;
import net.yuguerten.aritara.model.User;
import net.yuguerten.aritara.service.StoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

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

    @PostMapping("/story/save")
    public String saveStory(@RequestParam("content") String content, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Story story = new Story();
        story.setUser(loggedInUser);
        story.setContent(content);
        story.setCreatedAt(LocalDateTime.now());

        storyService.saveStory(story);

        model.addAttribute("message", "Story saved successfully!");
        model.addAttribute("story", story.getContent());
        return "storyResult";
    }

    // Display saved stories
    @GetMapping("/savedStories")
    public String showSavedStories(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Story> stories = storyService.findStoriesByUser(loggedInUser);
        model.addAttribute("stories", stories);
        return "savedStories";
    }

    // Delete a story
    @PostMapping("/story/delete")
    @ResponseBody
    public Map<String, Object> deleteStory(@RequestParam Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            storyService.deleteStory(id, loggedInUser);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
