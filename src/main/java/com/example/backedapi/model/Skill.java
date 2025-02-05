package com.example.backedapi.model;

import com.example.backedapi.model.Vo.SkillVo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class Skill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID key ;
    //skill Name
    private String name;

    //技能描述
    private String description;
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;

    @JsonIgnore
    @OneToMany(mappedBy = "skill",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<SkillMapUserAndProject> skillMapUserAndProjects =new ArrayList<>();

    public SkillVo toVo(){
        SkillVo vo = new SkillVo();
        vo.setKey(this.key);
        vo.setName(this.name);
        vo.setDescription(this.description);
        vo.setCreatedBy(this.createdBy);
        vo.setUpdatedBy(this.updatedBy);
        vo.setCreatedTime(this.createdTime);
        vo.setUpdatedTime(this.updatedTime);
        return vo;
    }
}
