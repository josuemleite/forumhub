package br.com.josuemleite.forumhub.repository;

import br.com.josuemleite.forumhub.domain.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByMessage(String message);

    @Query("SELECT a FROM Answer a ORDER BY a.solution DESC, a.creationDate ASC")
    Page<Answer> findAllOrderedBySolutionAndCreationDate(Pageable pageable);
}
