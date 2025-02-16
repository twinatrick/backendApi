package com.example.backedapi.Service;

import com.example.backedapi.Repository.AquarkDataRepository;
import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.WebSocket.AlarmWebSocket;
import com.example.backedapi.model.db.AquarkData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AquarkDataService {
    @Autowired
    private    AquarkDataRepository aquarkDataRepository;

    public boolean insertAquarkData(List<AquarkData> aquarkDataList) {
    // 更新數據庫
        aquarkDataRepository.saveAll(aquarkDataList);
    return true;
    }
    @CachePut(value = "aquarkData", key = "aquarkData.getStation_id()+aquarkData.getTrans_time().toString()")
    public AquarkData insertAquarkData(AquarkData aquarkData) {
        // 更新數據庫
        aquarkDataRepository.save(aquarkData);
        return aquarkData;
    }
    public AquarkData getAquarkData(String stationId, String transTime) {
        Date transTimeDate = new Date(transTime);
        return aquarkDataRepository.findAquarkDataByStation_idAndTrans_time(stationId, transTimeDate);
    }


}
