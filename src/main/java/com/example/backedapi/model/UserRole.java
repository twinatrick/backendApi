package com.example.backedapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@Entity
@Table
@Getter
@Setter
public class UserRole  implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Role role;
    @ManyToOne
    private User user;
}
