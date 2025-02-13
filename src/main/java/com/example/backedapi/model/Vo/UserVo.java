package com.example.backedapi.model.Vo;

import com.example.backedapi.model.Function;
import com.example.backedapi.model.SkillMapUserAndProject;
import com.example.backedapi.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserVo {
    private String key;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean disabled;
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;


    private List<String> roleArr;

    private List<FunctionVo> permissions;
}
