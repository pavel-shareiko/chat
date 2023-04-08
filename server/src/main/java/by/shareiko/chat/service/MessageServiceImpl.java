package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.exception.UserUnauthorizedException;
import by.shareiko.chat.mapper.MessageMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.repository.MessageRepository;
import by.shareiko.chat.security.SecurityUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final ChatService chatService;

    @Override
    public List<Message> getChatMessages(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }
        if (!chatService.doesCurrentUserParticipateInChat(chatId)) {
            throw new UserUnauthorizedException("Current user doesn't have enough permissions to view a chat with id " + chatId);
        }

        return messageRepository.getAllChatMessages(chatId);
    }

    @Override
    public Message saveMessage(NewMessageDTO newMessage) {
        if (StringUtils.isBlank(newMessage.getContent())) {
            throw new BadRequestException("Message content cannot be blank");
        }
        if (newMessage.getChatId() == null) {
            throw new BadRequestException("Chat cannot be null");
        }

        Message message = messageMapper.newMessageDTOToMessage(newMessage);
        if (!chatService.doesCurrentUserParticipateInChat(newMessage.getChatId())) {
            throw new UserUnauthorizedException("Current user doesn't have enough permissions to send a message to a chat with id " + newMessage.getChatId());
        }

        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Message with id " + id + " not found"));
        String currentUsername = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UserUnauthorizedException("Current user is not logged in"));

        if (!message.getSender().getUsername().equals(currentUsername)) {
            throw new UserUnauthorizedException("Current user doesn't have enough permissions to delete a message with id " + id);
        }

        messageRepository.deleteById(id);
    }

    @Override
    public Message updateMessage(Long messageId, String newContent) {
        if (messageId == null) {
            throw new BadRequestException("Message id cannot be null");
        }
        if (StringUtils.isBlank(newContent)) {
            throw new BadRequestException("Message content cannot be blank");
        }

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message with id " + messageId + " not found"));
        String currentUsername = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UserUnauthorizedException("Current user is not logged in"));

        if (!message.getSender().getUsername().equals(currentUsername)) {
            throw new UserUnauthorizedException("Current user doesn't have enough permissions to update a message with id " + messageId);
        }

        message.setContent(newContent);
        messageRepository.save(message);
        return message;
    }
}
