package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    List<Message> getChatMessages(Long chatId);

    Optional<Message> getLastMessageInChat(Long id);
}
