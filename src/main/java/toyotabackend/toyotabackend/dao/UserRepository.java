package toyotabackend.toyotabackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    Optional<User> findByusername(String username);

    @Query("SELECT e FROM User e WHERE e.isActive = true")
    List<User>getActiveUsers();

}
