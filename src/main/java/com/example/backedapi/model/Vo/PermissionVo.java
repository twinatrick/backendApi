package com.example.backedapi.model.Vo;

import com.example.backedapi.model.Function;
import com.example.backedapi.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.backedapi.model.User;

import java.util.List;

@Getter
@Setter@NoArgsConstructor
public class PermissionVo {
    private String user;
    private List<String> userList;
    private String function;
    private List<String> functionList;
    private String role;
    private List<String> roleList;
}
