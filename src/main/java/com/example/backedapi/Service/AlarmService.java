package com.example.backedapi.Service;

import com.example.backedapi.WebSocket.AlarmWebSocket;
import com.example.backedapi.Util.AlarmMessage;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {



    // 可以使用消息隊列、異步執行器等來處理
    public void processAlarm(AlarmMessage alarmMessage) {
        // 先記錄到資料庫或日誌
        saveAlarm(alarmMessage);
        AlarmWebSocket.broadcast(alarmMessage);

        // 根據告警等級進行通知
//        if ("ERROR".equalsIgnoreCase(alarmMessage.getLevel())) {
//            alarmWebSocket.broadcast(alarmMessage);
//
//        } else if ("WARN".equalsIgnoreCase(alarmMessage.getLevel())) {
//        } else {
//            // 其它情況可能僅記錄日誌
//            logAlarm(alarmMessage);
//        }
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
