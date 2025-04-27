package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vogulev.sofia_wb_tg_bot.MessageUtils;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.Reply;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotifyService {
    public static final int VIDEO_1_FIRST_NOTIFY_TIME_MINUTE = 30;
    public static final int VIDEO_1_SECOND_NOTIFY_TIME_MINUTE = 120;
    public static final String VIDEO_1_FIRST_NOTIFY_TEXT = ", смешные ролики, да? " +
            "Я понимаю: лента засосала, коты, рецепты, сериал про ведьм… " +
            "Но видео всё ещё ждёт тебя — и, поверь, ты там кое-что про себя узнаешь.";
    public static final String VIDEO_1_SECOND_NOTIFY_TEXT = ", не разбивай моё сердце.\n" +
            "Ты ведь хотела разобраться, как заработать на ВБ без миллиона?\n" +
            "Посмотри видео. Ты реально можешь удивиться.";

    private final WbUserRepository wbUserRepository;
    private final TelegramBot telegramBot;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void firstNotifyOnVideo1() {
        var wbUsersOnVideo1 = wbUserRepository.getWbUsersByState(UserState.VIDEO_1.name());
        notifyUser(wbUsersOnVideo1, VIDEO_1_FIRST_NOTIFY_TIME_MINUTE, VIDEO_1_FIRST_NOTIFY_TEXT, UserState.VIDEO_1_NOTIFY);
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void secondNotifyOnVideo1() {
        List<WbUser> wbUsersOnVideo1 = wbUserRepository.getWbUsersByState(UserState.VIDEO_1_NOTIFY.name());
        notifyUser(wbUsersOnVideo1, VIDEO_1_SECOND_NOTIFY_TIME_MINUTE, VIDEO_1_SECOND_NOTIFY_TEXT, UserState.PENDING_ANSWER_VIDEO_1);
    }

    private void notifyUser(List<WbUser> wbUsers, int notifyTime, String notifyText, UserState transitionState) {
        for (var wbUser : wbUsers) {
            var stateUpdate = wbUser.getStateUpdate();
            if (stateUpdate.plusMinutes(notifyTime).isBefore(LocalDateTime.now())) {
                var message = MessageUtils.getMessage(wbUser.getChatId(), wbUser.getName() + notifyText);
                telegramBot.proceed(new Reply(message));
                wbUser.setState(transitionState.name());
                wbUserRepository.save(wbUser);
            }
        }
    }
}