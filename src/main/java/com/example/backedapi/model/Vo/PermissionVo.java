package com.example.backedapi.model.Vo;

import com.example.backedapi.model.Function;
import com.example.backedapi.model.Role;
import lombok.Getter;
import lombok.Setter;
import com.example.backedapi.model.User;

import java.util.List;

@Getter
@Setter
public class PermissionVo {
    private User user;
    private List<User> userList;
    private Function function;
    private List<Function> functionList;
    private Role role;
    private List<Role> roleList;
}
