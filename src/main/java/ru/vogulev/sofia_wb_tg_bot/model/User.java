package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.Data;

@Data
public class User {
    private String phone;
    private String name;
    private String about;
    private UserState state;
    private Long chatId;

    public User(UserState state, Long chatId) {
        this.state = state;
        this.chatId = chatId;
    }
}