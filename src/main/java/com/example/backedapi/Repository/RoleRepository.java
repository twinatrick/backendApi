package com.example.backedapi.Repository;

import com.example.backedapi.model.Role;
import com.example.backedapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
