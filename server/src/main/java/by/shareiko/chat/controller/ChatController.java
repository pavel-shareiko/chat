package by.shareiko.chat.controller;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.service.ChatService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ExtendedChatDTO>> getCurrentUserChats() {
        log.debug("REST request to get current user chats");
        return ResponseEntity.ok(chatService.getCurrentUserChatsWithLastMessage());
    }

    @PostMapping("/{username}")
    public ResponseEntity<Chat> createChat(@PathVariable String username) {
        log.debug("REST request to create chat with user {}", username);
        return ResponseEntity.ok(chatService.startChat(username));
    }
}
