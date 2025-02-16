package com.example.backedapi.Repository;

import com.example.backedapi.model.db.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByName(String name);
}
