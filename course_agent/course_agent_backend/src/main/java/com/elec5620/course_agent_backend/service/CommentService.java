package com.elec5620.course_agent_backend.service;

import com.elec5620.course_agent_backend.model.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByCourseId(String courseId);
    void addComment(String courseId, Comment comment);
    List<Comment> getCommentsByUserId(Integer userId);

    // ✅ add：delete comment
    void deleteComment(Integer commentId, Integer currentUserId);
}
