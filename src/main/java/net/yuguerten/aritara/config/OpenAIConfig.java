package net.yuguerten.aritara.config;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class OpenAIConfig {
    @Value("${spring.ai.openai.api-key}")
    String apiKey;

    @Bean
    public OpenAiChatClient getOpenAiChatClient() {
        OpenAiApi openAiApi = new OpenAiApi(apiKey);
        OpenAiChatOptions options = new OpenAiChatOptions.Builder()
                .withModel("gpt-4")
                .withTemperature(0.9F)
                .withMaxTokens(3000)
                .build();
        return new OpenAiChatClient(openAiApi, options);
    }
}
