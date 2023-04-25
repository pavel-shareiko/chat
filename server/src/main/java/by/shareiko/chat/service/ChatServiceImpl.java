package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.dto.SimpleMessageDTO;
import by.shareiko.chat.dto.SimpleUserDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.exception.ChatAlreadyExists;
import by.shareiko.chat.exception.ChatNotAllowedException;
import by.shareiko.chat.exception.UserUnauthorizedException;
import by.shareiko.chat.mapper.ChatMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.security.SecurityUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
                .sorted((o1, o2) -> Comparators.nullsLow().compare(
                        Optional.ofNullable(o2.getLastMessage())
                                .map(SimpleMessageDTO::getCreatedAt)
                                .orElse(null),
                        Optional.ofNullable(o1.getLastMessage())
                                .map(SimpleMessageDTO::getCreatedAt)
                                .orElse(null))
                )
                .toList();
    }

    @Override
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
    public boolean hasChatWithUser(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BadRequestException("Username cannot be blank");
        }

        return chatRepository.existsByOtherUsernameAndCurrentUser(username);
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
