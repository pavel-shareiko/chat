package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.security.SecurityUtils;
import by.shareiko.chat.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> getCurrentUserChats() {
        Optional<UserPrincipal> currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.isEmpty()) {
            return List.of();
        }
        return chatRepository.findCurrentUserChats();
    }
}
