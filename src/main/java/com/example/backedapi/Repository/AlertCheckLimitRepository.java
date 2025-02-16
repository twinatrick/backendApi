package com.example.backedapi.Repository;

import com.example.backedapi.model.db.AlertCheckLimit;
import com.example.backedapi.model.db.AquarkData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AlertCheckLimitRepository extends JpaRepository<AlertCheckLimit, UUID> {

        @Query(value = "from AlertCheckLimit where tableName = ?1 AND  columnName = ?2 ")
        List<AlertCheckLimit> findAlertCheckLimitByTableNameAndColumnName(
                String tableName, String columnName);



}
