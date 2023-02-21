package by.shareiko.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_generator")
    @SequenceGenerator(name = "chat_id_generator", sequenceName = "chat_id_seq", allocationSize = 1)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    private Set<User> participants;
}
