package com.elec5620.course_agent_backend.service;

import com.elec5620.course_agent_backend.dto.CourseData;
import reactor.core.publisher.Mono;

public interface ChatService {
    Mono<String> getChatResponse(String userMessage);

    Mono<String> getStudyPlan(CourseData courseData);

}
