package com.rpms.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) 
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Nested Enum
    public enum UserRole {
        TENANT, LANDLORD, ADMIN
    }

    // ============================================================
    // 🔥 INTERVIEW DRILL: Spring Security UserDetails Methods
    // These methods map our User entity to Spring Security's internal model.
    // ============================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converts our Role (TENANT, LANDLORD, ADMIN) to a Spring Security Authority.
        // Interview Answer: "We use SimpleGrantedAuthority because it's the standard
        // implementation. This allows us to use @PreAuthorize('hasRole('TENANT')')."
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        // Spring Security uses "username" for login. We map it to our email field.
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // We don't handle account expiry in this version.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // We don't lock accounts in this version.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // We don't expire credentials.
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // Maps to our database "enabled" column.
    }
}