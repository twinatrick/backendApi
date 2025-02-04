package com.example.backedapi.Repository;

import com.example.backedapi.model.Skill;
import com.example.backedapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findByName(String name);
}
