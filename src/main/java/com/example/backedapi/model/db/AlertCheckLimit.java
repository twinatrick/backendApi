package com.example.backedapi.model.db;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AlertCheckLimit  implements Serializable {
    @Id
    @GeneratedValue
    private UUID key;
    private String tableName;
    private String columnName;

    private double limitValue;
}
