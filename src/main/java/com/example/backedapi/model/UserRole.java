package com.example.backedapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class UserRole  implements Serializable {
    @Id
    @GeneratedValue
    private UUID key;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Role role;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;
}
