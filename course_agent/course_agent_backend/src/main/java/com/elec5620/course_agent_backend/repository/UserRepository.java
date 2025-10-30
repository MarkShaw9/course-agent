package com.elec5620.course_agent_backend.repository;

import com.elec5620.course_agent_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT COUNT(uc) > 0 FROM UserCourse uc WHERE uc.user.UserId = :userId AND uc.course.courseId = :courseId")
    boolean existsUserCourseByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") String courseId);



}
