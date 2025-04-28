package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import ru.vogulev.sofia_wb_tg_bot.MessageUtils;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.Reply;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class StateMachine {
    public static final String START_CMD = "/start";
    private final WbUserRepository wbUserRepository;

    public Reply eventHandler(Long chatId, String userMessage) {
        var user = wbUserRepository.findWbUserByChatId(chatId)
                .orElse(new WbUser(UserState.START, LocalDateTime.now(), chatId));
        var success = handleUserAnswer(user, userMessage);
        SendMessage message;
        SendVideoNote videoNote = null;
        if (success) {
            message = MessageUtils.getMessage(chatId, user.getState());
            if (user.getState().video() != null) {
                videoNote = getVideoNote(chatId, user.getState());
            }
            user.setState(user.getState().nextState());
            wbUserRepository.save(user);
        } else {
            message = MessageUtils.getMessage(chatId, user.getState().unsuccessfulText(), user.getState().prevState().replyKeyboard());
        }
        return new Reply(message, videoNote);
    }

    private SendVideoNote getVideoNote(Long chatId, UserState state) {
        return new SendVideoNote(String.valueOf(chatId), state.video());
    }

    private boolean handleUserAnswer(WbUser user, String userMessage) {
        if (userMessage.equals(START_CMD)) {
            user.setState(UserState.START);
        }
        switch (user.getState()) {
            case GO -> {
                return userMessage.equalsIgnoreCase("Поехали!");
            }
            case NAME -> {
                if (!userMessage.matches("^[a-zA-Zа-яА-ЯёЁ\\s-]+$")) {
                    return false;
                }
                user.setName(userMessage);
            }
            case PHONE -> {
                if (!userMessage.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
                    return false;
                }
                user.setPhone(userMessage);
            }
            case ABOUT -> user.setAbout(userMessage);
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