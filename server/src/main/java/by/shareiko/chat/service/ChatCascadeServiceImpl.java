package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.mapper.ChatMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatCascadeServiceImpl implements ChatCascadeService {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageService messageService;

    public ChatCascadeServiceImpl(ChatService chatService, ChatMapper chatMapper, MessageService messageService) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
        this.messageService = messageService;
    }

    @Override
    public List<ExtendedChatDTO> getCurrentUserChats() {
        List<Chat> currentUserChats = chatService.getCurrentUserChats();
        return currentUserChats.stream()
                .map(chat -> {
                    Optional<Message> lastMessageOptional = messageService.getLastMessageInChat(chat.getId());
                    return chatMapper.chatToChatDTO(chat, lastMessageOptional.orElse(null));
                })
                .toList();
    }

}
