package com.elec5620.course_agent_backend.service.impl;

import com.elec5620.course_agent_backend.dto.CourseData;
import com.elec5620.course_agent_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChatServiceImpl implements ChatService {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public ChatServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<String> getChatResponse(String userMessage) {
        System.out.println("API Key: " + apiKey);
        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createRequestBody(userMessage))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(ex -> {
                    System.err.println("Error response: " + ex.getMessage());
                    return Mono.just("Failed to connect to AI service, please try again later.");
                });
    }


    @Override
    public Mono<String> getStudyPlan(CourseData courseData) {
        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createCourseRequestBody(courseData)) // 发送课程数据
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(ex -> {
                    System.err.println("Error response: " + ex.getMessage());
                    return Mono.just("Failed to connect to AI service, please try again later.");
                });
    }




    private String createRequestBody(String userMessage) {
        return "{ \"model\": \"gpt-4o-mini\", \"messages\": [{ \"role\": \"user\", \"content\": \"" + userMessage + "\" }] }";
    }


    private String createCourseRequestBody(CourseData courseData) {

        System.out.println("Show data:"+courseData.getCourseId()+courseData.getCourseName());

        return "{ \"model\": \"gpt-4o-mini\", \"messages\": [{ \"role\": \"system\", \"content\": \"You are a helpful assistant that generates personalized study plans based on course data.\" },"
                + "{ \"role\": \"user\", \"content\": \"Here is the course data: \\n"
                + "Course Code: " + courseData.getCourseId() + "\\n"
                + "Course Name: " + courseData.getCourseName() + "\\n"
                + "Course Description: " + courseData.getCourseDescription() + "\\n"
                + "Course Duration: 13 weeks\\n"
                + "Goals: HD \" }] }";


    }
}
