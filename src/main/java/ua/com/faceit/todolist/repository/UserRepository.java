package ua.com.faceit.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.faceit.todolist.data.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    Optional<User> findByUsername(String username);

    Optional<User> findByConfirmationToken(String confirmationToken);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}