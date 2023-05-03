package com.toyota.backend.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class represents a User entity in the database.
 */
@Data
@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
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
}
