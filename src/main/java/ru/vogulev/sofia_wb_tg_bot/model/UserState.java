package ru.vogulev.sofia_wb_tg_bot.model;

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
                    "— Устала работать на дядю, жить от зарплаты до зарплаты, \n\n" +
                    "— Думаешь, что на Wildberries уже поздно и все занято\n" +
                    "— Или боишься, что ничего не получится и ты сольешь деньги \n\n" +
                    "И да, я тебя понимаю. когда-то я была на твоем месте.\n\n" +
                    "Меня зовут Софья Волевая я — селлер на ВБ с ежемесяным обротом 2 млн, " +
                    "я эксперт по работе с Китаем и выходу на ВБ, я помогаю людям обрести свободу и независимость, " +
                    "сказать найму «пока» и зарабатывать на ВБ без страхов и мифов\n\n" +
                    "В этом боте ты получишь 3 видео:\n" +
                    "— в каждом будет кодовое слово\n" +
                    "— тебе надо будет его написать в бот, чтобы получить следующее видео\n" +
                    "— и ты пройдёшь путь от “смотрю, но боюсь” до “делаю первый шаг”\n\n" +
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
                    "— что ты теряешь, откладывая ещё на один месяц\n\n" +
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
            return PENDING_ANSWER_VIDEO_1;
        }

        @Override
        public String text() {
            return "Вот твоё первое видео!\n" +
                    "Смотри до конца — и пиши сюда кодовое слово, чтобы получить вторую часть.";
        }
    },
    VIDEO_1_NOTIFY {
        @Override
        public UserState nextState() {
            return PENDING_ANSWER_VIDEO_1;
        }
    },
    PENDING_ANSWER_VIDEO_1 {
        @Override
        public UserState nextState() {
            return VIDEO_2;
        }

        @Override
        public String text() {
            return "Класс!\n" +
                    "Во втором видео:\n" +
                    "— ты узнаешь, как можно зарабатывать без ИП и вложений от 50 тыс в месяц \n" +
                    "— поймёшь, что подходит именно тебе\n" +
                    "— и увидишь реальные истории людей, которые сделали это\n" +
                    "Жми на кнопку — и не забудь кодовое слово после просмотра, чтобы пришло финальное видео с самым жиром.";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Смотреть 2 видео"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_2 {
        @Override
        public UserState nextState() {
            return PENDING_ANSWER_VIDEO_2;
        }

        @Override
        public String text() {
            return "Вот твоё второе видео!\n" +
                    "Смотри до конца — и пиши сюда кодовое слово, чтобы получить финальное видео.";
        }
    },
    VIDEO_2_NOTIFY {
        @Override
        public UserState nextState() {
            return PENDING_ANSWER_VIDEO_2;
        }
    },
    PENDING_ANSWER_VIDEO_2 {
        @Override
        public UserState nextState() {
            return VIDEO_3;
        }

        @Override
        public String text() {
            return "Сектор «Приз» на барабане!\n" +
                    "Шучу, конечно, но в третьем видео реально БОМБА:\n" +
                    "— 7 смертных ошибок новичков на ВБ\n" +
                    "— мой личный факап \n" +
                    "— и самое главное — чек-лист, как запустить продажи без “слива”\n\n" +
                    "Жми на кнопку, смотри и пиши код — он откроет тебе следующий шаг.\n";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Смотреть 3 видео"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_3 {
        @Override
        public UserState nextState() {
            return PENDING_ANSWER_VIDEO_3;
        }

        @Override
        public String text() {
            return "Вот твоё третье видео!\n" +
                    "Смотри до конца — и пиши сюда кодовое слово.";
        }
    },
    VIDEO_3_NOTIFY {
        @Override
        public UserState nextState() {
            return PENDING_ANSWER_VIDEO_3;
        }
    },
    PENDING_ANSWER_VIDEO_3 {
        @Override
        public UserState nextState() {
            return REQUEST;
        }

        @Override
        public String text() {
            return "Имя, ты прошёл все три видео — это уже победа!\n" +
                    "Теперь пора сделать шаг к своему запуску.\n\n" +
                    "Жми на кнопку — оставь заявку на обучение.\n" +
                    "Ты получишь: — поддержку куратора\n" +
                    "— шаблоны\n" +
                    "— план запуска без ошибок\n" +
                    "— и главное — уверенность в себе и доход\n\n";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Оставить заявку"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    REQUEST {
        @Override
        public UserState nextState() {
            return END;
        }

        @Override
        public String text() {
            return "Спасибо за заявку, мы свяжемся с тобой по указанному телефону";
        }
    },
    END {
        @Override
        public UserState nextState() {
            return END;
        }
    };

    public abstract UserState nextState();

    public String text() {
        return "";
    }

    public String unsuccessfulText() {
        return "Введено не верное кодовое слово";
    }

    public ReplyKeyboard replyKeyboard() {
        return new ReplyKeyboardRemove(true);
    }
}