package by.shareiko.chat.service;

import by.shareiko.chat.domain.Role;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.user.RegisterUser;
import by.shareiko.chat.dto.user.SimpleUserDTO;
import by.shareiko.chat.dto.user.UserWithAuthorities;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.exception.UserUnauthorizedException;
import by.shareiko.chat.mapper.UserMapper;
import by.shareiko.chat.repository.UserRepository;
import by.shareiko.chat.security.RoleConstants;
import by.shareiko.chat.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserWithAuthorities getCurrentUserWithAuthorities() {
        return userMapper.userToUserWithAuthorities(SecurityUtils.getCurrentUser()
                .orElseThrow(() -> new UserUnauthorizedException("Current user is not logged in"))
                .getDomainUser()
        );
    }

    public User register(RegisterUser registerUser) {
        registerUser.setFirstName(StringUtils.capitalize(registerUser.getFirstName()));
        registerUser.setLastName(StringUtils.capitalize(registerUser.getLastName()));

        User user = userMapper.registerUserToUser(registerUser);

        Role userRole = roleService.findByName(RoleConstants.ROLE_USER);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        User registeredUser = userRepository.save(user);

        log.debug("Saved user with id: {}", registeredUser.getId());

        return registeredUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<SimpleUserDTO> findAllByUsernamePattern(String usernamePattern) {
        if (usernamePattern.startsWith("#")) {
            usernamePattern = usernamePattern.substring(1);
            try {
                long userId = Long.parseLong(usernamePattern);
                Optional<User> userById = userRepository.findById(userId);
                if (userById.isEmpty()) {
                    return List.of();
                }

                User user = userById.get();
                return List.of(userMapper.userToSimpleUserDTO(user));
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid user id '" + usernamePattern + "'");
            }
        }
        return userRepository.findTop10ByUsernameContainsIgnoreCase(usernamePattern);
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public void delete(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new NotFoundException("User with id " + id + " not found");
        }

        User user = userOpt.get();
        user.setActive(false);
        userRepository.save(user);

        log.debug("User with id {} was successfully deleted", id);
    }
}
