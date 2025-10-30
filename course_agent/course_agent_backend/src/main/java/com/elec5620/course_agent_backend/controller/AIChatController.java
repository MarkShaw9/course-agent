package com.elec5620.course_agent_backend.controller;

import com.elec5620.course_agent_backend.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;


    // Endpoint for processing chat messages
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody String userMessage) {
        String response = aiChatService.processUserMessage(userMessage);
        return ResponseEntity.ok(response);
    }





}