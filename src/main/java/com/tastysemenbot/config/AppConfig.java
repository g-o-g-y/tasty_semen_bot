//package com.tastysemenbot.config;
//
//import com.tastysemenbot.bot.TelegramBot;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
//
//@Configuration
//@AllArgsConstructor
//public class AppConfig {
//    private TelegramConfig botConfig;
//
////    @Bean
////    public RestTemplate restTemplate(RestTemplateBuilder builder) {
////        return builder.build();
////    }
//
////    @Bean
////    public MessageSource messageSource() {
////        ReloadableResourceBundleMessageSource messageSource
////                = new ReloadableResourceBundleMessageSource();
////
////        messageSource.setBasename("classpath:messages");
////        messageSource.setDefaultEncoding("UTF-8");
////        return messageSource;
////    }
//
//    @Bean
//    public TelegramBot TelegramBot() {
//
//        TelegramBot telegramBot = new TelegramBot();
//        telegramBot.setBotUsername(botConfig.getBotUsername());
//        telegramBot.setBotToken(botConfig.getBotToken());
//        telegramBot.setBotPath(botConfig.getBotPath());
//
//        return telegramBot;
//    }
//
//    @Bean
//    public SetWebhook setWebhookInstance() {
//        return SetWebhook.builder().url(botConfig.getBotPath()).build();
//    }
//}
