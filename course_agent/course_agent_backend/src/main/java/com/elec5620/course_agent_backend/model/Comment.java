package com.elec5620.course_agent_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String courseId;

    @Column(name = "user_id") // Maps user_id to userId in the entity
    private Integer userId;

    private String content;
    private Integer likes;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
