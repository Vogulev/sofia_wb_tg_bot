package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.vogulev.sofia_wb_tg_bot.model.Reply;
import ru.vogulev.sofia_wb_tg_bot.model.UserDto;

@Slf4j
@Getter
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final StateMachine stateMachine;

    public TelegramBot(@Value("${BOT_TOKEN}") String botToken, @Autowired StateMachine stateMachine) {
        this.botToken = botToken;
        telegramClient = new OkHttpTelegramClient(botToken);
        this.stateMachine = stateMachine;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        var userDto = new UserDto();
        if (update.hasCallbackQuery()) {
            userDto.setChatId(update.getCallbackQuery().getMessage().getChatId());
            userDto.setLogin(update.getCallbackQuery().getFrom().getUserName());
            userDto.setName(update.getCallbackQuery().getFrom().getFirstName());
            userDto.setMessage(update.getCallbackQuery().getData());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            userDto.setChatId(update.getMessage().getChatId());
            userDto.setLogin(update.getMessage().getFrom().getUserName());
            var contact = update.getMessage().getContact();
            userDto.setName(contact.getFirstName());
            userDto.setPhone(contact.getPhoneNumber());
            userDto.setMessage(update.getMessage().getText());
        }
        var reply = stateMachine.eventHandler(userDto);
        proceed(reply);
    }

    public void proceed(Reply reply) {
        try {
            if (reply.getVideoNote() != null) {
                telegramClient.execute(reply.getVideoNote());
            }
            if (reply.getMessage() != null) {
                telegramClient.execute(reply.getMessage());
            }
            if (reply.getDocument() != null) {
                telegramClient.execute(reply.getDocument());
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
