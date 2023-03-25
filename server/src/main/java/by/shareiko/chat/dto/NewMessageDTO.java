package by.shareiko.chat.dto;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Message} entity
 */
@Data
public class NewMessageDTO implements Serializable {
    private final String content;
    private final SimpleUserDTO sender;
    private final Chat chat;
}