package toyotabackend.toyotabackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    Optional<User> findByusername(String username);

}
