package com.mgaye.bsys.security;

import com.mgaye.bsys.model.User;
import com.mgaye.bsys.model.User.KycStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String id;
    private final String email;
    private String password; // Mutable for credential erasure
    private final KycStatus kycStatus;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserDetailsImpl(String id, String email, String password,
            KycStatus kycStatus,
            Collection<? extends GrantedAuthority> authorities,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kycStatus = kycStatus;
        this.authorities = Collections.unmodifiableSet(
                authorities.stream()
                        .collect(Collectors.toSet()));
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static UserDetailsImpl build(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getKycStatus(),
                authorities,
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled());
    }

    // Critical security method
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getId() {
        return id;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
        // return true;

    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
        // return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired; // FIXED
    }

    @Override
    public boolean isEnabled() {
        return enabled && kycStatus == KycStatus.VERIFIED; // FIXED
    }

    // Banking extensions
    public BigDecimal getDailyTransactionLimit() {
        return kycStatus == KycStatus.VERIFIED ? new BigDecimal("50000") : new BigDecimal("5000");
    }
}