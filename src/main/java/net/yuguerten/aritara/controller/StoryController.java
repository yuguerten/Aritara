package net.yuguerten.aritara.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.yuguerten.aritara.dto.RegenerateDTO;
import net.yuguerten.aritara.dto.StoryRequestDTO;
import net.yuguerten.aritara.model.Story;
import net.yuguerten.aritara.model.User;
import net.yuguerten.aritara.service.StoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
            MultipartFile file = storyRequestDTO.getFile();
            if (file != null && !file.isEmpty() && file.getSize() > 1024 * 1024) {
                throw new MaxUploadSizeExceededException(1024 * 1024);
            }

            String fileContent = null;
            if (file != null && !file.isEmpty()) {
                fileContent = new String(file.getBytes());
            }

            String story = storyService.generateStory(
                    storyRequestDTO.getPlot(),
                    storyRequestDTO.getTitle(),
                    storyRequestDTO.getStoryLength(),
                    storyRequestDTO.getGenre(),
                    storyRequestDTO.getWritingStyle(),
                    storyRequestDTO.getCharacterName(),
                    storyRequestDTO.getCharacterDescription(),
                    storyRequestDTO.getSettingDescription(),
                    storyRequestDTO.getAudience(),
                    fileContent
            );

            model.addAttribute("story", story);
            return "storyResult";
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("error", "File size exceeds the maximum limit of 1MB.");
            return "error";
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

    @GetMapping("/savedStories")
    public String showSavedStories(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Story> stories = storyService.findStoriesByUserDesc(loggedInUser);
        model.addAttribute("stories", stories);
        return "savedStories";
    }

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

    @PostMapping("/story/regenerate")
    public String regenerateStory(@ModelAttribute RegenerateDTO regenerateDTO, Model model) {
        try {
            String story = storyService.regenerateStory(
                    regenerateDTO.getOriginalStory(),
                    regenerateDTO.getFeedback()
            );
            model.addAttribute("story", story);
            model.addAttribute("message", "Story regenerated successfully based on your feedback!");
            return "storyResult";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while regenerating the story: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            int maxLength = 3000; // Maximum number of characters
            if (fileContent.length() > maxLength) {
                fileContent = fileContent.substring(0, maxLength);
            }
            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @Value("${app.base-url}")
    private String baseUrl;

    @GetMapping("/story/{id}")
    public String getStoryById(@PathVariable Long id, Model model) {
        Story story = storyService.getStoryById(id);
        model.addAttribute("storyObj", story);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("storyId", story.getId());
        return "storyResult";
    }
}
