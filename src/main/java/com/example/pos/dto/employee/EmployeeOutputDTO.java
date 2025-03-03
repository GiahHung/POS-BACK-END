package com.example.pos.dto.employee;

import com.example.pos.model.RefreshToken;

public class EmployeeOutputDTO {
    private Long id;
    private String userName;
    private String name;
    private String roleId;
    private String roleName;
    private String phoneNumber;
    private String email;
    private String token;
    private RefreshToken refreshToken;

    public EmployeeOutputDTO(Long id, String userName, String name, String roleId, String phoneNumber, String email,
            String roleName) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.roleId = roleId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
