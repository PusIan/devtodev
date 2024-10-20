package org.devtodev.producer;

import info.schnatterer.mobynamesgenerator.MobyNamesGenerator;
import lombok.extern.slf4j.Slf4j;
import org.devtodev.dto.AuthorRawDto;
import org.devtodev.producer.service.AuthorProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@Slf4j
public class ProducerMain implements ApplicationRunner {
    private final AuthorProducer authorProducer;
    @Value("${devtodev.kafka.numberofmessages}")
    private int numberOfMessages;

    @Value("${devtodev.kafka.numberofuniqueauthornames}")
    private int numberOfUniqueAuthorNames;

    public ProducerMain(AuthorProducer authorProducer) {
        this.authorProducer = authorProducer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerMain.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        List<String> authors = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfUniqueAuthorNames; i++) {
            authors.add(MobyNamesGenerator.getRandomName());
        }
        for (int i = 0; i < numberOfMessages; i++) {
            AuthorRawDto authorRawDto = new AuthorRawDto(authors.get(random.nextInt(authors.size())));
            authorProducer.send(authorRawDto);
        }
        log.info("{} messages sent successfully", numberOfMessages);
    }
}