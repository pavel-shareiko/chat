package by.shareiko.chat.repository;

import by.shareiko.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "select distinct chat " +
                   "from Chat chat " +
                   "left join fetch chat.participants " +
                   "where chat.id in " +
                   "(select distinct c.id " +
                   "from Chat c " +
                   "inner join c.participants p " +
                   "where p.id = ?#{principal.id})")
    List<Chat> findCurrentUserChats();

    @Query(value = "select count(p) > 0 " +
                   "from Chat c " +
                   "left join c.participants p " +
                   "where c.id = :chatId " +
                   "and p.id = ?#{principal.id}")
    boolean doesCurrentUserParticipateInChat(@Param("chatId") Long chatId);

    @Query("select c " +
           "from Chat c where " +
           "size(c.participants) = 2 " +
           "and :username in (select cp.username from c.participants cp)" +
           "and ?#{principal.id} in (select cp.id from c.participants cp)")
    Optional<Chat> findChatWithOtherUsernameAndCurrentUser(String username);
}
