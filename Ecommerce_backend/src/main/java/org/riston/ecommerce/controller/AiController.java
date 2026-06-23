package org.riston.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AiController {
    private final ChatClient chatClient;

    public AiController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @GetMapping("/ai-chat")
    public String generation(@RequestParam("message") String userInput) {
        try {
            return this.chatClient.prompt()
                    .user(userInput)
                    .system("You are a football expert.")
                    .call()
                    .content();
        } catch (Exception e) {
            System.err.println("AI Generation failed: " + e.getMessage());
            return "The AI service is currently unavailable. Please try again later.";
        }
    }
}