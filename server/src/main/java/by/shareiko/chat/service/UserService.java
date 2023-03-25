package by.shareiko.chat.service;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.security.user.RegisterUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    User register(RegisterUser registerUser);

    Optional<User> findByUsername(String username);

    boolean isUsernameUnique(String username);

    void delete(Long id);
}
