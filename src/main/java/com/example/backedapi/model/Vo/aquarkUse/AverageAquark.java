package com.example.backedapi.model.Vo.aquarkUse;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AverageAquark {

        private String station_id;//站點
        private Date trans_time;
        private String date;

        private String dateHour;
        private float rain_d;//日累積雨量

        private float moisture;//濕度
        private float temperature;//溫度

        private float echo; //水位空高

        private float waterSpeedAquark;//水流速


        private float v1;//鋰電池電壓
        private float v2;
        private float v3;
        private float v4;
        private float v5;//太陽能板 1
        private float v6;//太陽能板 1
        private float v7;

//
//        private float rain_d_avg_hour;//日小時平均雨量
//        private float moisture_avg_hour;//日小時平均濕度
//        private float temperature_avg_hour;//日小時平均溫度
//        private float echo_avg_hour;//日小時平均水位空高
//        private float waterSpeedAquark_avg_hour;//日小時平均水流速
//        private float v1_avg_hour;//日小時平均鋰電池電壓
//        private float v2_avg_hour;
//        private float v3_avg_hour;
//        private float v4_avg_hour;
//        private float v5_avg_hour;//日小時平均太陽能板 1
//        private float v6_avg_hour;//日小時平均太陽能板 1
//        private float v7_avg_hour;


    }
