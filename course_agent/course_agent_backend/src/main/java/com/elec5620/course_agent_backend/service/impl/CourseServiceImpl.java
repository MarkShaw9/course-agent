package com.elec5620.course_agent_backend.service.impl;

import com.elec5620.course_agent_backend.model.Course;
import com.elec5620.course_agent_backend.model.User;
import com.elec5620.course_agent_backend.model.UserCourse;
import com.elec5620.course_agent_backend.repository.CourseRepository;
import com.elec5620.course_agent_backend.repository.UserCourseRepository;
import com.elec5620.course_agent_backend.repository.UserRepository;
import com.elec5620.course_agent_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }
    @Override
    public Course getCourseById(String CourseId) {

        return courseRepository.findById(CourseId).orElse(null);
    }

    @Override
    public Optional<Course> getCourseOptionalById(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteCourseById(String CourseId) {
        courseRepository.deleteById(CourseId);
    }

    @Override
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public boolean existsById(String CourseId) {
        return courseRepository.existsById(CourseId);
    }


    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void selectCourse(Integer userId, String courseId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }


        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        User user = userOptional.get();
        Course course = courseOptional.get();


        Optional<UserCourse> existingRecord = userCourseRepository.findByUserAndCourse(user, course);
        if (existingRecord.isPresent()) {
            throw new IllegalStateException("Course already selected");
        }


        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        userCourseRepository.save(userCourse);
    }

    @Override
    public List<Course> recommendCoursesByMajor(String major) {

        List<Course> relatedCourses = courseRepository.findByMajor(major);


        if (relatedCourses.size() > 4) {
            Random random = new Random();
            return random.ints(0, relatedCourses.size())
                    .distinct()
                    .limit(4)
                    .mapToObj(relatedCourses::get)
                    .collect(Collectors.toList());
        }

        return relatedCourses;
    }

    @Override
    public List<Course> getCoursesByMajor(String major) {
        return courseRepository.findByMajor(major);
    }

}