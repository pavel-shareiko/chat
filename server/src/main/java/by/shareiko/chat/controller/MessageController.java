package by.shareiko.chat.controller;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
