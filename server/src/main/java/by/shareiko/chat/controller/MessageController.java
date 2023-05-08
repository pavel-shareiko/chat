package by.shareiko.chat.controller;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.service.MessageService;
import lombok.extern.log4j.Log4j2;
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

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long id) {
        List<Message> chatMessages = messageService.getChatMessages(id);
        return ResponseEntity.ok(chatMessages);
    }

    @Transactional
    @MessageMapping("/chats/messages")
    public void sendMessage(@Payload NewMessageDTO newMessage) {
        log.info("Request to send message: {}", newMessage);
        messageService.saveMessageAndNotifyListeners(newMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody String newContent) {
        Message updatedMessage = messageService.updateMessage(id, newContent);
        return ResponseEntity.ok(updatedMessage);
    }
}
