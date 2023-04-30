package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.dto.SimpleUserDTO;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.*;
import org.springframework.security.core.parameters.P;

import java.util.Set;

import static by.shareiko.chat.dto.ExtendedChatDTO.ChatType.*;

@Log4j2
@Mapper(componentModel = "spring")
public abstract class ChatMapper {
    @BeforeMapping
    protected void determineChatType(Chat chat, @MappingTarget ExtendedChatDTO extendedChatDTO) {
        Set<User> participants = chat.getParticipants();
        if (participants.size() == 1) {
            extendedChatDTO.setChatType(SELF_CHAT);
            return;
        }

        if (participants.size() == 2) {
            extendedChatDTO.setChatType(PERSONAL_CHAT);
            return;
        }

        extendedChatDTO.setChatType(GROUP_CHAT);
    }

    @AfterMapping
    protected void setDisplayName(Chat chat, @MappingTarget ExtendedChatDTO extendedChatDTO) {
        extendedChatDTO.setDisplayName("Chat #%d".formatted(chat.getId()));
    }

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "lastMessage.id", target = "lastMessage.id")
    @Mapping(source = "lastMessage.content", target = "lastMessage.content")
    @Mapping(source = "lastMessage.sender", target = "lastMessage.sender")
    @Mapping(source = "lastMessage.createdAt", target = "lastMessage.createdAt")
    @Mapping(source = "lastMessage.modifiedAt", target = "lastMessage.modifiedAt")
    @Mapping(source = "chat.participants", target = "participants")
    public abstract ExtendedChatDTO chatToExtendedChatDTO(Chat chat, Message lastMessage);
}
