package com.example.queueapplication.data.models;

import com.example.queueapplication.data.enums.Role;

public class User extends BaseModel {
    private String name;
    private Role role;

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public User(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
