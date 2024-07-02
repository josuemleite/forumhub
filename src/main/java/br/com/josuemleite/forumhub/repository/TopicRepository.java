package br.com.josuemleite.forumhub.repository;

import br.com.josuemleite.forumhub.domain.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByTitleAndMessage(String title, String message);
}
