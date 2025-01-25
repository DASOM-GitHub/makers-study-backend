package dmu.dasom.miniproject.repository;

import dmu.dasom.miniproject.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String userEmail);

    boolean existsByEmail(String email);
}
