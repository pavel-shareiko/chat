package by.shareiko.chat.service;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.user.LoginUser;
import by.shareiko.chat.dto.user.RegisterUser;
import by.shareiko.chat.dto.user.SimpleUserDTO;
import by.shareiko.chat.dto.user.UserWithAuthorities;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserWithAuthorities getCurrentUserWithAuthorities();

    User register(RegisterUser registerUser);

    User login(LoginUser loginUser);

    Optional<User> findByUsername(String username);

    List<SimpleUserDTO> findAllByUsernamePattern(String usernamePattern);

    boolean isUsernameUnique(String username);

    void delete(Long id);
}
