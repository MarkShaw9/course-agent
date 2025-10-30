package com.elec5620.course_agent_backend.repository;

import com.elec5620.course_agent_backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByCourseId(String courseId);
    List<Comment> findByUserId(Integer userId);
}
