package com.example.backedapi.Repository;

import com.example.backedapi.model.Role;
import com.example.backedapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("select r from Role r where r.key in ?1")
    List<Role> findRoleByKeyIn(List<UUID> keys);
}
