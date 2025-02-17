package com.example.backedapi.Service;

import com.example.backedapi.Repository.AquarkDataRepository;
import com.example.backedapi.model.Vo.aquarkUse.AquarkDataRaw;
import com.example.backedapi.model.Vo.aquarkUse.CriteriaAPIFilter;
import com.example.backedapi.model.Vo.aquarkUse.AverageAquark;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    public List<String> getColumnNameList() {
        Field[] declaredFields = AquarkData.class.getDeclaredFields();
        Field[] fields = AquarkData.class.getFields();
        List<String> columnNameList = new ArrayList<>();
        for (Field field : declaredFields) {
            columnNameList.add(field.getName());
        }
        for (Field field : fields) {
            columnNameList.add(field.getName());
        }


        return columnNameList;
    }

    public  List<AverageAquark> getAverageAquark(Date start, Date end) {
        CriteriaAPIFilter criteriaAPIFilterStart = new CriteriaAPIFilter();
        criteriaAPIFilterStart.setColumnName("trans_time");
        criteriaAPIFilterStart.setType(2);
        criteriaAPIFilterStart.setLarge(true);
        criteriaAPIFilterStart.setEqual(true);
        criteriaAPIFilterStart.setDate(start);
        CriteriaAPIFilter criteriaAPIFilterEnd = new CriteriaAPIFilter();
        criteriaAPIFilterEnd.setColumnName("trans_time");
        criteriaAPIFilterEnd.setType(2);
        criteriaAPIFilterEnd.setSmall(true);
        criteriaAPIFilterEnd.setEqual(true);
        criteriaAPIFilterEnd.setDate(end);
        List<CriteriaAPIFilter> criteriaAPIFilterList = new ArrayList<>();
        criteriaAPIFilterList.add(criteriaAPIFilterStart);
        criteriaAPIFilterList.add(criteriaAPIFilterEnd);
        List<AquarkDataRaw> rawList = getAquarkDataWithFilter(criteriaAPIFilterList);
        List<AverageAquark> avangeList = rawList.stream().map(AquarkDataRaw::toAverageAquark).toList();
        Map<String, List<AverageAquark>> collect = avangeList.stream()
                .collect(Collectors.groupingBy((a) -> a.getStation_id() + a.getDate()));
        avangeList = collect.values().stream().map(a -> {
            AverageAquark averageAquark = new AverageAquark();
            averageAquark.setStation_id(a.getFirst().getStation_id());
            averageAquark.setDate(a.getFirst().getDate());
            averageAquark.setRain_d((float) a.stream().mapToDouble(AverageAquark::getRain_d).max().orElse(0) / 24);
            averageAquark.setMoisture((float) a.stream().mapToDouble(AverageAquark::getMoisture).average().orElse(0));
            averageAquark.setTemperature((float) a.stream().mapToDouble(AverageAquark::getTemperature).average().orElse(0));
            averageAquark.setEcho((float) a.stream().mapToDouble(AverageAquark::getEcho).average().orElse(0));
            averageAquark.setWaterSpeedAquark((float) a.stream().mapToDouble(AverageAquark::getWaterSpeedAquark).average().orElse(0));
            averageAquark.setV1((float) a.stream().mapToDouble(AverageAquark::getV1).average().orElse(0));
            averageAquark.setV2((float) a.stream().mapToDouble(AverageAquark::getV2).average().orElse(0));
            averageAquark.setV3((float) a.stream().mapToDouble(AverageAquark::getV3).average().orElse(0));
            averageAquark.setV4((float) a.stream().mapToDouble(AverageAquark::getV4).average().orElse(0));
            averageAquark.setV5((float) a.stream().mapToDouble(AverageAquark::getV5).average().orElse(0));
            averageAquark.setV6((float) a.stream().mapToDouble(AverageAquark::getV6).average().orElse(0));
            averageAquark.setV7((float) a.stream().mapToDouble(AverageAquark::getV7).average().orElse(0));
            return averageAquark;
        }).toList();

        return avangeList;
    }

    public List<AquarkDataRaw> getAquarkDataWithFilter(List<CriteriaAPIFilter> fillterList) {
        if (fillterList.isEmpty()) {
            return getAquarkData();
        }
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
                    predicates.add(  cb.greaterThanOrEqualTo(root.get(colName), f.getDoubleValue()));
                } else if (f.isLarge()) {
                    predicates.add(cb.greaterThan(root.get(colName), f.getDoubleValue()));
                } else if (f.isSmall() && f.isEqual()) {
                    predicates.add(cb.lessThanOrEqualTo(root.get(colName), f.getDoubleValue()));
                } else if (f.isSmall()) {
                    predicates.add(cb.lessThan(root.get(colName), f.getDoubleValue()));
                } else if (f.isEqual()) {
                    predicates.add( cb.equal(root.get(colName), f.getDoubleValue()));
                } else {
                    predicates.add( cb.notEqual(root.get(colName), f.getDoubleValue()));
                }

            } else if (f.getType() == 2) {
                if (f.isLarge() && f.isEqual()) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get(colName), f.getDate()));
                } else if (f.isLarge()) {
                    predicates.add(cb.greaterThan(root.get(colName), f.getDate()));
                } else if (f.isSmall() && f.isEqual()) {
                    predicates.add(cb.lessThanOrEqualTo(root.get(colName), f.getDate()));
                } else if (f.isSmall()) {
                    predicates.add(cb.lessThan(root.get(colName), f.getDate()));
                } else if (f.isEqual()) {
                    predicates.add( cb.equal(root.get(colName), f.getDate()));
                } else {
                    predicates.add(cb.notEqual(root.get(colName), f.getDate()));
                }
            } else if (f.getType() == 3) {
                if (f.isEqual()) {
                    predicates.add(cb.equal(root.get(colName), f.isBooleanValue()));
                } else {
                    predicates.add( cb.notEqual(root.get(colName), f.isBooleanValue()));
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
        float abs = Math.abs(aquarkData.getWaterSpeedAquark());
        aquarkDataNeedUpdateData.setWaterSpeedAquark(abs);
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
