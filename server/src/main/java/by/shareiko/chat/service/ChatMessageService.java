package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;

import java.util.Optional;

public interface ChatMessageService {

    Optional<Message> getLastMessageInChat(Long chatId);
}
