package com.toyota.backend.domain.Terminal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Represents a category for a Terminal.
 <p>
 * This class is an entity class mapped to the "TerminalCategory" table in the database. It contains an identifier field
 * and a name field for the category name, as well as a list of terminals belonging to this category. It also has
 * constructors, getter and setter methods for its fields.
 */
@Entity
@Table(name = "TerminalCategory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TerminalCategory {
    /**
     * The unique identifier for the TerminalCategory entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the TerminalCategory, which is not nullable and can have a maximum length of 30 characters.
     */
    @Column(name = "Name",nullable = false, length = 30)
    private String name;

    /**
     * The terminals of the category , represented as a OneToMany relationship with the {@link Terminal} entity.
     * Cascading is set to ALL, fetching is set to EAGER, and it is not nullable.
     * @see Terminal
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "category",
            targetEntity = Terminal.class)
    private List<Terminal> terminals;


}


