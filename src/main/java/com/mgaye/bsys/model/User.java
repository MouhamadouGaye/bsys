package com.mgaye.bsys.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate dob;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @Column(name = "bank_account")
    // private List<BankAccount> accounts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BankAccount> accounts = new ArrayList<>();

    @Column(nullable = false)
    private boolean isEnable;

    @Column(nullable = false)
    private boolean active;

    private String deviceToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSettings userSettings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SecuritySettings securitySettings;

    // In User.java
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NotificationPreferences notificationPreferences;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    // Helper method to ensure settings exist
    public UserSettings getUserSettings() {
        if (this.userSettings == null) {
            this.userSettings = new UserSettings();
            this.userSettings.setUser(this);
        }
        return this.userSettings;
    }

    public enum KycStatus {
        VERIFIED, PENDING, REJECTED
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    // Add account management methods
    public void addAccount(BankAccount account) {
        accounts.add(account);
        account.setUser(this);
    }

    public void removeAccount(BankAccount account) {
        accounts.remove(account);
        account.setUser(null);
    }
}
