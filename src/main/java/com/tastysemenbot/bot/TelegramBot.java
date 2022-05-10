package com.tastysemenbot.bot;

import com.tastysemenbot.models.User;
import com.tastysemenbot.repositories.UserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TelegramBot extends TelegramWebhookBot {
    @Value("${telegram.webhook-path}")
    String BotPath;
    @Value("${telegram.bot-name}")
    String botUsername;
    @Value("${telegram.bot-token}")
    String botToken;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        logger.info(update.toString());
        try {

            if (update.getChannelPost() != null) {
                return handleChatUpdate(update.getChannelPost());
            }
//        if(update.getMessage() == null){
//            return null;
//        }
            if (update.getMessage().getSenderChat() != null) {
                return handleChatUpdate(update.getMessage());
            }
            return handleUpdate(update.getMessage());
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    private SendMessage handleChatUpdate(Message message) {
        String chatId = message.getChatId().toString();
        if (message.getText().equals("Начинаем конкурс")) {
//            return new SendMessage(chatId, "Погнали");
            List<User> users = userRepository.findAll();
//        userRepository.deleteAll();
            int count = users.size();
            if (count < 4) {
                return new SendMessage(chatId, "Чел, недостаточно людей на конкурс");
            }

            List<String> winners = new ArrayList<>();
            for (int i = 0; i < 3; ++i) {
                int rand = (int) (Math.random() * count);
                count--;
                User winner = users.get(rand);
                if (winner.getUsername() != null) winners.add(winner.getName() + " aka " + winner.getUsername());
                else winners.add(winner.getName());
                users.remove(rand);
            }
            return new SendMessage(chatId,
                    String.format(
                            """
                                    Слушаюсь! Начинаю генерировать чиселки.
                                    Напоминаю, что первое место подразумевает исполнение загаданного пользователем желания.
                                    За неимением желания выигрышем является денежный приз.
                                    Второе и третье места: денежный приз, чтобы тоже порадовались.
                                                                        
                                    Поздравляем победителей!
                                                                        
                                    Первое место: %s !!!!111!!!1!!!
                                    Второе место: %s !!!
                                    Третье место: %s !!!
                                    С победой вас, получается) В скором времени с вами свяжутся по поводу выигрыша
                                    """,
                            winners.get(0),
                            winners.get(1),
                            winners.get(2)
                    ));
        }
        return null;
    }

    public SendMessage handleUpdate(Message message) {
//        String username = message.getFrom().getUserName();
//        if (username == null) username = message.getFrom().getFirstName();
        String firstname = message.getFrom().getFirstName();
        String username = message.getFrom().getUserName();
        log.info("New message from User:{}, chatId: {},  with text: {}",
                firstname, message.getChatId(), message.getText());
        if (userRepository.findByName(firstname).isEmpty()) {
//            if (!username.equals("GroupAnonymousBot") && !username.equals("Channel_Bot")) {
            User user = new User();
            user.setUsername(username);
            user.setName(firstname);
            userRepository.save(user);
//            }
        } else if (message.getText().equals("/start")) message.setText("/");
        return handleInputMessage(message);
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText().toLowerCase(Locale.ROOT);
        String name = message.getFrom().getFirstName();
        Long userId = message.getFrom().getId();
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start" -> replyMessage = new SendMessage(userId.toString(), String.format(
                    "Привет, %s! Я так понимаю, что ты здесь за тем, чтобы стать участником конкурса\n" +
                            "Поздравляю! Теперь ты участвуешь в нем. " +
                            "Я записал твои исключительные и неповторимые наборы символов в свою секретную табличку участников. " +
                            "Продолжай следить за интереснейшими постами в сообществе @tasty_semen, чтобы не пропустить результаты конкурса :)", name));

            case "хуй" -> replyMessage = new SendMessage(userId.toString(), "Сам хуй");
            default -> replyMessage = new SendMessage(userId.toString(),
                    """
                            Не могу понять, что тебе еще нужно?
                            Не переживай, ты уже участвуешь в конкурсе
                            Если тебе хочется с кем-то пообщаться, лучше напиши в чат канала @tasty_semen""");
        }
        return replyMessage;
    }
}