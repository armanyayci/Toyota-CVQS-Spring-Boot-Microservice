package com.toyota.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.Terminal.TerminalCategory;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This interface extends the JpaRepository interface
 * and defines methods for managing TerminalCategory data in the database.
 */
@Repository
public interface TerminalCategoryRepository extends JpaRepository<TerminalCategory,Integer> {
    /**
     * Retrieves a page of TerminalCategory entities based on a filterCategory string and pageable object.
     * @param filterCategory the name of the TerminalCategory to filter by
     * @param paging the pageable object that contains the page number, page size, and sort information
     * @return a page of TerminalCategory entities that match the filterCategory string and pageable object
     */
    Page<TerminalCategory> findAllByname(String filterCategory, Pageable paging);
}
