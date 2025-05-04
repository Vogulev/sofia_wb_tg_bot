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
    public static final int FIRST_NOTIFY_TIME_MINUTE = 30;
    public static final int SECOND_NOTIFY_TIME_MINUTE = 120;
    public static final String VIDEO_1_FIRST_NOTIFY_TEXT = ", смешные ролики, да?\n" +
            "Я понимаю: лента засосала, коты, рецепты, сериал про ведьм… \n" +
            "Но видео всё ещё ждёт тебя — и, поверь, ты там кое-что про себя узнаешь.";
    public static final String VIDEO_1_SECOND_NOTIFY_TEXT = ", не разбивай моё сердце.\n" +
            "Ты ведь хотела разобраться, как заработать на ВБ без миллиона?\n" +
            "Посмотри видео. Ты реально можешь удивиться.";
    public static final String VIDEO_2_FIRST_NOTIFY_TEXT = ", смешные ролики, да?\n" +
            "Ну а там, в 2 видео, тебя ждёт твой путь. Прямо сейчас.\n" +
            "Забери его — пока не передумал(а).";
    public static final String VIDEO_2_SECOND_NOTIFY_TEXT = ", не разбивай моё сердце.\n" +
            "Ты уже почти всё понял(а), осталось чуть-чуть — и ты реально сможешь начать.\n" +
            "Давай, не тормози.";
    public static final String VIDEO_3_FIRST_NOTIFY_TEXT = ", тут важное:\n" +
            "предложение из видео очень скоро сгорит.\n" +
            "Да, реально. Таймер тикает.";
    public static final String VIDEO_3_SECOND_NOTIFY_TEXT = ", 10 минут — и двери закрываются…\n" +
            "Ты либо заходишь сейчас, либо потом будешь читать отзывы других.";

    private final WbUserRepository wbUserRepository;
    private final TelegramBot telegramBot;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void firstNotifyOnVideo() {
        var wbUsersOnVideo1 = wbUserRepository.getWbUsersByState(UserState.VIDEO_1);
        notifyUser(wbUsersOnVideo1, FIRST_NOTIFY_TIME_MINUTE, VIDEO_1_FIRST_NOTIFY_TEXT, UserState.VIDEO_1_NOTIFY_1);
        var wbUsersOnVideo2 = wbUserRepository.getWbUsersByState(UserState.VIDEO_2);
        notifyUser(wbUsersOnVideo2, FIRST_NOTIFY_TIME_MINUTE, VIDEO_2_FIRST_NOTIFY_TEXT, UserState.VIDEO_2_NOTIFY_1);
        var wbUsersOnVideo3 = wbUserRepository.getWbUsersByState(UserState.VIDEO_3);
        notifyUser(wbUsersOnVideo3, FIRST_NOTIFY_TIME_MINUTE, VIDEO_3_FIRST_NOTIFY_TEXT, UserState.VIDEO_3_NOTIFY_1);
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void secondNotifyOnVideo() {
        var wbUsersOnVideo1 = wbUserRepository.getWbUsersByState(UserState.VIDEO_1_NOTIFY_1);
        notifyUser(wbUsersOnVideo1, SECOND_NOTIFY_TIME_MINUTE, VIDEO_1_SECOND_NOTIFY_TEXT, UserState.VIDEO_1_NOTIFY_2);
        var wbUsersOnVideo2 = wbUserRepository.getWbUsersByState(UserState.VIDEO_2_NOTIFY_1);
        notifyUser(wbUsersOnVideo2, SECOND_NOTIFY_TIME_MINUTE, VIDEO_2_SECOND_NOTIFY_TEXT, UserState.VIDEO_2_NOTIFY_2);
        var wbUsersOnVideo3 = wbUserRepository.getWbUsersByState(UserState.VIDEO_3_NOTIFY_1);
        notifyUser(wbUsersOnVideo3, SECOND_NOTIFY_TIME_MINUTE, VIDEO_3_SECOND_NOTIFY_TEXT, UserState.VIDEO_3_NOTIFY_2);
    }

    private void notifyUser(List<WbUser> wbUsers, int notifyTime, String notifyText, UserState transitionState) {
        for (var wbUser : wbUsers) {
            var stateUpdate = wbUser.getStateUpdate();
            if (stateUpdate.plusMinutes(notifyTime).isBefore(LocalDateTime.now())) {
                var message = MessageUtils.getMessage(wbUser.getChatId(), wbUser.getName() + notifyText,
                        transitionState.prevState().replyKeyboard(), false);
                telegramBot.proceed(new Reply(message));
                wbUser.setState(transitionState);
                wbUserRepository.save(wbUser);
            }
        }
    }
}