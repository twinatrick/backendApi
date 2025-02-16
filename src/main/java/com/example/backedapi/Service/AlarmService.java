package com.example.backedapi.Service;

import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.WebSocket.AlarmWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmService {

    @Autowired
    private ProducerService producerService;

    // 可以使用消息隊列、異步執行器等來處理

    public void processAlarm(List<AlarmMessage> alarmMessage) {
        try {
            producerService.sendMessage(alarmMessage);
        } catch (Exception e) {
            alarmMessage.get(0).setMessage("告警消息發送失敗：" + e.getMessage());
        }

    }

    private void saveAlarm(AlarmMessage alarmMessage) {
        // 實作：保存至資料庫
//        System.out.println("保存告警：" + alarmMessage.getMessage());
    }

    private void logAlarm(AlarmMessage alarmMessage) {
        // 實作：記錄到日誌系統
        System.out.println("記錄告警：" + alarmMessage.getMessage());
    }
}
