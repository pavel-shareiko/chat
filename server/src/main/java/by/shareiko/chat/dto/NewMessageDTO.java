package by.shareiko.chat.dto;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Message} entity
 */
@Data
public class NewMessageDTO implements Serializable {
    private final String content;
    private final Chat chat;
}