package by.shareiko.chat.controller;

import by.shareiko.chat.dto.user.SimpleUserDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private static final int MIN_USERNAME_SEARCH_LENGTH = 5;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<SimpleUserDTO>> findUsers(@RequestParam("username") String username) {
        log.info("Request to find all users by username [{}]", username);
        if ((StringUtils.isBlank(username) || username.length() < MIN_USERNAME_SEARCH_LENGTH) && !username.startsWith("#")) {
            throw new BadRequestException("Username should contain at least " + MIN_USERNAME_SEARCH_LENGTH + " characters");
        }
        return ResponseEntity.ok(userService.findAllByUsernamePattern(username));
    }
}
