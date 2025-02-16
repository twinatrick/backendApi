package com.example.backedapi.Service;

import com.example.backedapi.Util.AlarmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProducerService {

//    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);
    private static final String TOPIC = "socketSend";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String toJsonString(List<AlarmMessage> message) {
        try {
            String s = objectMapper.writeValueAsString(message);
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void sendMessage(List<AlarmMessage> message)  {
        System.out.println("Sending message: {}"+ message.size());
            kafkaTemplate.send(TOPIC, Objects.requireNonNull(toJsonString(message)));

    }
}