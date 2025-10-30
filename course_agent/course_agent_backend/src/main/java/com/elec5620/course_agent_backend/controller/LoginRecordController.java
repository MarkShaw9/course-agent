package com.elec5620.course_agent_backend.controller;

import com.elec5620.course_agent_backend.model.LoginRecord;
import com.elec5620.course_agent_backend.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class LoginRecordController {

    @Autowired
    private LoginRecordService loginRecordService;


    @GetMapping("/login-records")
    public ResponseEntity<List<LoginRecord>> getAllLoginRecords() {
        List<LoginRecord> records = loginRecordService.getAllLoginRecords();
        return ResponseEntity.ok(records);
    }


    @GetMapping("/login-records/{username}")
    public ResponseEntity<List<LoginRecord>> getLoginRecordsByUsername(@PathVariable String username) {
        List<LoginRecord> records = loginRecordService.getLoginRecordsByUsername(username);
        return ResponseEntity.ok(records);
    }
}