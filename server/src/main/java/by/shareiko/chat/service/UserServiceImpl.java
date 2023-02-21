package by.shareiko.chat.service;

import by.shareiko.chat.domain.Role;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.RegisterUser;
import by.shareiko.chat.mapper.UserMapper;
import by.shareiko.chat.repository.RoleRepository;
import by.shareiko.chat.repository.UserRepository;
import by.shareiko.chat.security.RoleConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User register(RegisterUser registerUser) {
        User user = userMapper.registerUserToUser(registerUser);

        Role userRole = roleRepository.findByName(RoleConstants.ROLE_USER);
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
    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public void delete(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User user = userOpt.get();
        user.setActive(false);
        userRepository.save(user);

        log.debug("User with id {} was successfully deleted", id);
    }
}
