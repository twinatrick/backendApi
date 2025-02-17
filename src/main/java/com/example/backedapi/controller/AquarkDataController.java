package com.example.backedapi.controller;

import com.example.backedapi.Service.AlertCheckLimitService;
import com.example.backedapi.model.Vo.AlertCheckLimitVo;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.db.AlertCheckLimit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/backend/aquarkData")
public class AquarkDataController {
    private final AlertCheckLimitService alertCheckLimitService;


    @PostMapping("/add")
    public ResponseType<AlertCheckLimitVo> addLimit(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            AlertCheckLimit  data=   alertCheckLimitService.insertLimit(alertCheckLimitVo.getTableName(),alertCheckLimitVo.getColumnName(),alertCheckLimitVo.getLimitValue());
            return new ResponseType<>( 0,data.toVo());
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error adding limit");
        }
    }
    @GetMapping("/get")
    public ResponseType<List<AlertCheckLimitVo>> getLimit() {
        return new ResponseType<>( 0,alertCheckLimitService.getLimit().stream().map(AlertCheckLimit::toVo).toList());
    }
    @PostMapping("/update")
    public ResponseType<AlertCheckLimitVo> updateLimit(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            return new ResponseType<>( 0,alertCheckLimitService.update(alertCheckLimitVo.toDb()).toVo());
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error updating limit");
        }
    }

    @PostMapping("/delete")
    public ResponseType<String> deleteLimit(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            alertCheckLimitService.deleteLimit(alertCheckLimitVo.toDb());
        }catch (Exception e){
            return new ResponseType<>( -1,"Error deleting limit");
        }

        return new ResponseType<>( 0,"limit deleted successfully");
    }
}
