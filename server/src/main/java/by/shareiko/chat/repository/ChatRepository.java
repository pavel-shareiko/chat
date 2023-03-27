package by.shareiko.chat.repository;

import by.shareiko.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
