package net.yuguerten.aritara.service;


import net.yuguerten.aritara.config.OpenAIConfig;
import net.yuguerten.aritara.model.Story;
import net.yuguerten.aritara.repository.StoryRepository;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoryService {
    @Autowired
    private StoryRepository storyRepository;

    private final OpenAIConfig openAiChatClient;

    public String generateStory(String plot, String title, String storyLength, String genre, String writingStyle, String mainCharacter, String characterDescription, String settingDescription, String audience) {

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(
                """
                    Title: "{title}"
                    Summary: Write a story about: {plot}
                    Main Character Name: {mainCharacter}
                    Main Character Description: {characterDescription}
                    Setting Description: {settingDescription}
                    Tone/Style: {writingStyle}
                    Story Length: {storyLength}
                    Genre: {genre}
                    Audience: {audience}
                """
        );
        Prompt prompt = promptTemplate.create(Map.of(
                "plot", plot,
                "title", title,
                "storyLength", storyLength,
                "genre", genre,
                "writingStyle", writingStyle,
                "mainCharacter", mainCharacter,
                "characterDescription", characterDescription,
                "settingDescription", settingDescription,
                "audience", audience
        ));
        ChatResponse response = openAiChatClient.getOpenAiChatClient().call(prompt);
        return response.getResult().getOutput().getContent();
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public List<Story> getStoriesByUserId(Long userId) {
        return storyRepository.findByUserId(userId);
    }

    public Story getStoryById(Long id) {
        return storyRepository.findById(id).orElse(null);
    }

    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}
