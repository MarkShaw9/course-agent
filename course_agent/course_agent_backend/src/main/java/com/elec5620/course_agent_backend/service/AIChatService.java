package com.elec5620.course_agent_backend.service;

import com.elec5620.course_agent_backend.model.Course;

import java.util.List;

public interface AIChatService {
    String processUserMessage(String prompt);
    List<Course> getCourseRecommendations(String inputMessage, List<Course> relatedCourses);
}