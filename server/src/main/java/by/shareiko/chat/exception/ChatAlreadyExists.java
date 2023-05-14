package by.shareiko.chat.exception;

public class ChatAlreadyExists extends ConflictException {
    public ChatAlreadyExists(String message) {
        super(message);
    }
}
