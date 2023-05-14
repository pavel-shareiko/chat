package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public Optional<Message> getLastMessageInChat(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        return messageRepository.getLastMessageInChat(chatId);
    }
}
