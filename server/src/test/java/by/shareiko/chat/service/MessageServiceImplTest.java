package by.shareiko.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import by.shareiko.chat.domain.Chat;
import by.shareiko.chat.domain.Message;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.exception.BadRequestException;
import by.shareiko.chat.exception.NotFoundException;
import by.shareiko.chat.exception.UserUnauthorizedException;
import by.shareiko.chat.mapper.MessageMapper;
import by.shareiko.chat.repository.ChatRepository;
import by.shareiko.chat.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WithMockUser(username = "sender", authorities = {"ROLE_USER"})
public class MessageServiceImplTest {

    private static final User SENDER = getUser("sender");
    private static final String INITIAL_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatService chatService;

    private static User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

    @Test
    public void testGetChatMessages_withExistingChat_shouldReturnMessages() {
        // Arrange
        Long chatId = 1L;
        Chat chat = new Chat();
        chat.setId(chatId);

        User user1 = getUser("user1");
        Message message1 = new Message();
        String content1 = "Hello";
        message1.setSender(user1);
        message1.setChat(chat);
        message1.setContent(content1);

        User user2 = getUser("user2");
        Message message2 = new Message();
        String content2 = "How are you?";
        message2.setSender(user2);
        message2.setChat(chat);
        message2.setContent(content2);

        when(messageRepository.getAllChatMessages(chatId)).thenReturn(Arrays.asList(
                message1,
                message2
        ));
        when(chatRepository.existsById(chatId)).thenReturn(true);
        when(chatService.doesCurrentUserParticipateInChat(chatId)).thenReturn(true);

        // Act
        List<Message> messages = messageService.getChatMessages(chatId);

        // Assert
        assertEquals(2, messages.size());
        assertEquals(content1, messages.get(0).getContent());
        assertEquals("user1", messages.get(0).getSender().getUsername());
        assertEquals(content2, messages.get(1).getContent());
        assertEquals("user2", messages.get(1).getSender().getUsername());
    }

    @Test
    public void testGetChatMessages_withUnauthorizedUser_shouldThrowUserUnauthorizedException() {
        // Arrange
        Long chatId = 1L;

        when(chatService.doesCurrentUserParticipateInChat(chatId)).thenReturn(false);
        when(chatRepository.existsById(chatId)).thenReturn(true);

        // Act & Assert
        UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class,
                () -> messageService.getChatMessages(chatId)
        );
        assertEquals("Current user doesn't have enough permissions to view a chat with id " + chatId, exception.getMessage());
    }

    @Test
    public void testGetChatMessages_withNonexistentChat_shouldThrowNotFoundException() {
        // Arrange
        Long chatId = 1L;

        when(chatRepository.existsById(chatId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> messageService.getChatMessages(chatId)
        );
        assertEquals("Chat with id " + chatId + " not found", exception.getMessage());
    }

    @Test
    void testSaveMessage_withBlankContent_shouldThrowBadRequestException() {
        // Arrange
        String content = "";
        long chatId = 1L;
        NewMessageDTO newMessage = new NewMessageDTO(content, chatId);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> messageService.saveMessage(newMessage));

        assertEquals("Message content cannot be blank", exception.getMessage());
        verifyNoInteractions(messageRepository, chatService);
    }

    @Test
    void testSaveMessage_withNullChatId_shouldThrowBadRequestException() {
        // Arrange
        String content = "Hello, world!";
        Long chatId = null;
        NewMessageDTO newMessage = new NewMessageDTO(content, chatId);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> messageService.saveMessage(newMessage));

        assertEquals("Chat cannot be null", exception.getMessage());
        verifyNoInteractions(messageRepository, chatService);
    }

    @Test
    void testSaveMessage_withNonexistentChat_shouldThrowNotFoundException() {
        // Arrange
        String content = "Hello, world!";
        long chatId = 1L;
        NewMessageDTO newMessage = new NewMessageDTO(content, chatId);

        when(chatService.doesCurrentUserParticipateInChat(newMessage.getChatId())).thenReturn(false);

        // Act & Assert
        UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class,
                () -> messageService.saveMessage(newMessage));

        assertEquals("Current user doesn't have enough permissions to send a message to a chat with id " + newMessage.getChatId(), exception.getMessage());
        verify(chatService).doesCurrentUserParticipateInChat(newMessage.getChatId());
        verifyNoMoreInteractions(chatService, messageRepository);
    }

    @Test
    void testSaveMessage_withValidInputs_shouldReturnSavedMessage() {
        // Arrange
        String content = "Hello, world!";
        long chatId = 1L;

        NewMessageDTO newMessage = new NewMessageDTO(content, chatId);

        Message message = new Message();
        message.setContent(newMessage.getContent());

        when(chatService.doesCurrentUserParticipateInChat(newMessage.getChatId())).thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.newMessageDTOToMessage(newMessage)).thenReturn(message);

        // Act
        Message savedMessage = messageService.saveMessage(newMessage);

        // Assert
        assertEquals(message, savedMessage);
        verify(chatService).doesCurrentUserParticipateInChat(newMessage.getChatId());
        verify(messageRepository).save(any(Message.class));
        verifyNoMoreInteractions(chatService, messageRepository);
    }

    @Test
    public void testUpdateMessage_withValidParameters_shouldReturnUpdatedMessage() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        message.setContent(INITIAL_CONTENT);
        message.setSender(SENDER);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        String newContent = "new content";

        // Act
        Message updatedMessage = messageService.updateMessage(messageId, newContent);

        // Assert
        assertEquals(newContent, updatedMessage.getContent());
        verify(messageRepository).save(message);
    }

    @Test
    public void testUpdateMessage_withInvalidMessageId_shouldThrowBadRequestException() {
        // Arrange
        Long messageId = null;

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> messageService.updateMessage(messageId, "new content"));

        assertEquals("Message id cannot be null", exception.getMessage());
        verifyNoInteractions(messageRepository);
    }

    @Test
    public void testUpdateMessage_withInvalidContent_shouldThrowBadRequestException() {
        // Arrange
        Long messageId = 1L;
        String newContent = "   ";

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> messageService.updateMessage(messageId, newContent));

        assertEquals("Message content cannot be blank", exception.getMessage());
        verifyNoInteractions(messageRepository);
    }

    @Test
    public void testUpdateMessage_withNonexistentMessage_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 1L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> messageService.updateMessage(messageId, UPDATED_CONTENT)
        );

        assertEquals("Message with id " + messageId + " not found", exception.getMessage());
        verify(messageRepository).findById(messageId);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @WithMockUser(username = "another user", authorities = {"ROLE_USER"})
    public void testUpdateMessage_withUnauthorizedUser_shouldThrowUserUnauthorizedException() {
        // Arrange
        Long messageId = 1L;

        Message message = new Message();
        message.setId(messageId);
        message.setContent(INITIAL_CONTENT);
        message.setSender(SENDER);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act & Assert
        UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> messageService.updateMessage(messageId, UPDATED_CONTENT));

        assertEquals("Current user doesn't have enough permissions to update a message with id " + messageId, exception.getMessage());
    }

    @Test
    public void testDeleteMessage_withValidMessageId_shouldInvokeDeleteMethod() {
        // Arrange
        Long messageId = 1L;

        Message message = new Message();
        message.setSender(SENDER);
        message.setContent(INITIAL_CONTENT);
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        messageService.deleteMessage(1L);

        // Assert
        verify(messageRepository).deleteById(messageId);
    }

    @Test
    public void testDeleteMessage_withNonexistentMessage_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 5L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> messageService.deleteMessage(messageId));

        // Assert
        verify(messageRepository).findById(messageId);
        verifyNoMoreInteractions(messageRepository);

        assertEquals("Message with id " + messageId + " not found", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "another user")
    public void testDeleteMessage_withUnauthorizedUser_shouldThrowUserUnauthorizedException() {
        // Arrange
        Long messageId = 1L;

        Message message = new Message();
        message.setSender(SENDER);
        message.setContent(INITIAL_CONTENT);
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act & Assert
        UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> messageService.deleteMessage(messageId));

        verify(messageRepository).findById(messageId);
        verifyNoMoreInteractions(messageRepository);
        assertEquals("Current user doesn't have enough permissions to delete a message with id " + messageId, exception.getMessage());
    }
}