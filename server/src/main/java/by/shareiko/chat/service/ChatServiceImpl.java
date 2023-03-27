package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.mapper.ChatMapper;
import by.shareiko.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatMessageService chatMessageService;

    @Override
    public List<Chat> getCurrentUserChats() {
        return chatRepository.findCurrentUserChats();
    }

    @Override
    public List<ExtendedChatDTO> getCurrentUserChatsWithLastMessage() {
        List<Chat> currentUserChats = getCurrentUserChats();
        return currentUserChats.stream()
                .map(chat -> {
                    Optional<Message> lastMessageOptional = chatMessageService.getLastMessageInChat(chat.getId());
                    return chatMapper.chatToChatDTO(chat, lastMessageOptional.orElse(null));
                })
                .toList();
    }

    @Override
    public boolean doesCurrentUserParticipateInChat(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new BadRequestException("Chat with id " + chatId + " doesn't exist");
        }

        return chatRepository.findCurrentUserChats().stream()
                .flatMap(c -> c.getParticipants().stream())
                .anyMatch(participant -> participant.getId().equals(chatId));
    }
}
