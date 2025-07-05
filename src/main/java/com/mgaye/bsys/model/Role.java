package com.mgaye.bsys.model;

import java.security.Permission;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    private Set<Permission> permissions;

    private String description;

    public Role(String name) {
        this.name = ERole.valueOf(name);
    }

    public Role(String name, String description) {
        this.name = ERole.valueOf(name);
        this.description = description;
    }

    public Role(ERole name, String description) {
        this.name = name;
        this.description = description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public enum ERole {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_MODERATOR,
        ROLE_AUDITOR
    }
}