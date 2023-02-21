package by.shareiko.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority {
    @Id
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
