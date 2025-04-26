package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.Data;

@Data
public class User {
    private String phone;
    private String name;
    private String about;
    private UserState state;

    public User(UserState state) {
        this.state = state;
    }

}
