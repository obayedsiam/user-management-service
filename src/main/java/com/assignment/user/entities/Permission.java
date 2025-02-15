package com.assignment.user.entities;

import com.assignment.user.enums.PermissionEnum;
import jakarta.persistence.*;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PermissionEnum permissionName;
}

