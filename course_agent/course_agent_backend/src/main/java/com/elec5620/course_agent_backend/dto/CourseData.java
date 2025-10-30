package com.elec5620.course_agent_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseData {

    @JsonProperty("CourseId")
    String CourseId;

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseGoals() {
        return courseGoals;
    }

    public void setCourseGoals(String courseGoals) {
        this.courseGoals = courseGoals;
    }

    @JsonProperty("Course_description")
    String courseDescription;

    @JsonProperty("Course_name")
    String courseName;

    String courseDuration;

    String courseGoals;




}
