package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.dto.user.SimpleUserDTO;
import by.shareiko.chat.exception.*;
import by.shareiko.chat.mapper.ChatMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.security.SecurityUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatMessageService chatMessageService;
    private final ChatNameResolver chatNameResolver;
    private final UserService userService;

    @Override
    public List<Chat> getCurrentUserChats() {
        return chatRepository.findCurrentUserChats();
    }

    @Override
    public List<ExtendedChatDTO> getCurrentUserChatsWithLastMessage() {
        List<Chat> currentUserChats = getCurrentUserChats();
        return currentUserChats.stream()
                .map(this::getExtendedChat)
                .filter(chat -> chat.getLastMessage() != null)
                .sorted((o1, o2) -> Comparators.nullsLow().compare(
                        o2.getLastMessage().getCreatedAt(),
                        o1.getLastMessage().getCreatedAt()
                ))
                .toList();
    }

    @Override
    public ExtendedChatDTO getChat(Long chatId) {
        if (!doesCurrentUserParticipateInChat(chatId)) {
            throw new UserUnauthorizedException("Current user is not allowed to access chat with id [" + chatId + "]");
        }
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new NotFoundException("Chat with id [" + chatId + "] not found"));
        return getExtendedChat(chat);
    }

    @Override
    public Chat getChatWithParticipants(Long chatId) {
        if (!doesCurrentUserParticipateInChat(chatId)) {
            throw new UserUnauthorizedException("Current user is not allowed to access chat with id [" + chatId + "]");
        }
        return chatRepository.findById(chatId).orElseThrow(() -> new NotFoundException("Chat with id [" + chatId + "] not found"));
    }

    @Override
    @Transactional
    public Chat startChat(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BadRequestException("Username cannot be blank");
        }

        User currentUser = SecurityUtils.getCurrentUser().orElseThrow(() -> new UserUnauthorizedException("Current user is not logged in")).getDomainUser();
        if (currentUser.getUsername().equals(username)) {
            throw new ChatNotAllowedException("Current user is not allowed to start chat with [" + username + "]");
        }
        User otherUser = userService.findByUsername(username).orElseThrow(() -> new BadRequestException("User with name [" + username + "] not found"));

        if (hasChatWithUser(username)) {
            throw new ChatAlreadyExists("Chat with user [" + username + "] already exists");
        }

        Chat newChat = new Chat();
        newChat.setParticipants(Set.of(currentUser, otherUser));
        return chatRepository.save(newChat);
    }

    @Override
    public Optional<Chat> getChatWithUser(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BadRequestException("Username cannot be blank");
        }

        return chatRepository
                .findChatWithOtherUsernameAndCurrentUser(username);
    }

    @Override
    public boolean hasChatWithUser(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BadRequestException("Username cannot be blank");
        }

        return chatRepository.findChatWithOtherUsernameAndCurrentUser(username).isPresent();
    }

    private ExtendedChatDTO getExtendedChat(Chat chat) {
        Message lastMessage = chatMessageService.getLastMessageInChat(chat.getId()).orElse(null);

        ExtendedChatDTO extendedChatDTO = chatMapper.chatToExtendedChatDTO(chat, lastMessage);

        String currentUsername = SecurityUtils.getCurrentUserLogin().orElse(null);
        List<SimpleUserDTO> participants = extendedChatDTO.getParticipants().stream()
                .filter(participant -> !participant.getUsername().equals(currentUsername))
                .collect(Collectors.toList());
        extendedChatDTO.setParticipants(participants);

        String chatName = chatNameResolver.resolveName(extendedChatDTO);
        extendedChatDTO.setDisplayName(chatName);

        return extendedChatDTO;
    }

    @Override
    public boolean doesCurrentUserParticipateInChat(Long chatId) {
        return chatRepository.doesCurrentUserParticipateInChat(chatId);
    }

    @Override
    public boolean exists(Long chatId) {
        return chatRepository.existsById(chatId);
    }
}
