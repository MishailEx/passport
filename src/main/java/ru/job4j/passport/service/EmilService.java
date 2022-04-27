package ru.job4j.passport.service;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class EmilService {

    @KafkaListener(topics = "email")
    public void printEmail(String massage) {
        System.out.println(massage);
    }
}
