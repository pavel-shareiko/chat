package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.dto.SimpleMessageDTO;
import by.shareiko.chat.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageStompServiceImpl implements MessageService {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MessageMapper messageMapper;

    public MessageStompServiceImpl(@Qualifier("messageServiceImpl") MessageService messageService, SimpMessagingTemplate messagingTemplate, ChatService chatService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getChatMessages(Long chatId) {
        return messageService.getChatMessages(chatId);
    }

    @Override
    @Transactional
    public Message saveMessage(NewMessageDTO newMessage) {
        Message savedMessage = messageService.saveMessage(newMessage);

        Chat chat = chatService.getChatWithParticipants(newMessage.getChatId());
        SimpleMessageDTO simpleMessage = messageMapper.messageToSimpleMessageDTO(savedMessage);
        for (User receiver : chat.getParticipants()) {
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages/new", simpleMessage);
        }

        return savedMessage;
    }

    @Override
    @Transactional
    public Message updateMessage(Long messageId, String newContent) {
        Message updatedMessage = messageService.updateMessage(messageId, newContent);

        for (User participant : updatedMessage.getChat().getParticipants()) {
            messagingTemplate.convertAndSendToUser(participant.getUsername(), "/queue/messages/edit", updatedMessage);
        }

        return updatedMessage;
    }

    @Override
    @Transactional
    public Message deleteMessage(Long id) {
        Message deletedMessage = messageService.deleteMessage(id);

        Chat chat = chatService.getChatWithParticipants(deletedMessage.getChat().getId());
        for (User participant : chat.getParticipants()) {
            messagingTemplate.convertAndSendToUser(participant.getUsername(), "/queue/messages/delete", List.of(id));
        }

        return deletedMessage;
    }

    @Override
    @Transactional
    public List<Message> deleteAllMessages(List<Long> messageIds) {
        List<Message> deletedMessages = messageService.deleteAllMessages(messageIds);

        deletedMessages.stream()
                .flatMap(message -> message.getChat().getParticipants().stream())
                .forEach(participant -> messagingTemplate.convertAndSendToUser(participant.getUsername(), "/queue/messages/delete", messageIds));
        return deletedMessages;
    }
}
