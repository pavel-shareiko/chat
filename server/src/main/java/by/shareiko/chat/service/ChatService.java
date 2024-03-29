package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.dto.ExtendedChatDTO;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    List<Chat> getCurrentUserChats();

    List<ExtendedChatDTO> getCurrentUserChatsWithLastMessage();

    ExtendedChatDTO getChat(Long chatId);

    Chat getChatWithParticipants(Long chatId);

    Chat startChat(String username);

    Optional<Chat> getChatWithUser(String username);

    boolean hasChatWithUser(String username);

    boolean doesCurrentUserParticipateInChat(Long id);

    boolean exists(Long chatId);
}
