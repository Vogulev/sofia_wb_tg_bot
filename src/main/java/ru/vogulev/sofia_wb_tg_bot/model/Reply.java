package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;

@Data
@AllArgsConstructor
public class Reply {
    private SendMessage message;
    private SendVideoNote videoNote;

    public Reply(SendMessage message) {
        this.message = message;
    }
}
