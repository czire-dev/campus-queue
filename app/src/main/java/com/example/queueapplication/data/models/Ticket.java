package com.example.queueapplication.data.models;

import com.example.queueapplication.data.enums.Priority;
import com.example.queueapplication.data.enums.Status;

public class Ticket extends BaseModel {
    private String ticketNo;
    private Service service;
    private User user;
    private Status status;
    private long dateCreated, dateFinished = -1;

    public Ticket(String ticketNo, Service service, User user) {
        this.ticketNo = ticketNo;
        this.service = service;
        this.user = user;
        this.status = Status.WAITING;
        dateCreated = System.currentTimeMillis();
    }

    public Ticket(int id, String ticketNo, Service service, User user) {
        this.id = id;
        this.ticketNo = ticketNo;
        this.service = service;
        this.user = user;
        this.status = Status.WAITING;
        dateCreated = System.currentTimeMillis();
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(long dateFinished) {
        this.dateFinished = dateFinished;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
