package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.User;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.util.HashMap;
import java.util.Map;

import static ru.vogulev.sofia_wb_tg_bot.model.UserState.START;


@Component
@RequiredArgsConstructor
public class StateMachine {
    private final WbUserRepository wbUserRepository;
    private final Map<Long, User> users = new HashMap<>();

    public SendMessage eventHandler(Long chatId, String userMessage) {
        var user = users.get(chatId) == null ? new User(START) : users.get(chatId);
        handleUserAnswer(user, userMessage);
        var message = getMessage(chatId, user);
        user.setState(user.getState().nextState());
        users.put(chatId, user);
        return message;
    }

    private void handleUserAnswer(User user, String userMessage) {
        switch (user.getState()) {
            case NAME -> user.setName(userMessage);
            case PHONE -> user.setPhone(userMessage);
            case ABOUT -> user.setAbout(userMessage);
            case VIDEO_1 -> {
                var wbUser = new WbUser(user.getName(), user.getPhone(), user.getAbout());
                wbUserRepository.save(wbUser);
            }
        }
    }

    @NotNull
    private SendMessage getMessage(Long chatId, User user) {
        var state = user.getState();
        var text = state.text();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        message.setReplyMarkup(state.replyKeyboard());
        return message;
    }
}