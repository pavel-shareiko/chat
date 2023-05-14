package by.shareiko.chat.controller;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.service.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(@Qualifier("chatStompServiceImpl") ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ExtendedChatDTO>> getCurrentUserChats() {
        log.debug("REST request to get current user chats");
        return ResponseEntity.ok(chatService.getCurrentUserChatsWithLastMessage());
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ExtendedChatDTO> getChat(@PathVariable Long chatId) {
        log.debug("REST request to get chat with id {}", chatId);
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @GetMapping("/{chatId}/exists")
    public ResponseEntity<Boolean> doesChatExist(@PathVariable Long chatId) {
        log.debug("REST request to check if chat with id {} exists", chatId);
        return ResponseEntity.ok(chatService.doesCurrentUserParticipateInChat(chatId));
    }

    @PostMapping("/{username}")
    public ResponseEntity<Long> createChat(@PathVariable String username) {
        log.debug("REST request to create chat with user {}", username);

        Optional<Chat> optionalChat = chatService.getChatWithUser(username);
        if (optionalChat.isPresent()) {
            return ResponseEntity.ok(optionalChat.get().getId());
        }

        return ResponseEntity.ok(chatService.startChat(username).getId());
    }
}
