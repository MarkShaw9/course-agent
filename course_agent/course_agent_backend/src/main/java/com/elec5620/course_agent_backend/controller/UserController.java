package com.elec5620.course_agent_backend.controller;

import com.elec5620.course_agent_backend.dto.LoginData;
import com.elec5620.course_agent_backend.dto.LoginRequest;

import com.elec5620.course_agent_backend.model.UserCourse;
import com.elec5620.course_agent_backend.response.ApiResponse;
import com.elec5620.course_agent_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import com.elec5620.course_agent_backend.model.User;
import com.elec5620.course_agent_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.elec5620.course_agent_backend.utils.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        try {

            User registeredUser = userService.registerUser(user);


            ApiResponse<User> response = new ApiResponse<>(true, "Registration successful!", registeredUser);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            ApiResponse<User> response = new ApiResponse<>(true, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {

            ApiResponse<User> response = new ApiResponse<>(true, "Registration failed!", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPwd());

        if (user != null) {
            String token = jwtUtil.generateToken(user.getEmail());
            LoginData loginData = new LoginData(user.getEmail(), user.getAdmin(), token, user.getUsername(), user.getUserId());
            ApiResponse<LoginData> response = new ApiResponse<>(true, "Login successful", loginData);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id, @RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {

                    userService.deleteUserById(id);
                    return ResponseEntity.noContent().build();
                }
            } catch (Exception e) {
                e.printStackTrace();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> editUser(@PathVariable Integer id, @RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        // Validate token
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    // Perform edit operation
                    if (userService.existsById(id)) {
                        // Do not update the user ID
                        User existingUser = userService.findById(id);
                        if (existingUser != null) {
                            existingUser.setUsername(updatedUser.getUsername());
                            existingUser.setEmail(updatedUser.getEmail());
                            existingUser.setAdmin(updatedUser.getAdmin());
                            existingUser.setPwd(updatedUser.getPwd());
                            userService.updateUser(existingUser);
                        }
                        return ResponseEntity.noContent().build(); // 204 No Content
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
    }



    // check personal courses
    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<UserCourse>> getUserCourses(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                List<UserCourse> userCourses = userService.getUserCourses(userId);
                return ResponseEntity.ok(userCourses);
            }
        }
        return ResponseEntity.status(401).build();
    }


    //user cancel selected course
    @DeleteMapping("/{userId}/courses/{courseId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelCourse(@PathVariable String userId, @PathVariable String courseId, @RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    userService.cancelCourse(userId, courseId);
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", true);
                    response.put("message", "Course cancelled successfully");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}







