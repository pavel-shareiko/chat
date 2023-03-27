package by.shareiko.chat.repository;

import by.shareiko.chat.domain.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m " +
           "from Message m " +
           "where m.chat.id = :chatId " +
           "order by m.createdAt " +
           "desc")
    List<Message> getAllChatMessages(@Param("chatId") Long chatId);

    @Query("select m " +
           "from Message m " +
           "where m.chat.id = :chatId " +
           "order by m.createdAt " +
           "desc")
    List<Message> getChatMessages(@Param("chatId") Long chatId, Pageable pageable);

    default Optional<Message> getLastMessageInChat(@Param("chatId") Long chatId) {
        return getChatMessages(chatId, PageRequest.of(0, 1)).stream().findFirst();
    }

}
