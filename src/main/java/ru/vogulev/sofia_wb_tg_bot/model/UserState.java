package ru.vogulev.sofia_wb_tg_bot.model;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.List;

import static ru.vogulev.sofia_wb_tg_bot.Constants.REQUEST_FORM_URL;
import static ru.vogulev.sofia_wb_tg_bot.Constants.SEND_REQUEST_MSG;

@Slf4j
public enum UserState {

    START {
        @Override
        public UserState nextState() {
            return GO;
        }

        @Override
        public UserState prevState() {
            return START;
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

        @SneakyThrows
        @Override
        public InputFile video() {
            return new InputFile().setMedia(new File("video/greet_video.mp4"));
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Поехали!");
            button.setCallbackData("Поехали!");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    GO {
        @Override
        public UserState nextState() {
            return NAME;
        }

        @Override
        public UserState prevState() {
            return START;
        }

        @Override
        public String text() {
            return "Давай познакомимся с тобой перед началом! Напиши ниже, как тебя зовут? \uD83D\uDC47\uD83C\uDFFB";
        }

        @Override
        public String unsuccessfulText() {
            return "Жми кнопку \"Поехали!\"";
        }
    },
    NAME {
        @Override
        public UserState nextState() {
            return PHONE;
        }

        @Override
        public UserState prevState() {
            return GO;
        }

        @Override
        public String text() {
            return "И оставь свой номер телефона \uD83D\uDC47\uD83C\uDFFB";
        }

        @Override
        public String unsuccessfulText() {
            return "Введите корректное имя, оно не может содержать цифр и спец. символов";
        }
    },
    PHONE {
        @Override
        public UserState nextState() {
            return ABOUT;
        }

        @Override
        public UserState prevState() {
            return NAME;
        }

        @Override
        public String text() {
            return "Расскажи чем ты сейчас занимаешься: может быть ты в декрете или работаешь в найме?";
        }

        @Override
        public String unsuccessfulText() {
            return "Введите корректный номер телефона, что бы мы могли с вами связаться";
        }
    },
    ABOUT {
        @Override
        public UserState nextState() {
            return VIDEO_1;
        }

        @Override
        public UserState prevState() {
            return PHONE;
        }

        @Override
        public String text() {
            return "Рада узнать тебя поближе и давай начнем с  первого видео, в котором мы разберем:\n" +
                    "—  почему ты до сих пор не на ВБ\n" +
                    "— какие мифы тормозят 90% новичков\n" +
                    "— что ты теряешь, откладывая ещё на один месяц\n\n" +
                    "Жми на кнопку и смотри.\n";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Смотреть 1 видео");
            button.setCallbackData("Смотреть 1 видео");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_1 {
        @Override
        public UserState nextState() {
            return VIDEO_2;
        }

        @Override
        public UserState prevState() {
            return ABOUT;
        }

        @Override
        public String text() {
            return "Вот твоё первое видео!\n" +
                    "<a href=\"https://youtu.be/8EJpBQ5ICic?si=RbP0rFMdKL5UaSF0&feature=share\">ВИДЕО 1</a>";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Смотреть 2 видео");
            button.setCallbackData("Смотреть 2 видео");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_1_NOTIFY_1 {
        @Override
        public UserState nextState() {
            return VIDEO_2;
        }

        @Override
        public UserState prevState() {
            return VIDEO_1;
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Смотреть 2 видео");
            button.setCallbackData("Смотреть 2 видео");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_1_NOTIFY_2 {
        @Override
        public UserState nextState() {
            return VIDEO_2;
        }

        @Override
        public UserState prevState() {
            return VIDEO_1;
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Смотреть 2 видео");
            button.setCallbackData("Смотреть 2 видео");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_2 {
        @Override
        public UserState nextState() {
            return VIDEO_3;
        }

        @Override
        public UserState prevState() {
            return VIDEO_1;
        }

        @Override
        public String text() {
            return "Класс!\n" +
                    "Во втором видео:\n" +
                    "— ты узнаешь, как можно зарабатывать без ИП и вложений от 50 тыс в месяц \n" +
                    "— поймёшь, что подходит именно тебе\n" +
                    "— и увидишь реальные истории людей, которые сделали это\n" +
                    "<a href=\"https://youtu.be/Ht3BkHxtzFk?si=ygOYSzz07ojeGCB9&feature=share\">ВИДЕО 2</a>";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var button = new InlineKeyboardButton("Смотреть 3 видео");
            button.setCallbackData("Смотреть 3 видео");
            var row = new InlineKeyboardRow(button);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_2_NOTIFY_1 {
        @Override
        public UserState nextState() {
            return VIDEO_3;
        }

        @Override
        public UserState prevState() {
            return VIDEO_2;
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var requestButton = new InlineKeyboardButton(SEND_REQUEST_MSG);
            requestButton.setUrl(REQUEST_FORM_URL);
            var row = new InlineKeyboardRow(requestButton);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_2_NOTIFY_2 {
        @Override
        public UserState nextState() {
            return VIDEO_3;
        }

        @Override
        public UserState prevState() {
            return VIDEO_2;
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var requestButton = new InlineKeyboardButton(SEND_REQUEST_MSG);
            requestButton.setUrl(REQUEST_FORM_URL);
            var row = new InlineKeyboardRow(requestButton);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    VIDEO_3 {
        @Override
        public UserState nextState() {
            return END;
        }

        @Override
        public UserState prevState() {
            return VIDEO_2;
        }

        @Override
        public String text() {
            return "В третьем видео реально БОМБА:\n" +
                    "— 7 смертных ошибок новичков на ВБ\n" +
                    "— мой личный факап \n" +
                    "— и самое главное — чек-лист, как запустить продажи без “слива”\n" +
                    "<a href=\"https://youtu.be/kAf6SsfzRDY?si=rRIV0O_UZCS7xpuX&feature=share\">ВИДЕО 3</a>";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var requestButton = new InlineKeyboardButton(SEND_REQUEST_MSG);
            requestButton.setUrl(REQUEST_FORM_URL);
            var row = new InlineKeyboardRow(requestButton);
            return new InlineKeyboardMarkup(List.of(row));
        }
    },
    END {
        @Override
        public UserState nextState() {
            return END;
        }

        @Override
        public UserState prevState() {
            return VIDEO_3;
        }
    };

    public abstract UserState nextState();

    public abstract UserState prevState();

    public String text() {
        return "";
    }

    public InputFile video() {
        return null;
    }

    public String unsuccessfulText() {
        return "Для получения видео нажми кнопку";
    }

    public ReplyKeyboard replyKeyboard() {
        return new ReplyKeyboardRemove(true);
    }
}