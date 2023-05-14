package by.shareiko.chat.dto;

import lombok.Data;

@Data
public class MessageUpdateDTO {
    private Long messageId;
    private String newContent;
}
