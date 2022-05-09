package com.tastysemenbot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Telegram_user")
@Table(name = "telegram_user")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    private String username;

    @Column(nullable = false)
    private String name;
}
