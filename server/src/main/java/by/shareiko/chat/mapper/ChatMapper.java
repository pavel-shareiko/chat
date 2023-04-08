package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ExtendedChatDTO;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Log4j2
@Mapper(componentModel = "spring")
public abstract class ChatMapper {
    @BeforeMapping
    protected void determineChatType(Chat chat, @MappingTarget ExtendedChatDTO extendedChatDTO) {
        Set<User> participants = chat.getParticipants();
        if (participants.size() == 1) {
            extendedChatDTO.setChatType(ExtendedChatDTO.ChatType.SELF_CHAT);
            return;
        }

        if (participants.size() == 2) {
            extendedChatDTO.setChatType(ExtendedChatDTO.ChatType.PERSONAL_CHAT);
            return;
        }

        extendedChatDTO.setChatType(ExtendedChatDTO.ChatType.GROUP_CHAT);
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
