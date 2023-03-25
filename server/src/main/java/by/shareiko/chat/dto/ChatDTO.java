package by.shareiko.chat.dto;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import lombok.Data;

import java.util.Set;

@Data
public class ChatDTO {
    private Long chatId;
    private ChatType chatType;
    private Message lastMessage;
    private Set<User> participants;
}
