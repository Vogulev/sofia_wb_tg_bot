package ru.vogulev.sofia_wb_tg_bot;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;

import java.util.List;

@UtilityClass
public class MessageUtils {
    @NotNull
    public SendMessage getMessage(Long chatId, String text, boolean isAdmin) {
        return getMessage(chatId, text, new ReplyKeyboardRemove(true), isAdmin);
    }

    @NotNull
    public SendMessage getMessage(Long chatId, @NotNull UserState state, boolean isAdmin) {
        return getMessage(chatId, state.text(), state.replyKeyboard(), isAdmin);
    }

    @NotNull
    public SendMessage getMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, boolean isAdmin) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .linkPreviewOptions(LinkPreviewOptions.builder().preferLargeMedia(true).build())
                .parseMode(ParseMode.HTML)
                .build();
        message.setReplyMarkup(replyKeyboard);
        if (isAdmin && replyKeyboard instanceof InlineKeyboardMarkup) {
            addAdminButtons((InlineKeyboardMarkup) replyKeyboard);
        }
        return message;
    }

    private void addAdminButtons(@NotNull InlineKeyboardMarkup replyKeyboard) {
        var keyboard = replyKeyboard.getKeyboard();
        if (!keyboard.isEmpty()) {
            var row = keyboard.getFirst();
            var button = new InlineKeyboardButton("Выгрузить таблицу");
            button.setCallbackData("/download");
            row.add(button);
        }
    }
}