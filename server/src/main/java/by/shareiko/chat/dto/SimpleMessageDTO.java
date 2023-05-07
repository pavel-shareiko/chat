package by.shareiko.chat.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link by.shareiko.chat.domain.Message} entity
 */
@Data
public class SimpleMessageDTO implements Serializable {
    private final Long id;
    private final Long chatId;
    private final String content;
    private final SimpleUserDTO sender;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}