package by.shareiko.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_generator")
    @SequenceGenerator(name = "chat_id_generator", sequenceName = "chat_id_seq", allocationSize = 1)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = {@JoinColumn(name = "chat_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @ToString.Exclude
    private Set<User> participants;
}


