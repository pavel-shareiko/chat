package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ChatDTO;
import by.shareiko.chat.dto.ChatType;
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
    protected void determineChatType(Chat chat, @MappingTarget ChatDTO chatDTO) {
        Set<User> participants = chat.getParticipants();
        if (participants.size() == 1) {
            chatDTO.setChatType(ChatType.SELF_CHAT);
            return;
        }

        if (participants.size() == 2) {
            chatDTO.setChatType(ChatType.PERSONAL_CHAT);
            return;
        }

        chatDTO.setChatType(ChatType.GROUP_CHAT);
    }

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "lastMessage.id", target = "lastMessage.id")
    @Mapping(source = "lastMessage.content", target = "lastMessage.content")
    @Mapping(source = "lastMessage.sender", target = "lastMessage.sender")
    @Mapping(source = "lastMessage.createdAt", target = "lastMessage.createdAt")
    @Mapping(source = "lastMessage.modifiedAt", target = "lastMessage.modifiedAt")
    @Mapping(target = "lastMessage.sender.roles", ignore = true)
    @Mapping(source = "chat.participants", target = "participants")
    public abstract ChatDTO chatToChatDTO(Chat chat, Message lastMessage);
}
