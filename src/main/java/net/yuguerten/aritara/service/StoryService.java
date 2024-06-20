package net.yuguerten.aritara.service;


import net.yuguerten.aritara.config.OpenAIConfig;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final OpenAIConfig openAiChatClient;

    public String askLLM(String storyLength, String genre, String mainCharacter, String characterDescription, String audience) {

        SystemPromptTemplate promptTemplate = new SystemPromptTemplate(
                """
                        I need you to generate a story based on the following:
                            Story Length: {storyLength}
                            Genre: {genre}
                            Main Character: {mainCharacter}
                            Character Description: {characterDescription}
                            Audience: {audience}
                            The output should be a concise and coherent paragraph in the specified genre, featuring the described main character, and tailored for the given audience.
                        """
        );
        Prompt prompt = promptTemplate.create(Map.of(
                "storyLength", storyLength,
                "genre", genre,
                "mainCharacter", mainCharacter,
                "characterDescription", characterDescription,
                "audience", audience
        ));
        ChatResponse response = openAiChatClient.getOpenAiChatClient().call(prompt);
        return response.getResult().getOutput().getContent();
    }
}
