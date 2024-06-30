package net.yuguerten.aritara.service;


import net.yuguerten.aritara.config.OpenAIConfig;
import net.yuguerten.aritara.dto.StoryResponseDTO;
import net.yuguerten.aritara.model.Story;
import net.yuguerten.aritara.model.User;
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

//    public String generateStory(String plot, String title, String storyLength, String genre, String writingStyle, String mainCharacter, String characterDescription, String settingDescription, String audience) {
//
//        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(
//                """
//                    You are a very good and experienced story teller.
//                    --------------------
//                    Write a {storyLength}-length story titled: {title}.
//                    The story has to respect and contain the following preferences in a coherent way:
//                    Plot details: {plot}
//                    Main Character Name: {mainCharacter}
//                    Main Character Description: {characterDescription}
//                    Setting Description: {settingDescription}
//                    Tone/Style: {writingStyle}
//                    Genre: {genre}
//                    Audience: {audience}
//                    ---------------------
//                    The output should be something like:
//                    Tile: "-the title-"
//                    then show the story in a new line.
//                    ---------------------
//                    If the preferences mentioned above are not specified, choose your own but don't mention them when outputting the story.
//                    The output should not be something like:
//                    "Main Character Name: Lucas Main Character Description: Lucas is a blind musician in his mid-thirties with an unruly mane of jet-black hair and a soulful heart. Despite his disability, his spirit radiates positivity and warmth. Setting Description: The story is set in a bustling city that never sleeps. Lucas is often found playing his soulful tunes in the heart of the city, amidst the chaos and noise. Tone/Style: Inspirational and Heartwarming Genre: Drama Audience: Young adults and adults ... etc"
//                    It should be something like:
//                    "Title: "-title-"
//                             \s
//                              Joe, a 22-year-old data science student from a modest European background, possessed a burning ambition to get rich; not for the sake of vanity or mere materialistic desires but to bring about a significant change in the life of his ailing mother whose dreams had been repeatedly crushed under the weight of economic hardships... etc"
//                """
//        );
//        Prompt prompt = promptTemplate.create(Map.of(
//                "plot", plot,
//                "title", title,
//                "storyLength", storyLength,
//                "genre", genre,
//                "writingStyle", writingStyle,
//                "mainCharacter", mainCharacter,
//                "characterDescription", characterDescription,
//                "settingDescription", settingDescription,
//                "audience", audience
//        ));
//        ChatResponse response = openAiChatClient.getOpenAiChatClient().call(prompt);
//        return new StoryResponseDTO(response.getResult().getOutput().getContent()).getGeneratedStory();
//    }

    public String generateStory(String plot, String title, String storyLength, String genre, String writingStyle, String mainCharacter, String characterDescription, String settingDescription, String audience, String fileContent) {

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are a very good and experienced storyteller.")
                .append("\n--------------------")
                .append("\nWrite a ").append(storyLength).append("-length story titled: ").append(title).append(".")
                .append("\nThe story has to respect and contain the following preferences in a coherent way:")
                .append("\nPlot details: ").append(plot)
                .append("\nMain Character Name: ").append(mainCharacter)
                .append("\nMain Character Description: ").append(characterDescription)
                .append("\nSetting Description: ").append(settingDescription)
                .append("\nTone/Style: ").append(writingStyle)
                .append("\nGenre: ").append(genre)
                .append("\nAudience: ").append(audience)
                .append("\n--------------------");

        if (fileContent != null && !fileContent.isEmpty()) {
            promptBuilder.append("\nHere is some reference material:").append("\n").append(fileContent);
        }

        promptBuilder.append("\n--------------------")
                .append("\nThe output should be something like:")
                .append("\nTitle: \"-title-\"")
                .append("\n\nJoe, a 22-year-old data science student from a modest European background, possessed a burning ambition to get rich; not for the sake of vanity or mere materialistic desires but to bring about a significant change in the life of his ailing mother whose dreams had been repeatedly crushed under the weight of economic hardships... etc");

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(promptBuilder.toString());
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
        return new StoryResponseDTO(response.getResult().getOutput().getContent()).getGeneratedStory();
    }

    public String regenerateStory(String originalStory, String feedback) {

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(
                """
                    You are a very good and experienced story teller.
                    --------------------
                    Below is the original story:
                    "{originalStory}"
                    Here is the feedback: "{feedback}"
                    Please regenerate the story based on the feedback.
                    """
        );
        Prompt prompt = promptTemplate.create(Map.of(
                "originalStory", originalStory,
                "feedback", feedback
        ));
        ChatResponse response = openAiChatClient.getOpenAiChatClient().call(prompt);
        return new StoryResponseDTO(response.getResult().getOutput().getContent()).getGeneratedStory();
    }

//    public List<Story> findStoriesByUser(User user) {
//        return storyRepository.findByUser(user);
//    }

    public List<Story> findStoriesByUserDesc(User user) {
        return storyRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public void deleteStory(Long id, User user) {
        Story story = storyRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Story not found or you are not authorized to delete it"));
        storyRepository.delete(story);
    }

    public void saveStory(Story story) {
        storyRepository.save(story);
    }

    public Story getStoryById(Long id) {
        return storyRepository.findById(id).orElse(null);
    }
}
