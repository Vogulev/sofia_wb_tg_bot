package ru.vogulev.sofia_wb_tg_bot.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "wb_user")
public class WbUser {

    public WbUser(String name, String phone, String about) {
        this.name = name;
        this.phone = phone;
        this.about = about;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String about;
}
