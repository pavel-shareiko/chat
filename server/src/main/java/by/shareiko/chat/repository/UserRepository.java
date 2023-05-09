package by.shareiko.chat.repository;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.user.SimpleUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<SimpleUserDTO> findDistinctByUsernameContainsIgnoreCase(@Nullable String username);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
