package com.example.backedapi.Service;

import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.Util.CallApi;
import com.example.backedapi.model.Vo.aquarkUse.RowData;
import com.example.backedapi.model.Vo.aquarkUse.aquarkApiReturnVo;
import com.example.backedapi.model.Vo.aquarkUse.aquarkDataRaw;
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
        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.setLevel("INFO");
        alarmMessage.setMessage("getAquarkApiData :"+aquarkDataRawList.size());

        alarmMessage.setTimestamp(new Date());
        alarmService.processAlarm(alarmMessage);
        for (aquarkDataRaw data : aquarkDataRawList) {
            alarmMessage = new AlarmMessage();
            alarmMessage.setLevel("INFO");
            String dataString = objectMapper.writeValueAsString(data);
            alarmMessage.setMessage(dataString);
            alarmService.processAlarm(alarmMessage);
        }
    }
}
