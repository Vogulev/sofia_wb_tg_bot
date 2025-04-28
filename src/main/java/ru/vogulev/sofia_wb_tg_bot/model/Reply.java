package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;

@Data
@NoArgsConstructor
public class Reply {
    private SendMessage message;
    private SendVideoNote videoNote;
    private SendDocument document;

    public Reply(SendMessage message, SendVideoNote videoNote) {
        this.message = message;
        this.videoNote = videoNote;
    }

    public Reply(SendMessage message) {
        this.message = message;
    }

    public Reply(SendDocument document) {
        this.document = document;
    }
}
