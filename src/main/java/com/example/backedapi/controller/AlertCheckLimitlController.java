package com.example.backedapi.controller;

import com.example.backedapi.Service.AlertCheckLimitService;
import com.example.backedapi.Service.SkillService;
import com.example.backedapi.model.Vo.AlertCheckLimitVo;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.SkillVo;
import com.example.backedapi.model.db.AlertCheckLimit;
import com.example.backedapi.model.db.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/backend/alertCheckLimit")
public class AlertCheckLimitlController {
    private final AlertCheckLimitService alertCheckLimitService;


    @PostMapping("/add")
    public ResponseType<AlertCheckLimitVo> addLimit(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            AlertCheckLimit  data=   alertCheckLimitService.insertLimit(alertCheckLimitVo.getTableName(),alertCheckLimitVo.getColumnName(),alertCheckLimitVo.getLimitValue());
            return new ResponseType<>( 0,data.toVo());
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error adding skill");
        }
    }
    @GetMapping("/get")
    public ResponseType<List<AlertCheckLimitVo>> getSkill() {
        return new ResponseType<>( 0,alertCheckLimitService.getLimit().stream().map(AlertCheckLimit::toVo).toList());
    }
    @PostMapping("/update")
    public ResponseType<String> updateSkill(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            alertCheckLimitService.update(alertCheckLimitVo.toDb());
        }catch (Exception e){
            return new ResponseType<>( -1,"Error updating skill");
        }

        return new ResponseType<>( 0,"Skill updated successfully");
    }

    @PostMapping("/delete")
    public ResponseType<String> deleteSkill(@RequestBody AlertCheckLimitVo alertCheckLimitVo) {
        try {
            alertCheckLimitService.deleteLimit(alertCheckLimitVo.toDb());
        }catch (Exception e){
            return new ResponseType<>( -1,"Error deleting skill");
        }

        return new ResponseType<>( 0,"Skill deleted successfully");
    }
}
