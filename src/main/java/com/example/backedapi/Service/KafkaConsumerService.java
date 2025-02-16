package com.example.backedapi.Service;

import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.WebSocket.AlarmWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private ObjectMapper objectMapper;

    List toObject(String message) {
        try {
            return objectMapper.readValue(message, List.class);

        } catch (Exception e) {
            LOGGER.error("Error parsing message", e);
            return null;
        }
    }

    @KafkaListener(topics = "socketSend", groupId = "myGroup")
    public void listen(String  msg) {
        List<AlarmMessage> alarmMessages = toObject(msg);

        System.out.println("outSize:"+alarmMessages.size());
        alarmMessages.forEach(AlarmWebSocket::broadcast);

    }

}