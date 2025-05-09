package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import ru.vogulev.sofia_wb_tg_bot.MessageUtils;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;
import ru.vogulev.sofia_wb_tg_bot.model.Reply;
import ru.vogulev.sofia_wb_tg_bot.model.UserState;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.vogulev.sofia_wb_tg_bot.model.UserState.*;

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
    public static final String VIDEO_3_THIRD_NOTIFY_TEXT = "500.000 ₽ просто на ветер… Думаю, ты не хочешь также?\n\n" +
            "Если ты думаешь, что ошибки селлеров — это просто выдумки, то вот тебе реальная история.\n\n" +
            "Девушка пришла на ВБ с горящими глазами.\n" +
            "Купила ВИП-обучение за 200.000 ₽.\n\n" +
            "Обещали: “закупим, запустим, продадим” — легко, быстро, красиво.\n\n" +
            "А в итоге?\n" +
            "- товар был выбран неудачно, непродуманно\n" +
            "- куплен на Садоводе с большой накруткой \n" +
            "- юнит экономика не рассчитана\n" +
            "- вложила сразу большую сумму \n\n" +
            "Естественно, товар не зашел. А обучение закончилось, никто не стал помогать. Никто не указал даже на ошибки!\n\n" +
            "Вложила — потому что поверила на 100%, ведь там “миллионные обороты, известный эксперт»\n\n" +
            "Но далеко не всегда дорогое обучение = хорошее..\n\n" +
            "Результат: разбитые ожидания, не закрытый кредит, который взял муж, слёзы и выгорание.\n\n" +
            "И надежда… Сейчас уже с этим товаром ничего не сделаешь, распродадим и будем с 0 строить заново. " +
            "С головой и пониманием что и как мы делаем!\n\n" +
            "Если хочешь на ВБ — делай это осознанно, с работающей системой.\n" +
            "Не с рекламными сказками и “золотыми горами”.\n" +
            "А с реальной стратегией, которая проверена мной и моими учениками!\n\n" +
            "Вот анкета, заполни её — и мы обсудим, как выйти на ВБ без слива.";
    public static final String VIDEO_3_FOURTH_NOTIFY_TEXT = "Я не первый год обучаю людей запускаться на маркетплейсах." +
            "И знаешь, какой самый частый страх?\n\n" +
            "А если не получится?..\n\n" +
            "Вот только смотри:";
    public static final String VIDEO_3_FOURTH_NOTIFY_TEXT_2 = "Они все боялись. Все!\n" +
            "Но сделали шаг. И пошли с поддержкой, пошагово, по системе.\n\n" +
            "Моя стратегия — это не про “на авось”.\n\n" +
            "Это конкретные действия, контроль запусков, чёткий расчёт и обратная связь.\n" +
            "К тому же я сертифицированный коуч и понимаю, что многие страхи в голове реально мешают нам начать жить, " +
            "так как мы хотим. Поэтому я добавила в свое обучение психологический модуль, " +
            "который обязательно вам поможет идти вперед к своей цели!\n\n" +
            "Пока ты сомневаешься — другие уже зарабатывают.\n" +
            "Можно ещё месяц думать. Год. Или три.\n" +
            "А можно сделать шаг сейчас.\n\n" +
            "Заполни анкету — и получи свой персональный план выхода.";

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
        notifyUser(wbUsersOnVideo3, SECOND_NOTIFY_TIME_MINUTE, VIDEO_3_SECOND_NOTIFY_TEXT, VIDEO_3_NOTIFY_2);
    }

    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    public void notify1AfterVideo3() {
        var statesAfterVideo3 = List.of(
                UserState.VIDEO_3,
                UserState.VIDEO_3_NOTIFY_1,
                VIDEO_3_NOTIFY_2,
                UserState.END);
        var wbUsers = wbUserRepository.getWbUsersByStateIn(statesAfterVideo3);
        for (var wbUser : wbUsers) {
            var message = MessageUtils.getMessage(wbUser.getChatId(), VIDEO_3_THIRD_NOTIFY_TEXT, UserState.VIDEO_3.replyKeyboard());
            var screen1 = new InputMediaPhoto(new File("photo/screen1.jpg"), "screen1");
            var screen2 = new InputMediaPhoto(new File("photo/screen2.jpg"), "screen2");
            var mediaGroup = SendMediaGroup.builder()
                    .chatId(wbUser.getChatId())
                    .medias(List.of(screen1, screen2)).build();
            telegramBot.proceed(new Reply(message, mediaGroup));
            wbUser.setState(VIDEO_3_NOTIFY_3);
            wbUserRepository.save(wbUser);
        }
    }

    @Scheduled(initialDelay = 1, fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    public void notify2AfterVideo3() {
        var statesAfterVideo3 = List.of(VIDEO_3_NOTIFY_3);
        var wbUsers = wbUserRepository.getWbUsersByStateIn(statesAfterVideo3);
        for (var wbUser : wbUsers) {
            var message = MessageUtils.getMessage(wbUser.getChatId(), VIDEO_3_FOURTH_NOTIFY_TEXT);
            var screen3 = new InputMediaPhoto(new File("photo/screen3.jpg"), "screen3");
            var screen4 = new InputMediaPhoto(new File("photo/screen4.jpg"), "screen4");
            var screen5 = new InputMediaPhoto(new File("photo/screen5.jpg"), "screen5");
            var screen6 = new InputMediaPhoto(new File("photo/screen6.jpg"), "screen6");
            var mediaGroup = SendMediaGroup.builder()
                    .chatId(wbUser.getChatId())
                    .medias(List.of(screen3, screen4, screen5, screen6)).build();
            telegramBot.proceed(new Reply(message, mediaGroup));
            message = MessageUtils.getMessage(wbUser.getChatId(), VIDEO_3_FOURTH_NOTIFY_TEXT_2, UserState.VIDEO_3.replyKeyboard());
            telegramBot.proceed(new Reply(message));
            wbUser.setState(END_AFTER_NOTIFY);
            wbUserRepository.save(wbUser);
        }
    }

    private void notifyUser(List<WbUser> wbUsers, int notifyTime, String notifyText, UserState transitionState) {
        for (var wbUser : wbUsers) {
            var stateUpdate = wbUser.getStateUpdate();
            if (stateUpdate.plusMinutes(notifyTime).isBefore(LocalDateTime.now())) {
                var message = MessageUtils.getMessage(wbUser.getChatId(), wbUser.getName() + notifyText,
                        transitionState.prevState().replyKeyboard());
                telegramBot.proceed(new Reply(message));
                wbUser.setState(transitionState);
                wbUserRepository.save(wbUser);
            }
        }
    }
}