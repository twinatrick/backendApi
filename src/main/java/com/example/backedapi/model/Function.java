package com.example.backedapi.model;

import com.example.backedapi.model.Vo.FunctionVo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Function implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id ;
    private String name="";
    private String parent="";
    private String sort="";
    private Integer type;
    @JsonIgnore
    @OneToMany(mappedBy = "function", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<RoleFunction> roleFunctions =new ArrayList<>();
    private String createdBy;
    private String updatedBy;
    private Date createdTime;
    private Date updatedTime;
    public FunctionVo toVo(){
        FunctionVo vo = new FunctionVo();
        vo.setId(this.id.toString());
        vo.setName(this.name);
        vo.setParent(this.parent);
        vo.setSort(this.sort);
        vo.setType(this.type);
        vo.setParentName(this.parent);
        vo.setGrandParentId("");
        vo.setDisabled(false);
        vo.setEdit(false);
        vo.setNewAdd(false);
        vo.setNewName(this.name);
        vo.setDelete(false);
        return vo;
    }
}
