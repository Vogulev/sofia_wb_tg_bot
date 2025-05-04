package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long chatId;
    private String login;
    private String name;
    private String message;
}