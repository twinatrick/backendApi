package com.example.backedapi.Service;

import com.example.backedapi.model.Vo.RoleOutVo;
import com.example.backedapi.model.db.AlertCheckLimit;
import com.example.backedapi.model.db.Function;
import com.example.backedapi.model.db.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class initAndCheckService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AlertCheckLimitService alertCheckLimitService;

    @Autowired
    private  FunctionService functionService;

    public void initAndCheck() {
        checkRole();

        checkLimit();
        checkFunctionBindDefaultRole();
        // 初始化告警設定
        // 1. 從資料庫讀取告警設定
        // 2. 將告警設定放入內存
        // 3. 啟動告警檢查任務
    }

    public void checkRole() {
        List<RoleOutVo> roleList = roleService.getRole();
        if (roleList.isEmpty()) {
            // 初始化角色
            Role role = new Role();
            role.setName("admin");
            roleService.addRole(role);
            role = new Role();
            role.setName("user");
            roleService.addRole(role);
        }
    }

    public void checkLimit() {
        List<AlertCheckLimit> alertCheckLimitList = alertCheckLimitService.getLimit();
        if (alertCheckLimitList.isEmpty()) {
            alertCheckLimitService.insertLimit("aquark_data", "rain_d", 10);
            alertCheckLimitService.insertLimit("aquark_data", "moisture", 10);
            alertCheckLimitService.insertLimit("aquark_data", "temperature", 10);
            alertCheckLimitService.insertLimit("aquark_data", "echo", 10);
            alertCheckLimitService.insertLimit("aquark_data", "water_speed_aquark", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v1", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v2", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v3", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v4", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v5", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v6", 10);
            alertCheckLimitService.insertLimit("aquark_data", "v7", 10);
        }else {
            String[] aquark_data_column = {"rain_d", "moisture", "temperature", "echo", "water_speed_aquark", "v1", "v2", "v3", "v4", "v5", "v6", "v7"};
            Arrays.stream(aquark_data_column).forEach(s -> {
                AlertCheckLimit alertCheckLimit = alertCheckLimitService.getLimit("aquark_data", s);
                if (alertCheckLimit == null) {
                    alertCheckLimitService.insertLimit("aquark_data", s, 10);
                }
            });
        }
    }
    public boolean checkIsExist(String oneLayer, String twoLayer, String threeLayer) {
        Function one= functionService.getFunctionByName(oneLayer);
        if (one == null) {
            return false;
        }
        Function two = functionService.getFunctionByNameAndParent(twoLayer, one.getId().toString());

        if (two == null) {
            return false;
        }
        Function three = functionService.getFunctionByNameAndParent(threeLayer, two.getId().toString());
        if (three == null) {
            return false;
        }
        return true;
    }

    public void checkFunctionBindDefaultRole() {
        List<Function> functionList = functionService.getFunction();
        List<List<String>> allFunctionList = new ArrayList<>();
        allFunctionList.add(new ArrayList<>( List.of("System", "User", "View")));
        allFunctionList.add(new ArrayList<>( List.of("System", "RolePermission", "View")));
        allFunctionList.add(new ArrayList<>( List.of("DataView", "AquarkData", "View")));
        allFunctionList.add(new ArrayList<>( List.of("DataView", "AquarkDataAvg", "View")));
        allFunctionList.add(new ArrayList<>( List.of("System", "LimitSetting", "View")));


        for (List<String> functionListStr : allFunctionList) {
            if (!checkIsExist(functionListStr.get(0), functionListStr.get(1), functionListStr.get(2))) {
                insertFunctionByList(functionListStr,"");
            }
        }
        functionList= functionService.getFunction();
        Role role = roleService.getRoleByName("admin");
        roleService.roleBindFunction(role,functionList);
    }
    public void insertFunctionByList(List<String> functionList , String parent) {
        if (functionList.isEmpty()) {
            return;
        }
        Function sameFunction = functionService.getFunctionByNameAndParent(functionList.getFirst(), parent);
        if (sameFunction != null) {
            insertFunctionByList(functionList.subList(1, functionList.size()), sameFunction.getId().toString());
        }else {
            Function f = new Function();
            f.setName(functionList.getFirst());
            f.setParent(parent);
            f=functionService.addFunction(f);
            insertFunctionByList(functionList.subList(1, functionList.size()), f.getId().toString());
        }

    }

}
