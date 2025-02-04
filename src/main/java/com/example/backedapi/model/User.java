package com.example.backedapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.AccessType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "Member")
@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID key ;
    private String name;
    private String email;
    private String password;
    private String phone;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
    @CreatedDate
    private Date createdTime;
    @LastModifiedDate
    private Date updatedTime;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserRole> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SkillMapUserAndProject> skillMapUserAndProjects;
    @Transient
    private List<String> roleArr;

    @Transient
    private List<Function> permissions;

}
