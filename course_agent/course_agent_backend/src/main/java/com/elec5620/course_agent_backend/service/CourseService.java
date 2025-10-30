package com.elec5620.course_agent_backend.service;

import com.elec5620.course_agent_backend.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(String courseId);


    void deleteCourseById(String courseId);

    void updateCourse(Course course);
    boolean existsById(String courseId);

    Course addCourse(Course course);

    void selectCourse(Integer userId, String courseId);

    List<Course> getCoursesByMajor(String major);
    Optional<Course> getCourseOptionalById(String courseId);
    List<Course> recommendCoursesByMajor(String major);
}
