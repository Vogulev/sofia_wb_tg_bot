package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vogulev.sofia_wb_tg_bot.MessageUtils;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.User;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static ru.vogulev.sofia_wb_tg_bot.model.UserState.START;
import static ru.vogulev.sofia_wb_tg_bot.model.UserState.VIDEO_1;


@Component
@RequiredArgsConstructor
public class StateMachine {
    private final WbUserRepository wbUserRepository;
    private final Map<Long, User> users = new HashMap<>();

    public SendMessage eventHandler(Long chatId, String userMessage) {
        var user = users.get(chatId) == null ? new User(START, chatId) : users.get(chatId);
        var success = handleUserAnswer(user, userMessage);
        SendMessage message;
        if (success) {
            message = MessageUtils.getMessage(chatId, user.getState());
            user.setState(user.getState().nextState());
            users.put(chatId, user);
        } else {
            message = MessageUtils.getMessage(chatId, user.getState().unsuccessfulText());
        }
        return message;
    }

    private boolean handleUserAnswer(User user, String userMessage) {
        switch (user.getState()) {
            case NAME -> user.setName(userMessage);
            case PHONE -> user.setPhone(userMessage);
            case ABOUT -> user.setAbout(userMessage);
            case VIDEO_1 -> {
                var wbUser = new WbUser(user.getName(), user.getPhone(), user.getAbout(),
                        VIDEO_1.name(), LocalDateTime.now(), user.getChatId());
                wbUserRepository.save(wbUser);
            }
            case PENDING_ANSWER_VIDEO_1, VIDEO_1_NOTIFY -> {
                return userMessage.equalsIgnoreCase("победа");
            }
            case PENDING_ANSWER_VIDEO_2, VIDEO_2_NOTIFY -> {
                return userMessage.equalsIgnoreCase("успех");
            }
            case PENDING_ANSWER_VIDEO_3, VIDEO_3_NOTIFY -> {
                return userMessage.equalsIgnoreCase("деньги");
            }
        }
        return true;
    }
}