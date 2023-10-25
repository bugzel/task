package com.task.model;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class User {

    private Integer id;
    private String login;
    private String name;
    private String type;
    private String avatar_url;
    private LocalDateTime created_at;
    private Integer followers;
    private Integer public_repos;

}
