package ru.vogulev.sofia_wb_tg_bot.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wb_user")
public class WbUser {

    public WbUser(UserState state, LocalDateTime stateUpdate, Long chatId, String tgUserName, String name) {
        this.state = state;
        this.stateUpdate = stateUpdate;
        this.chatId = chatId;
        this.tgUserName = tgUserName;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tg_user_name")
    private String tgUserName;

    private String name;

    private String phone;

    private String about;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(name = "state_update")
    private LocalDateTime stateUpdate;

    @Column(name = "chat_id")
    private Long chatId;
}