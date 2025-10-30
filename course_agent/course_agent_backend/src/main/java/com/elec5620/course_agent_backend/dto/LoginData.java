package com.elec5620.course_agent_backend.dto;


public class LoginData {
    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String userId;
    private Boolean isAdmin;
    private String token;

    private Integer Uid;

    public Integer getUid() {
        return Uid;
    }

    public void setUid(Integer uid) {
        Uid = uid;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    private String Uname;






    public LoginData(String userId, Boolean isAdmin, String token, String Uname, Integer Uid) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.token = token;
        this.Uname = Uname;
        this.Uid = Uid;
    }


}
