package com.ninjaone.backendinterviewproject.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode
@Getter
public class User {
    @Id
    private String login;
    private String password;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
