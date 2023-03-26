package by.shareiko.chat.service;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.exception.UserUnauthorizedException;
import by.shareiko.chat.mapper.MessageMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.repository.MessageRepository;
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

        return messageRepository.getAllChatMessages(chatId);
    }

    @Override
    public Message saveMessage(NewMessageDTO newMessage) {
        if (StringUtils.isBlank(newMessage.getContent())) {
            throw new BadRequestException("Message content cannot be blank");
        }
        if (newMessage.getChat() == null) {
            throw new BadRequestException("Chat cannot be null");
        }

        Message message = messageMapper.newMessageDTOToMessage(newMessage);
        if (chatService.doesCurrentUserParticipateInChat(newMessage.getChat().getId())) {
            throw new UserUnauthorizedException("Current user doesn't have enough permissions to send a message to a chat with id " + newMessage.getChat().getId());
        }

        return messageRepository.save(message);
    }
}
