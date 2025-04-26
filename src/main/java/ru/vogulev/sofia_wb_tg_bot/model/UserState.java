package ru.vogulev.sofia_wb_tg_bot.model;

import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public enum UserState {

    START {
        @Override
        public UserState nextState() {
            return GO;
        }

        @Override
        public String text() {
            return "Привет! Если ты здесь, то, скорее всего:\n" +
                    "— Устала работать на дядю, жить от зарплаты до зарплаты, \n" +
                    "— Думаешь, что на Wildberries уже поздно и все занято\n" +
                    "— Или боишься, что ничего не получится и ты сольешь деньги \n" +
                    "И да, я тебя понимаю. когда-то я была на твоем месте.\n" +
                    "Меня зовут Софья Волевая я — селлер на ВБ с ежемесяным обротом 2 млн, я эксперт по работе с Китаем и выходу на ВБ, я помогаю людям обрести свободу и независимость, сказать найму «пока» и зарабатывать на ВБ без страхов и мифов\n" +
                    "В этом боте ты получишь 3 видео:\n" +
                    "— в каждом будет кодовое слово\n" +
                    "— тебе надо будет его написать в бот, чтобы получить следующее видео\n" +
                    "— и ты пройдёшь путь от “смотрю, но боюсь” до “делаю первый шаг”\n" +
                    "Поехали?\n";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Поехали!"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    GO {
        @Override
        public UserState nextState() {
            return NAME;
        }

        @Override
        public String text() {
            return "Давай познакомимся с тобой перед началом! Напиши ниже, как тебя зовут? \uD83D\uDC47\uD83C\uDFFB";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    NAME {
        @Override
        public UserState nextState() {
            return PHONE;
        }

        @Override
        public String text() {
            return "И оставь свой номер телефона \uD83D\uDC47\uD83C\uDFFB";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    PHONE {
        @Override
        public UserState nextState() {
            return ABOUT;
        }

        @Override
        public String text() {
            return "Расскажи чем ты сейчас занимаешься: может быть ты в декрете или работаешь в найме?";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    ABOUT {
        @Override
        public UserState nextState() {
            return VIDEO_1;
        }

        @Override
        public String text() {
            return "Рада узнать тебя поближе и давай начнем с  первого видео, в котором мы разберем:\n" +
                    "—  почему ты до сих пор не на ВБ\n" +
                    "— какие мифы тормозят 90% новичков\n" +
                    "— что ты теряешь, откладывая ещё на один месяц\n" +
                    "Жми на кнопку, смотри — и обязательно пришли кодовое слово, чтобы получить второе видео.\n";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Смотреть 1 видео"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_1 {
        @Override
        public UserState nextState() {
            return START;
        }

        @Override
        public String text() {
            return "Вот твоё первое видео!\n" +
                    "Смотри до конца — и пиши сюда кодовое слово, чтобы получить вторую часть.";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    };

    public abstract UserState nextState();

    public abstract String text();

    public abstract ReplyKeyboard replyKeyboard();
}