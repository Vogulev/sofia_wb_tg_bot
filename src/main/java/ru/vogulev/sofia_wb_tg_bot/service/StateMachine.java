package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.vogulev.sofia_wb_tg_bot.MessageUtils;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.Reply;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.vogulev.sofia_wb_tg_bot.Constants.SEND_REQUEST_MSG;


@Component
@RequiredArgsConstructor
public class StateMachine {
    public static final String START_CMD = "/start";
    public static final String DOWNLOAD_CMD = "/download";
    private final WbUserRepository wbUserRepository;
    private final ExportService exportService;
    @Value("${BOT_ADMINS}")
    private List<String> admins;

    public Reply eventHandler(Long chatId, String userName, String userMessage) {
        var user = wbUserRepository.findWbUserByChatId(chatId)
                .orElse(new WbUser(UserState.START, LocalDateTime.now(), chatId));
        var success = handleUserAnswer(user, userMessage);
        SendMessage message;
        SendVideoNote videoNote = null;
        if (userMessage.equals(DOWNLOAD_CMD) && admins.contains(userName)) {
            return new Reply(SendDocument.builder()
                    .chatId(chatId)
                    .document(new InputFile(exportService.exportUserData(), "WbUsers_%s.xlsx".formatted(LocalDate.now())))
                    .build());
        }
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
        if (Objects.equals(userMessage, START_CMD)) {
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
            case VIDEO_1 -> {
                return userMessage.equalsIgnoreCase("Смотреть 1 видео");
            }
            case VIDEO_2 -> {
                return userMessage.equalsIgnoreCase("Смотреть 2 видео");
            }
            case VIDEO_3 -> {
                return userMessage.equalsIgnoreCase("Смотреть 3 видео");
            }
            case ABOUT -> user.setAbout(userMessage);
        }
        return true;
    }
}