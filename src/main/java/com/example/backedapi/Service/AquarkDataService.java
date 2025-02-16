package com.example.backedapi.Service;

import com.example.backedapi.Repository.AquarkDataRepository;
import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.WebSocket.AlarmWebSocket;
import com.example.backedapi.model.db.AquarkData;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(value = "aquarkData", key = "#aquarkData.station_id + '_' + #aquarkData.trans_time.toString()")
    public AquarkData insertAquarkData(AquarkData aquarkData) {
        // 更新數據庫
        List<AquarkData> aquarkDataList = aquarkDataRepository.findAquarkDataByStation_idAndTrans_time(aquarkData.getStation_id(), aquarkData.getTrans_time());
        if (aquarkDataList.isEmpty()){
            aquarkDataRepository.save(aquarkData);
            return aquarkData;
        }
        AquarkData aquarkDataNeedUpdateData = aquarkDataList.getFirst();
        aquarkDataNeedUpdateData.setCSQ(aquarkData.getCSQ());
        aquarkDataNeedUpdateData.setRain_d(aquarkData.getRain_d());
        aquarkDataNeedUpdateData.setMoisture(aquarkData.getMoisture());
        aquarkDataNeedUpdateData.setTemperature(aquarkData.getTemperature());
        aquarkDataNeedUpdateData.setEcho(aquarkData.getEcho());
        aquarkDataNeedUpdateData.setWaterSpeedAquark(aquarkData.getWaterSpeedAquark());
        aquarkDataNeedUpdateData.setV1(aquarkData.getV1());
        aquarkDataNeedUpdateData.setV2(aquarkData.getV2());
        aquarkDataNeedUpdateData.setV3(aquarkData.getV3());
        aquarkDataNeedUpdateData.setV4(aquarkData.getV4());
        aquarkDataNeedUpdateData.setV5(aquarkData.getV5());
        aquarkDataNeedUpdateData.setV6(aquarkData.getV6());
        aquarkDataNeedUpdateData.setV7(aquarkData.getV7());
        aquarkDataNeedUpdateData.setPeak(aquarkData.isPeak());
        aquarkDataRepository.save(aquarkDataNeedUpdateData);
        return aquarkDataNeedUpdateData;


    }
    @Cacheable(value = "aquarkData", key = "#aquarkData.station_id + '_' + #aquarkData.trans_time.toString()")
    public AquarkData getAquarkData(AquarkData aquarkData) {
        return aquarkDataRepository.findAquarkDataByStation_idAndTrans_time(aquarkData.getStation_id(), aquarkData.getTrans_time()).getFirst();
    }


}
