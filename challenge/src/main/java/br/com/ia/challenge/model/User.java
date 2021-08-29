package br.com.ia.challenge.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user_t")
@EntityListeners(AuditingEntityListener.class)
@Getter  @Setter @EqualsAndHashCode @Builder @NoArgsConstructor @AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank(message = "The field name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "The field login is required")
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank(message = "The field password is required")
    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedDate;

    @NotBlank(message = "The field github is required")
    @Column(nullable = false, unique = true)
    private String github;

    @NotBlank(message = "The field email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean admin;

    public UserDTO getDTO() {
        return UserDTO.builder()
                .id(id)
                .name(name)
                .login(login)
                .github(github)
                .email(email)
                .admin(admin)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
