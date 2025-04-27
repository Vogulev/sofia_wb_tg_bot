package ru.vogulev.sofia_wb_tg_bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wb_user")
public class WbUser {

    public WbUser(String name, String phone, String about) {
        this.name = name;
        this.phone = phone;
        this.about = about;
    }

    public WbUser(String name, String phone, String about, String state, LocalDateTime stateUpdate, Long chatId) {
        this.name = name;
        this.phone = phone;
        this.about = about;
        this.state = state;
        this.stateUpdate = stateUpdate;
        this.chatId = chatId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String about;

    private String state;

    @Column(name = "state_update")
    private LocalDateTime stateUpdate;

    @Column(name = "chat_id")
    private Long chatId;
}
