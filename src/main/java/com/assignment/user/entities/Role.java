package com.assignment.user.entities;

import com.assignment.user.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private RoleEnum roleName;
}
