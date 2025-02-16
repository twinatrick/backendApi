package com.example.backedapi.Repository;

import com.example.backedapi.model.db.SkillMapUserAndProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SkillMapUserAndProjectRepository extends JpaRepository<SkillMapUserAndProject, UUID> {
    @Modifying
    @Query("delete from SkillMapUserAndProject s where s.project = ?1")
    void deleteByProjectKey(UUID key);
    @Modifying
    @Query("delete from SkillMapUserAndProject s where s.user = ?1")
    void deleteByUserKey(UUID key);
    @Modifying
    @Query("delete from SkillMapUserAndProject s where s.skill = ?1")
    void deleteBySkillKey(UUID key);
}
