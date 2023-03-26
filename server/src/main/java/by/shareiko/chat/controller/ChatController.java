package by.shareiko.chat.controller;

import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.service.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ExtendedChatDTO> getCurrentUserChats() {
        log.debug("REST request to get current user chats");
        return chatService.getCurrentUserChatsWithLastMessage();
    }
}
