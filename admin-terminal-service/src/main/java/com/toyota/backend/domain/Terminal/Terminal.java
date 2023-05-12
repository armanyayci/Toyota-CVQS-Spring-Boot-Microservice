package com.toyota.backend.domain.Terminal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Represents a terminal entity with its name and category.
 */
@Entity
@Table(name = "Terminal")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Terminal {
    /**
     * The unique identifier of the terminal.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The name of the terminal, which is not nullable and can have a maximum length of 30 characters.
     */
    @Column(name = "Name",nullable = false, length = 30)
    private String name;
    /**
     * The category of the terminal, represented as a many-to-one relationship with the {@link TerminalCategory} entity.
     * Cascading is set to ALL, fetching is set to EAGER, and it is not nullable.
     * @see TerminalCategory
     */
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false)
    private TerminalCategory category;
}