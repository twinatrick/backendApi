package com.example.backedapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table
public class SkillMapUserAndProject  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID key ;
    @ManyToOne
    private User user;
    @ManyToOne
    private Skill skill;
    @ManyToOne
    private Project project;
}
