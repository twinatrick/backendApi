package com.example.backedapi.Service;

import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.Util.CallApi;
import com.example.backedapi.model.Vo.aquarkUse.RowData;
import com.example.backedapi.model.Vo.aquarkUse.aquarkApiReturnVo;
import com.example.backedapi.model.Vo.aquarkUse.aquarkDataRaw;
import com.example.backedapi.model.db.AquarkData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckApiService {
    private final CallApi callApi;
    private  final ObjectMapper  objectMapper ;
    private  final AlarmService alarmService;
    private final AquarkDataService aquarkDataService;
    private final AlertCheckLimitService alertCheckLimitService;
    private final ProducerService producerService;
    public String getApiOnlyUrl(String url) throws IOException {
        return callApi.callGetApi(url);
    }
    public void getAquarkApiData() throws IOException {
        List<String> urlList = List.of(
                "https://app.aquark.com.tw/api/raw/Angle2024/240627",
                "https://app.aquark.com.tw/api/raw/Angle2024/240706",
                "https://app.aquark.com.tw/api/raw/Angle2024/240708",
                "https://app.aquark.com.tw/api/raw/Angle2024/240709",
                "https://app.aquark.com.tw/api/raw/Angle2024/240710");
        System.out.println("CheckApiService.getAquarkApiData");
        List<aquarkDataRaw> aquarkDataRawList = new ArrayList<>();
        for (String url : urlList) {
            String result = getApiOnlyUrl(url);
            aquarkApiReturnVo aquarkApiReturnVo =objectMapper.readValue(result, aquarkApiReturnVo.class);
            System.out.println("aquarkApiReturnVo Raw size:"+(aquarkApiReturnVo.getRaw().size()));
            aquarkDataRawList.addAll(aquarkApiReturnVo.getRaw().stream().map(RowData::transToDbData).toList());

        }
        List<AlarmMessage> alarmMessages = new ArrayList<>();
        for (aquarkDataRaw data : aquarkDataRawList) {
            AquarkData aquarkData= aquarkDataService.insertAquarkData(data.toDataBase());
            alarmMessages.addAll(checkValue(aquarkData)) ;
        }
        if (!alarmMessages.isEmpty()){
            alarmService.processAlarm(alarmMessages);
        }
    }
    List<AlarmMessage> checkValue(AquarkData data) {

        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.setLevel("ERROR");
        String message = data.getStation_id() + "Date:" + data.getTrans_time().toString();
        int alertLevel = 0;
        if (data.getV1() > alertCheckLimitService.getLimit("aquark_data", "v1").getLimitValue()) {
            message += "v1超出限制,";
            alertLevel++;

        }
        if (data.getV2() > alertCheckLimitService.getLimit("aquark_data", "v2").getLimitValue()) {
            message += "v2超出限制,";
            alertLevel++;

        }
        if (data.getV3() > alertCheckLimitService.getLimit("aquark_data", "v3").getLimitValue()) {
            message += "v3超出限制,";
            alertLevel++;

        }
        if (data.getV4() > alertCheckLimitService.getLimit("aquark_data", "v4").getLimitValue()) {
            message += "v4超出限制,";
            alertLevel++;

        }
        if (data.getV5() > alertCheckLimitService.getLimit("aquark_data", "v5").getLimitValue()) {
            message += "v5超出限制,";
            alertLevel++;


        }
        if (data.getV6() > alertCheckLimitService.getLimit("aquark_data", "v6").getLimitValue()) {
            message += "v6超出限制,";
            alertLevel++;


        }
        if (data.getV7() > alertCheckLimitService.getLimit("aquark_data", "v7").getLimitValue()) {
            message += "v7超出限制,";
            alertLevel++;

        }
        if (data.getTemperature() > alertCheckLimitService.getLimit("aquark_data", "temperature").getLimitValue()) {
            message += "temperature超出限制,";
            alertLevel++;


        }
        if (data.getMoisture() > alertCheckLimitService.getLimit("aquark_data", "moisture").getLimitValue()) {
            message += "moisture超出限制,";
            alertLevel++;


        }
        if (data.getRain_d() > alertCheckLimitService.getLimit("aquark_data", "rain_d").getLimitValue()) {
            message += "rain_d超出限制,";
            alertLevel++;

        }
        if (data.getWaterSpeedAquark() > alertCheckLimitService.getLimit("aquark_data", "water_speed_aquark").getLimitValue()) {
            message += "water_speed_aquark超出限制,";
            alertLevel++;

        }
        if (data.getEcho() > alertCheckLimitService.getLimit("aquark_data", "echo").getLimitValue()) {
            message += "echo超出限制,";
            alertLevel++;

        }
        if (alertLevel > 0) {
            alarmMessage.setMessage(message);
//            alarmService.processAlarm(alarmMessage);
            return List.of(alarmMessage);
        }
        return List.of();
    }




}
