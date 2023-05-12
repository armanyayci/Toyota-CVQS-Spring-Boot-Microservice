package com.toyota.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.Entity.Role;
/**
 * This interface extends the JpaRepository interface
 * and defines methods for managing Role data in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}
