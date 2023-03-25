package by.shareiko.chat.service;

import by.shareiko.chat.dto.ChatDTO;

import java.util.List;

public interface ChatCascadeService {
    List<ChatDTO> getCurrentUserChats();
}
