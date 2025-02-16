package com.example.backedapi.Repository;

import com.example.backedapi.model.db.Role;
import com.example.backedapi.model.db.User;
import com.example.backedapi.model.db.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
//    @Modifying
//    @Query("delete from UserRole ur where ur.user IN ?1 and ur.role IN ?2")
//    void deleteByUserAndRole(List<UUID> userKey, List<UUID> roleKey);
//    @Modifying
//    @Query("delete from UserRole ur where ur.user IN ?1 and ur.role IN ?2")
//
    void deleteAllByUserInAndRoleIn(List<User> userKey, List<Role> roleKey);
}
