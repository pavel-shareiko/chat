package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public MessageServiceImpl(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Message> getChatMessages(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        return messageRepository.getChatMessages(chatId);
    }

    @Override
    public Optional<Message> getLastMessageInChat(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        return messageRepository.getLastMessageInChat(chatId);
    }
}
