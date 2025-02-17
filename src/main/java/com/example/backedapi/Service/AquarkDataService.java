package com.example.backedapi.Service;

import com.example.backedapi.Repository.AquarkDataRepository;
import com.example.backedapi.model.Vo.aquarkUse.AquarkDataRaw;
import com.example.backedapi.model.Vo.aquarkUse.CriteriaAPIFilter;
import com.example.backedapi.model.db.AquarkData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AquarkDataService {
    @Autowired
    private AquarkDataRepository aquarkDataRepository;

    @Autowired
    private EntityManager entityManager;

    public List<AquarkDataRaw> getAquarkData() {
        return aquarkDataRepository.findAll().stream().map(AquarkData::toVo).collect(Collectors.toList());
    }

    public List<AquarkDataRaw> getAquarkDataWithFilter(List<CriteriaAPIFilter> fillterList) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AquarkData> query = cb.createQuery(AquarkData.class);
        Root<AquarkData> root = query.from(AquarkData.class);
        List<Predicate> predicates = new ArrayList<>();
        fillterList.forEach(f ->
        {
            String colName = f.getColumnName();
            if (f.getType() == 0) {
                if (f.isLike())
                    predicates.add(cb.like(root.get(colName), "%" + f.getString() + "%"));
                else if (f.isEqual()) {
                    predicates.add(cb.equal(root.get(colName), f.getString()));

                }
            } else if (f.getType() == 1) {
                if (f.isLarge() && f.isEqual()) {
                    cb.greaterThanOrEqualTo(root.get(colName), f.getDoubleValue());
                } else if (f.isLarge()) {
                    cb.greaterThan(root.get(colName), f.getDoubleValue());
                } else if (f.isSmall() && f.isEqual()) {
                    cb.lessThanOrEqualTo(root.get(colName), f.getDoubleValue());
                } else if (f.isSmall()) {
                    cb.lessThan(root.get(colName), f.getDoubleValue());
                } else if (f.isEqual()) {
                    cb.equal(root.get(colName), f.getDoubleValue());
                } else {
                    cb.notEqual(root.get(colName), f.getDoubleValue());
                }

            } else if (f.getType() == 2) {
                if (f.isLarge() && f.isEqual()) {
                    cb.greaterThanOrEqualTo(root.get(colName), f.getDate());
                } else if (f.isLarge()) {
                    cb.greaterThan(root.get(colName), f.getDate());
                } else if (f.isSmall() && f.isEqual()) {
                    cb.lessThanOrEqualTo(root.get(colName), f.getDate());
                } else if (f.isSmall()) {
                    cb.lessThan(root.get(colName), f.getDate());
                } else if (f.isEqual()) {
                    cb.equal(root.get(colName), f.getDate());
                } else {
                    cb.notEqual(root.get(colName), f.getDate());
                }
            } else if (f.getType()==3) {
                if (f.isEqual()) {
                    cb.equal(root.get(colName), f.isBooleanValue());
                } else {
                    cb.notEqual(root.get(colName), f.isBooleanValue());
                }
            }
        });
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList().stream().map(AquarkData::toVo).collect(Collectors.toList());
    }


    public boolean insertAquarkData(List<AquarkData> aquarkDataList) {
        // 更新數據庫
        aquarkDataRepository.saveAll(aquarkDataList);
        return true;
    }

    @CachePut(value = "aquarkData", key = "#aquarkData.station_id + '_' + #aquarkData.trans_time.toString()")
    public AquarkData insertAquarkData(AquarkData aquarkData) {
        // 更新數據庫
        List<AquarkData> aquarkDataList = aquarkDataRepository.findAquarkDataByStation_idAndTrans_time(aquarkData.getStation_id(), aquarkData.getTrans_time());
        if (aquarkDataList.isEmpty()) {
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
