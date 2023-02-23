package by.shareiko.chat.service;

import by.shareiko.chat.domain.Chat;

import java.util.List;

public interface ChatService {
    List<Chat> getCurrentUserChats();
}
