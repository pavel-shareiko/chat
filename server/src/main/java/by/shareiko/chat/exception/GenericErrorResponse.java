package by.shareiko.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
    private String title;
    private String details;
    private long responseCode;
    private LocalDateTime timestamp;
}
