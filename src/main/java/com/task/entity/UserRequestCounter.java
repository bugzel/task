package com.task.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
public class UserRequestCounter {

    public UserRequestCounter(String login) {
        this.login = login;
        this.requestCount = 0;
    }

    @Id
    private String login;
    private long requestCount;

}
