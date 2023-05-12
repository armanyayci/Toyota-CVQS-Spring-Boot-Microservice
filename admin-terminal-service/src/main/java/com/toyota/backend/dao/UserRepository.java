package com.toyota.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.Entity.User;

import java.util.List;
import java.util.Optional;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This interface extends the JpaRepository interface
 * and defines methods for managing User data in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    /**
     * Finds a User object by "username" in the database.
     * @param username the username of the User object to find
     * @return Optional object containing the found User object, or an empty Optional if not found
     */
    Optional<User> findByusername(String username);

    /**
     * Check a User object by "username" in the database.
     * @param username the username of the User object to check.
     * @return true if the user is existed in database, false otherwise
     */
    Boolean existsByusername(String username);

    /**
     * Check a User object by "email" in the database.
     * @param email the email of the User object to check.
     * @return true if the user is existed in database, false otherwise
     */
    Boolean existsByemail(String email);

    /**
     * Retrieves a list of active User objects from the database.
     * @return List of active User objects
     */
    @Query("SELECT e FROM User e WHERE e.isActive = true")
    Page<User>getActiveUsers(Pageable paging);

    /**
     * Retrieves a list of all User objects with filter name from the database.
     * @return List of User objects
     */
    @Query("select t from User t where t.name =:filterName")
    Page<User> findUserByFilter(@Param("filterName") String filterName, Pageable paging);

    /**
     * Retrieves a list of Active User objects with filter name from the database.
     * @return List of User objects
     */

    @Query("SELECT t FROM User t WHERE t.isActive = true AND t.name =:filterName")
    Page<User> findActiveUserByFilter(@Param("filterName") String filterName, Pageable paging);
}
