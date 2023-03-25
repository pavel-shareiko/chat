package by.shareiko.chat.service;

import by.shareiko.chat.dto.ExtendedChatDTO;

import java.util.List;

public interface ChatCascadeService {
    List<ExtendedChatDTO> getCurrentUserChats();
}
