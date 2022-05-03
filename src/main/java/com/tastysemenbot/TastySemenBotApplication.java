package com.tastysemenbot;

import com.tastysemenbot.models.User;
import com.tastysemenbot.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class TastySemenBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TastySemenBotApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepository) {
//        return args -> {
//            User user1 = new User();
//            user1.setUsername("user1");
//            User user2 = new User();
//            user2.setUsername("user2");
//            User user3 = new User();
//            user3.setUsername("user3");
//            Set<User> users = Stream.of(user1, user2, user3).collect(Collectors.toSet());
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
////            userRepository.saveAll(users);
//        };
//    }
}
