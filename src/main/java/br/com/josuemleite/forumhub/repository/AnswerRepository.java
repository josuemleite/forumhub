package br.com.josuemleite.forumhub.repository;

import br.com.josuemleite.forumhub.domain.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByMessage(String message);
}
