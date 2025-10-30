package com.elec5620.course_agent_backend.service;

import com.elec5620.course_agent_backend.model.LoginRecord;

import java.util.List;

public interface LoginRecordService {
    List<LoginRecord> getAllLoginRecords();
    List<LoginRecord> getLoginRecordsByUsername(String username);
    void saveLoginRecord(LoginRecord loginRecord);
}