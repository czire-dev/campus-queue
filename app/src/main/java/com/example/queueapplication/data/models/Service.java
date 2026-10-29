package com.example.queueapplication.data.models;

import com.example.queueapplication.data.enums.Priority;

import java.util.List;

public class Service extends BaseModel {
    private int imgResourceId;
    private String title, subtitle, prefix;
    private Priority priorityLevel;


    public Service(int imgResourceId, String title, String subtitle, String prefix, Priority priorityLevel) {
        this.imgResourceId = imgResourceId;
        this.title = title;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.priorityLevel = priorityLevel;
    }

    public Service(int id, int imgResourceId, String title, String subtitle, String prefix, Priority priorityLevel) {
        this.id = id;
        this.imgResourceId = imgResourceId;
        this.title = title;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.priorityLevel = priorityLevel;
    }

    public Priority getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Priority priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
