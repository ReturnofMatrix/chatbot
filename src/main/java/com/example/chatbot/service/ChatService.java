package com.example.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;


@Service
public class ChatService {

    private final Client client;
    private static final String MODEL_NAME = "gemini-2.5-flash";

    public ChatService(@Value("${GEMINI_KEY:}") String geminiKey) {
        // Check environment variable first, then application.properties
        String GEMINI_KEY = System.getenv("GEMINI_KEY");
        if (GEMINI_KEY == null || GEMINI_KEY.isEmpty()) {
            GEMINI_KEY = geminiKey;
        }
        
        if (GEMINI_KEY == null || GEMINI_KEY.isEmpty()) {
            throw new IllegalStateException("GEMINI_KEY environment variable or application property is not set.");
        }
        
        // Initialize the client
        this.client = Client.builder().apiKey(GEMINI_KEY).build();
    }

    public String getChatResponse(String userMessage){
        try {

            GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME, 
                userMessage, 
                GenerateContentConfig.builder().build()
            );
            
            String reply = response.text();
            return reply;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating response: " + e.getMessage();
        }
    }
}