package com.example.backedapi.model.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter@NoArgsConstructor
public class ProjectVo {
    private UUID key ;
    //skill Name
    private String name;
    //專案描述
    private String description;
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;
}
