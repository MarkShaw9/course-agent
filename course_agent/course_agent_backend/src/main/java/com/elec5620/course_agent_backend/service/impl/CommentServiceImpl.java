package com.elec5620.course_agent_backend.service.impl;

import com.elec5620.course_agent_backend.model.Comment;
import com.elec5620.course_agent_backend.repository.CommentRepository;
import com.elec5620.course_agent_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByCourseId(String courseId) {
        return commentRepository.findByCourseId(courseId);
    }

    @Override
    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public void addComment(String courseId, Comment comment) {
        comment.setCourseId(courseId);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer currentUserId) {
        // 1. 找到这条评论
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }

        Comment comment = optionalComment.get();

        // 2. 权限检查（考虑 userId 可能为 null 的情况）
        Integer authorId = comment.getUserId(); // 可能为 null

        // 情况A：评论没有记录作者（authorId == null）
        // 我们可以选择直接允许删除（方便你现在调试），或者禁止删除。
        // 我给你的版本：允许这个用户删除这种“无作者”的评论
        if (authorId != null && !authorId.equals(currentUserId)) {
            // 有作者 && 不是当前用户
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        // 3. 真正删除
        commentRepository.deleteById(commentId);
    }
}
