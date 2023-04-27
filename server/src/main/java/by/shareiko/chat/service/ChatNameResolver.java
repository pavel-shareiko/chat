package by.shareiko.chat.service;

import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.exception.ChatNameResolutionException;

public interface ChatNameResolver {
    String resolveName(ExtendedChatDTO chat) throws ChatNameResolutionException;
}
