package com.tastysemenbot.bot;

import com.tastysemenbot.models.User;
import com.tastysemenbot.repositories.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(update.getChannelPost() != null){
            return handleChatUpdate(update.getChannelPost());
        }
        if(update.getMessage().getSenderChat() != null){
            return handleChatUpdate(update.getMessage());
        }
        else
            return handleUpdate(update.getMessage());
    }

    private SendMessage handleChatUpdate(Message message) {
        String chatId = message.getChatId().toString();
        if(message.getText().equals("Начинаем конкурс")) {
//            return new SendMessage(chatId, "Погнали");
            List<User> users = userRepository.findAll();
//        userRepository.deleteAll();
            int count = users.size();
            if(count < 4){
                return new SendMessage(chatId, "Чел, недостаточно людей на конкурс");
            }

            List<String> winners = new ArrayList<>();
            for(int i = 0; i < 3; ++i){
                logger.info(users.toString());
                int rand = (int) (Math.random() * count);
                logger.info(String.valueOf(rand));
                count--;
                winners.add(users.get(rand).getUsername());
                users.remove(rand);
            }
            return new SendMessage(chatId,
                    String.format(
                            """
                                    Слушаюсь! Начинаю генерировать чиселки.
                                    
                                    Поздравляем победителей!
                                    Первое место: %s!!!
                                    Второе место: %s!!!
                                    Третье место: %s!!!
                                    """,
                            winners.get(0),
                            winners.get(1),
                            winners.get(2)
                            ));
        }
        return null;
    }

    public SendMessage handleUpdate(Message message) {
        SendMessage replyMessage = null;
        if (message != null && message.hasText()) {
            String username = message.getFrom().getUserName();
            if(userRepository.findByUsername(username).isEmpty()){
                if(!username.equals("GroupAnonymousBot") && !username.equals("Channel_Bot")){
                    User user = new User();
                    user.setUsername(username);
                    userRepository.save(user);
                }
            }

            log.info("New message from User:{}, chatId: {},  with text: {}",
                    username, message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        Long userId = message.getFrom().getId();
        SendMessage replyMessage;

        switch (inputMsg){
            case "/start" -> replyMessage = new SendMessage(userId.toString(), "Поздравляю! Вы участвуете в конкурсе");
            case "хуй" -> replyMessage = new SendMessage(userId.toString(), "Сам хуй");
            default -> replyMessage = new SendMessage(userId.toString(),
                    "Я не понимаю, че тебе еще надо?\nТы уже участвуешь");
        }
        return replyMessage;
    }
}