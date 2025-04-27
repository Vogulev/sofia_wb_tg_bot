package ru.vogulev.sofia_wb_tg_bot;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;

@UtilityClass
public class MessageUtils {

    @NotNull
    public SendMessage getMessage(Long chatId, String text) {
        return getMessage(chatId, text, new ReplyKeyboardRemove(true));
    }

    @NotNull
    public SendMessage getMessage(Long chatId, UserState state) {
        return getMessage(chatId, state.text(), state.replyKeyboard());
    }

    @NotNull
    public SendMessage getMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        message.setReplyMarkup(replyKeyboard);
        return message;
    }
}
