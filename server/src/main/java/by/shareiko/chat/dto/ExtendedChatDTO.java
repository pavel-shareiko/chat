package by.shareiko.chat.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ExtendedChatDTO {
    private Long chatId;
    private ChatType chatType;
    private SimpleMessageDTO lastMessage;
    private Set<SimpleUserDTO> participants;

    public enum ChatType {
        GROUP_CHAT,
        PERSONAL_CHAT,
        SELF_CHAT
    }
}
