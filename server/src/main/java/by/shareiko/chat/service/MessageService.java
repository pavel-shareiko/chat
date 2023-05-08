package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;

import java.util.List;

public interface MessageService {
    List<Message> getChatMessages(Long chatId);

    Message saveMessage(NewMessageDTO newMessage);

    Message updateMessage(Long messageId, String newContent);

    Message deleteMessage(Long id);

    List<Message> deleteAllMessages(List<Long> messageIds);
}
