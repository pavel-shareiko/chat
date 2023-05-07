package by.shareiko.chat.dto;

import by.shareiko.chat.dto.user.SimpleUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExtendedChatDTO {
    private Long chatId;
    private String displayName;
    private ChatType chatType;
    private SimpleMessageDTO lastMessage;
    private List<SimpleUserDTO> participants;

    public enum ChatType {
        GROUP_CHAT,
        PERSONAL_CHAT,
        SELF_CHAT
    }
}
