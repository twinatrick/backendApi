package com.example.backedapi.Repository;

import com.example.backedapi.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FunctionRepository extends JpaRepository<Function, UUID> {
}
