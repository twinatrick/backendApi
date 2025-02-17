package com.example.backedapi.Timer;

import com.example.backedapi.Service.CheckApiService;
import com.example.backedapi.Util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
public class CheckTime {
    @Autowired
    private static SpringUtil springUtil;

    public static void start(){
        Timer timer = new Timer(true);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        while (calendar.getTime().before(now)) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
//        while (calendar.getTime().before(now)) {
//            calendar.add(Calendar.HOUR_OF_DAY, 30);
//        }
        Date setTime = new Date(calendar.getTimeInMillis());
        timer.schedule(checkApi, setTime, 60 * 60 * 1000);
        System.out.printf("CheckTime.start: %s\n", setTime);
    }
    public static TimerTask checkApi = new TimerTask() {
        @Override
        public void run() {
            CheckApiService checkApiService = springUtil.getBean(CheckApiService.class);
            try {
                Date now = new Date();
                checkApiService.getAquarkApiData();
                long executeTime = System.currentTimeMillis()-now.getTime();
                System.out.printf("CheckTime.checkApi: %s\n", executeTime/1000);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };
}
