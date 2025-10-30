package com.elec5620.course_agent_backend.repository;

import com.elec5620.course_agent_backend.model.Course;
import com.elec5620.course_agent_backend.model.User;
import com.elec5620.course_agent_backend.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    Optional<UserCourse> findByUserAndCourse(User user, Course course);

    @Query("SELECT uc FROM UserCourse uc WHERE uc.user.UserId = :userId")
    List<UserCourse> findCoursesByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(uc) > 0 FROM UserCourse uc WHERE uc.user.UserId = :userId AND uc.course.courseId = :courseId")
    boolean existsUserCourseByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") String courseId);






}

