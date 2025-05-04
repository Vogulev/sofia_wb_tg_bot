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
import ru.vogulev.sofia_wb_tg_bot.model.UserDto;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class StateMachine {
    public static final String START_CMD = "/start";
    public static final String DOWNLOAD_CMD = "/download";
    private final WbUserRepository wbUserRepository;
    private final ExportService exportService;
    @Value("${BOT_ADMINS}")
    private List<String> admins;

    public Reply eventHandler(UserDto userDto) {
        var currentDateTime = LocalDateTime.now();
        var chatId = userDto.getChatId();
        var login = userDto.getLogin();
        var userMessage = userDto.getMessage();
        var user = wbUserRepository.findWbUserByChatId(chatId)
                .orElse(new WbUser(UserState.START, currentDateTime, chatId, login, userDto.getName()));
        var success = handleUserAnswer(user, userMessage);
        SendMessage message;
        SendVideoNote videoNote = null;
        if (userMessage.equals(DOWNLOAD_CMD) && isAdmin(login)) {
            return new Reply(SendDocument.builder()
                    .chatId(chatId)
                    .document(new InputFile(exportService.exportUserData(), "WbUsers_%s.xlsx".formatted(currentDateTime)))
                    .build());
        }
        if (success) {
            message = MessageUtils.getMessage(chatId, user.getState());
            videoNote = getVideoNote(chatId, user.getState().video());
            user.setState(user.getState().nextState());
            user.setStateUpdate(currentDateTime);
            wbUserRepository.save(user);
        } else {
            message = MessageUtils.getMessage(chatId, user.getState().unsuccessfulText(), user.getState().prevState().replyKeyboard());
        }
        return new Reply(message, videoNote);
    }

    public boolean isAdmin(String userName) {
        return admins.contains(userName);
    }

    private SendVideoNote getVideoNote(Long chatId, InputFile video) {
        return video == null ? null : new SendVideoNote(String.valueOf(chatId), video);
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