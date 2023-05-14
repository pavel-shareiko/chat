package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ExtendedChatDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChatStompServiceImpl implements ChatService {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatStompServiceImpl(@Qualifier("chatServiceImpl") ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<Chat> getCurrentUserChats() {
        return chatService.getCurrentUserChats();
    }

    @Override
    public List<ExtendedChatDTO> getCurrentUserChatsWithLastMessage() {
        return chatService.getCurrentUserChatsWithLastMessage();
    }

    @Override
    public ExtendedChatDTO getChat(Long chatId) {
        return chatService.getChat(chatId);
    }

    @Override
    public Chat getChatWithParticipants(Long chatId) {
        return chatService.getChatWithParticipants(chatId);
    }

    @Override
    @Transactional
    public Chat startChat(String username) {
        Chat newChat = chatService.startChat(username);

        for (User participants : newChat.getParticipants()) {
            messagingTemplate.convertAndSendToUser(participants.getUsername(), "/chats/new", newChat.getId());
        }
        return newChat;
    }

    @Override
    public Optional<Chat> getChatWithUser(String username) {
        return chatService.getChatWithUser(username);
    }

    @Override
    public boolean hasChatWithUser(String username) {
        return chatService.hasChatWithUser(username);
    }

    @Override
    public boolean doesCurrentUserParticipateInChat(Long id) {
        return chatService.doesCurrentUserParticipateInChat(id);
    }
}
