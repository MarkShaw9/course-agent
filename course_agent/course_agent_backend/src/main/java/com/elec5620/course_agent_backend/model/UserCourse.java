package com.elec5620.course_agent_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_courses")
@Data
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;


    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;



    public String getCourseId() {
        return course.getCourseId();
    }


    public String getCourseName() {
        return course.getCourseName();
    }

}
