package com.toyota.backend.domain.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class represents a Role entity in the database.
 */
@Table(name = "Roles")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    /**
     * The unique identifier for the Role entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The name of the Role, which is not nullable.
     */
    @Column(name = "Name",nullable = false)
    private String name;

    /**
     * Represents a list of users associated with a specific role.
     * The list of users is defined by a many-to-many relationship, using the {@link javax.persistence.ManyToMany} annotation.
     * The relationship is mapped by the "roles" attribute of the {@link User} entity.
     * This entity is used to define the association between roles and users in the system.
     * The association allows for easy retrieval of all users that have a certain role.
     * @see User
     */
    @ManyToMany(cascade = CascadeType.ALL
            ,mappedBy = "roles" )
    private List<User> users;

}
