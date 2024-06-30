package br.com.josuemleite.forumhub.model;

import br.com.josuemleite.forumhub.model.enums.TopicStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private TopicStatus status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "topic")
    private Set<Answer> answers = new HashSet<>();

    public Topic(String title, String message, User author, Course course, Set<Answer> answers) {
        this.title = title;
        this.message = message;
        this.creationDate = LocalDateTime.now();
        this.status = TopicStatus.OPEN;
        this.author = author;
        this.course = course;
        if (answers != null) {
            this.answers.addAll(answers);
        }
    }
}
