package com.example.backedapi.controller;

import com.example.backedapi.Service.AlertCheckLimitService;
import com.example.backedapi.Service.AquarkDataService;
import com.example.backedapi.model.Vo.AlertCheckLimitVo;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.aquarkUse.AquarkDataRaw;
import com.example.backedapi.model.Vo.aquarkUse.CriteriaAPIFilter;
import com.example.backedapi.model.db.AlertCheckLimit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/backend/aquarkData")
public class AquarkDataController {
    private final AquarkDataService aquarkDataService;

    @PostMapping("/getData")
    public ResponseType<List<AquarkDataRaw>> getData(@RequestBody List<CriteriaAPIFilter> fillterList) {


        return new ResponseType<>(aquarkDataService.getAquarkDataWithFilter(fillterList));
    }

    @GetMapping("/getColumnNameList")
    public ResponseType<List<String>> getColumnNameList() {
        return new ResponseType<>(aquarkDataService.getColumnNameList());
    }
}
