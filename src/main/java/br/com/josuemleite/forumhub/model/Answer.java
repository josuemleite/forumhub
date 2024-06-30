package br.com.josuemleite.forumhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private Boolean solution;

    public Answer(String message, Topic topic, User author) {
        this.message = message;
        this.topic = topic;
        this.creationDate = LocalDateTime.now();
        this.author = author;
        this.solution = false;
    }
}
