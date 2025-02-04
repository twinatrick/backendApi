package com.example.backedapi.model.Vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindUserSkillOrProject{
    private String userId;
    private String skill;
    private String projectId;
    private String type;
}