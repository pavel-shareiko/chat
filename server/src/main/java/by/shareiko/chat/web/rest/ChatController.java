package by.shareiko.chat.web.rest;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.service.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chats")
    public List<Chat> getCurrentUserChats() {
        log.debug("REST request to get all chats");
        return chatService.getCurrentUserChats();
    }
}
