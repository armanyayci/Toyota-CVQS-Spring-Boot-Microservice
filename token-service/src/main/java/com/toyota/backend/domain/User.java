package com.toyota.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class represents a User entity in the database.
 */
@Data
@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    /**
     * The unique identifier for the User entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The name of the User, which is not nullable and can have a maximum length of 30 characters.
     */
    @Column(name = "Name", nullable = false, length = 30)
    private String name;
    /**
     * The username of the User, which is not nullable and can have a maximum length of 50 characters.
     */
    @Column(name = "Username", nullable = false, length = 50)
    private String username;
    /**
     * The password of the User, which is not nullable and can have a maximum length of 100 characters.
     */
    @Column(name = "Password", nullable = false, length = 100)
    private String password;
    /**
     * The email of the User, which is not nullable and can have a maximum length of 50 characters.
     */
    @Column(name = "Email", nullable = false, length = 50)
    private String email;
    /**
     * Flag indicating whether the User is active or not.
     */
    @Column(name = "isActive")
    private boolean isActive;
    /**
     * Represents a many-to-many relationship between users and roles.
     * <p>
     * The {@code User} entity is linked to the {@code Role} entity through this attribute.
     * This attribute is annotated with {@code @ManyToMany} to indicate that many users can have many roles.
     * The {@code CascadeType.ALL} property ensures that any changes made to a role in this relationship are propagated to all the users that have the role.
     * The {@code FetchType.EAGER} property indicates that the roles of a user should be loaded eagerly.
     * @see Role
     * */
    @ManyToMany(cascade = CascadeType.ALL
            , fetch = FetchType.EAGER)
    @JoinTable(name = "user_role"
            , joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonIgnore
    private List<Role> roles;

    /**
     * Returns the list of GrantedAuthority objects associated with the User's roles.
     * @return List of GrantedAuthority objects
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
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
    /**
     * Returns a boolean indicating whether the User's account is enabled.
     * @return boolean indicating whether the User's account is enabled
     */
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}













