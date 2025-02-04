package com.example.backedapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID key ;
    //skill Name
    private String name;
    //專案描述
    private String description;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
    @CreatedDate
    private Date createdTime;
    @LastModifiedDate
    private Date updatedTime;
    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SkillMapUserAndProject> skillMapUserAndProjects;
}
