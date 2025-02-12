package com.example.backedapi.model;

import com.example.backedapi.model.Vo.UserVo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.AccessType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "Member")
@Getter
@Setter
@Entity
@NoArgsConstructor

public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID key ;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;
    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<UserRole> roles =new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<SkillMapUserAndProject> skillMapUserAndProjects =new ArrayList<>();
    @Transient
    private List<String> roleArr;

    @JsonIgnore
    @Transient
    private List<Function> permissions;

    public UserVo toUserVo(){
        UserVo userVo = new UserVo();
        userVo.setKey(this.key);
        userVo.setName(this.name);
        userVo.setEmail(this.email);
        userVo.setPhone(this.phone);
        userVo.setCreatedBy(this.createdBy);
        userVo.setUpdatedBy(this.updatedBy);
        userVo.setCreatedTime(this.createdTime);
        userVo.setUpdatedTime(this.updatedTime);
        roleArr=roles.stream().map(userRole -> userRole.getRole().getKey().toString()).toList();
        userVo.setRoleArr(this.roleArr);
        userVo.setPermissions(this.permissions);
        return userVo;
    }

}
