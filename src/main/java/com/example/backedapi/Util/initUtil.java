package com.example.backedapi.Util;

import com.example.backedapi.Service.initAndCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class initUtil {
    @Autowired
    private static SpringUtil springUtil;
    public static void init() {
        springUtil.getBean(initAndCheckService.class).initAndCheck();
    }
}
