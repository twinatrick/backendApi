package com.example.backedapi.model.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class RoleOutVo {
    private UUID key;
    private String name;
    private String description;
    private String permissions;
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;
    private List<String> functionKeys;
}
