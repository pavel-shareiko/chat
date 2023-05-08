package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;

import java.util.List;

public interface MessageService {
    List<Message> getChatMessages(Long chatId);

    Message saveMessageAndNotifyListeners(NewMessageDTO newMessage);

    void deleteMessage(Long id);

    Message updateMessage(Long messageId, String newContent);
}
