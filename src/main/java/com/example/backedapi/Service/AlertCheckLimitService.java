package com.example.backedapi.Service;

import com.example.backedapi.Repository.AlertCheckLimitRepository;
import com.example.backedapi.Util.AlarmMessage;
import com.example.backedapi.WebSocket.AlarmWebSocket;
import com.example.backedapi.model.db.AlertCheckLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertCheckLimitService {

    @Autowired
    private AlertCheckLimitRepository alertCheckLimitRepository;

    @Cacheable(value = "alertCheckLimit",key = "#tableName + '.' + #column" )
    public AlertCheckLimit getLimit(String tableName, String column) {
        return alertCheckLimitRepository.findAlertCheckLimitByTableNameAndColumnName(tableName,column).getFirst();
    }

    @CachePut(value = "alertCheckLimit", key = "#tableName + '.' + #column")
    public AlertCheckLimit insertLimit(String tableName, String column, double limitValue) {
        AlertCheckLimit alertCheckLimit = new AlertCheckLimit();
        alertCheckLimit.setTableName(tableName);
        alertCheckLimit.setColumnName(column);
        alertCheckLimit.setLimitValue(limitValue);
        List<AlertCheckLimit> limitList= alertCheckLimitRepository.findAlertCheckLimitByTableNameAndColumnName(alertCheckLimit.getTableName(),alertCheckLimit.getColumnName());
        if (limitList.isEmpty()) {
            AlertCheckLimit limit= alertCheckLimitRepository.save(alertCheckLimit);
            return limit;
        }
        AlertCheckLimit limit= limitList.getFirst();
        limit.setLimitValue(limitValue);
        alertCheckLimitRepository.save(limit);
        return limit;
    }
    @CachePut(value = "alertCheckLimit", key = "#alertCheckLimit.tableName + '.' + #alertCheckLimit.columnName")
    public AlertCheckLimit update(AlertCheckLimit alertCheckLimit) {
        return alertCheckLimitRepository.save(alertCheckLimit);
    }

    public List<AlertCheckLimit> getLimit() {
        return alertCheckLimitRepository.findAll();
    }

    @CacheEvict(value = "alertCheckLimit", key = "#alertCheckLimit.tableName + '.' + #alertCheckLimit.columnName")
    public void deleteLimit(AlertCheckLimit alertCheckLimit) {
        alertCheckLimitRepository.delete(alertCheckLimit);
    }


}
