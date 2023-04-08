package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.dto.SimpleUserDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.mapper.ChatMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .map(this::getExtendedChat)
                .sorted((o1, o2) -> Comparators.nullsLow().compare(o2.getLastMessage(), o1.getLastMessage()))
                .toList();
    }

    private ExtendedChatDTO getExtendedChat(Chat chat) {
        Message lastMessage = chatMessageService.getLastMessageInChat(chat.getId()).orElse(null);

        ExtendedChatDTO extendedChatDTO = chatMapper.chatToExtendedChatDTO(chat, lastMessage);

        String currentUsername = SecurityUtils.getCurrentUserLogin().orElse(null);
        Set<SimpleUserDTO> participants = extendedChatDTO.getParticipants().stream()
                .filter(participant -> !participant.getUsername().equals(currentUsername))
                .collect(Collectors.toSet());
        extendedChatDTO.setParticipants(participants);

        return extendedChatDTO;
    }

    @Override
    public boolean doesCurrentUserParticipateInChat(Long chatId) {
        return chatRepository.doesCurrentUserParticipateInChat(chatId);
    }
}
