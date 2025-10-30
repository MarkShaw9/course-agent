package com.elec5620.course_agent_backend.controller;

import com.elec5620.course_agent_backend.dto.CourseData;
import com.elec5620.course_agent_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")  // 允许从前端地址的请求
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Mono<String> chatWithAI(@RequestBody ChatRequest chatRequest) {
        return chatService.getChatResponse(chatRequest.getMessage());
    }


    @PostMapping("/generate-study-plan")
    public Mono<ResponseEntity<String>> generateStudyPlan(@RequestBody CourseData courseData) {
        return chatService.getStudyPlan(courseData)
                .map(studyPlan -> ResponseEntity.ok(studyPlan))
                .onErrorResume(ex -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error generating study plan: " + ex.getMessage()));
                });
    }










    public static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
