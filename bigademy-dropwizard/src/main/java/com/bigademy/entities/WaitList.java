package com.bigademy.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by mshah on 4/25/14.
 */
@Entity
@Table(name = "wait_list")
public class WaitList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wait_list_id", nullable = false)
    private long waitListId;

    @Column(name = "email", nullable = false)
    private String email;

    public long getWaitListId() {
        return waitListId;
    }

    public void setWaitListId(long waitListId) {
        this.waitListId = waitListId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
