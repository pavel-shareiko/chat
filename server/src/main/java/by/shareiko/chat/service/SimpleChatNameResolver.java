package by.shareiko.chat.service;

import by.shareiko.chat.dto.ExtendedChatDTO;
import by.shareiko.chat.dto.SimpleUserDTO;
import by.shareiko.chat.exception.ChatNameResolutionException;
import by.shareiko.chat.security.SecurityUtils;
import org.springframework.stereotype.Service;

import static by.shareiko.chat.dto.ExtendedChatDTO.ChatType.*;

@Service
public class SimpleChatNameResolver implements ChatNameResolver {
    @Override
    public String resolveName(ExtendedChatDTO chat) {
        if (chat.getChatType() == SELF_CHAT) {
            return "You";
        }
        if (chat.getChatType() == PERSONAL_CHAT) {
            String currentUserName = SecurityUtils.getCurrentUserLogin().orElse(null);
            SimpleUserDTO companion = chat.getParticipants().stream()
                    .filter(p -> !p.getUsername().equals(currentUserName))
                    .findFirst()
                    .orElseThrow(() -> new ChatNameResolutionException("Personal Chat should contain at least on companion"));
            return companion.getFirstName() + " " + companion.getLastName();
        }
        if (chat.getChatType() == GROUP_CHAT) {
            return "Group chat";
        }
        return chat.getDisplayName();
    }
}
