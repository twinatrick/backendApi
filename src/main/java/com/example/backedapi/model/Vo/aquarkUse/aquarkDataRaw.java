package com.example.backedapi.model.Vo.aquarkUse;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class aquarkDataRaw {
    @Id
    private UUID key;

    private String station_id;//站點
    private String CSQ;
    private String obs_time;
    private Date trans_time;
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
}
