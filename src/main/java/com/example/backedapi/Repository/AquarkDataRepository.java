package com.example.backedapi.Repository;

import com.example.backedapi.model.db.AquarkData;
import com.example.backedapi.model.db.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AquarkDataRepository extends JpaRepository<AquarkData, UUID> {

    @Query(value = "from AquarkData where station_id = ?1 AND  trans_time = ?2 ")
    AquarkData findAquarkDataByStation_idAndTrans_time(
            String station_id, Date trans_time);


}
