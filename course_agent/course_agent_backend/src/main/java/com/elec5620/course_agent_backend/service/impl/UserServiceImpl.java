package com.elec5620.course_agent_backend.service.impl;
import com.elec5620.course_agent_backend.model.Course;
import com.elec5620.course_agent_backend.model.UserCourse;

import com.elec5620.course_agent_backend.repository.UserCourseRepository;

import com.elec5620.course_agent_backend.model.User;
import com.elec5620.course_agent_backend.repository.UserRepository;
import com.elec5620.course_agent_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println(users);
        return users;
    }

    @Override
    public User registerUser(User user){
        if (userRepository.existsByEmail(user.getEmail())) {

            throw new IllegalArgumentException("Email already exists!");
        }
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String pwd) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPwd().equals(pwd)) {
            return user.get();
        } else {
            return null;
        }
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }


    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }


    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }


    @Override
    public List<UserCourse> getUserCourses(Integer userId) {
        return userCourseRepository.findCoursesByUserId(userId);
    }


    public boolean hasUserSelectedCourse(Integer userId, String courseId) {

        return userRepository.existsUserCourseByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }

    @Override
    public void cancelCourse(String userId, String courseId)  {
        User user = new User();
        user.setUserId(Integer.valueOf(userId));
        Course course = new Course();
        course.setCourseId(courseId);
        Optional<UserCourse> userCourseOpt = userCourseRepository.findByUserAndCourse(user, course);
        if (userCourseOpt.isEmpty()) {
            throw new RuntimeException("Course not found for the user");
        }

        userCourseRepository.delete(userCourseOpt.get());
    }

}
