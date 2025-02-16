package com.example.backedapi.model.Vo;

import com.example.backedapi.model.db.AlertCheckLimit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
public class AlertCheckLimitVo implements Serializable {

    private UUID key;
    private String tableName;
    private String columnName;

    private double limitValue;

    public AlertCheckLimitVo(UUID key, String tableName, String columnName, double limitValue) {
        this.key = key;
        this.tableName = tableName;
        this.columnName = columnName;
        this.limitValue = limitValue;
    }
    public AlertCheckLimit toDb() {
        AlertCheckLimit alertCheckLimit = new AlertCheckLimit();
        alertCheckLimit.setKey(this.key);
        alertCheckLimit.setTableName(this.tableName);
        alertCheckLimit.setColumnName(this.columnName);
        alertCheckLimit.setLimitValue(this.limitValue);
        return alertCheckLimit;
    }


}
