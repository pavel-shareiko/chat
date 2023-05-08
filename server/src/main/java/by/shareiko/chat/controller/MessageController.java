package by.shareiko.chat.controller;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.MessageUpdateDTO;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Transactional
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(@Qualifier("messageStompServiceImpl") MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long id) {
        List<Message> chatMessages = messageService.getChatMessages(id);
        return ResponseEntity.ok(chatMessages);
    }

    @Transactional
    @MessageMapping("/messages/send")
    public void sendMessage(@Payload NewMessageDTO newMessage) {
        log.info("Request to send message: {}", newMessage);
        messageService.saveMessage(newMessage);
    }

    @Transactional
    @MessageMapping("/messages/delete")
    public void deleteMessage(@Payload Long messageId) {
        log.info("Request to delete message: {}", messageId);
        messageService.deleteMessage(messageId);
    }

    @Transactional
    @MessageMapping("/messages/delete-all")
    public void deleteMessages(@Payload List<Long> messageIds) {
        log.info("Request to delete messages: {}", messageIds);
        messageService.deleteAllMessages(messageIds);
    }

    @Transactional
    @MessageMapping("/messages/edit")
    public void updateMessage(@Payload MessageUpdateDTO newMessage) {
        log.info("Request to update message: {}", newMessage.getMessageId());
        messageService.updateMessage(newMessage.getMessageId(), newMessage.getNewContent());
    }
}
