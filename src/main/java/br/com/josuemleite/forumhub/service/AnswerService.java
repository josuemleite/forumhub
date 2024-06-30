package br.com.josuemleite.forumhub.service;

import br.com.josuemleite.forumhub.dto.answer.AnswerCreationDTO;
import br.com.josuemleite.forumhub.dto.answer.AnswerDetailingDTO;
import br.com.josuemleite.forumhub.dto.answer.AnswerUpdatingDTO;
import br.com.josuemleite.forumhub.model.Answer;
import br.com.josuemleite.forumhub.model.Topic;
import br.com.josuemleite.forumhub.model.User;
import br.com.josuemleite.forumhub.repository.AnswerRepository;
import br.com.josuemleite.forumhub.repository.TopicRepository;
import br.com.josuemleite.forumhub.repository.UserRepository;
import br.com.josuemleite.forumhub.service.exceptions.DatabaseException;
import br.com.josuemleite.forumhub.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }

    public Set<Answer> findAnswersById(Set<Long> answersIds) {
        if (answersIds == null) {
            return new HashSet<>();
        }
        Set<Answer> answers = new HashSet<>();
        for (Long userId : answersIds) {
            answerRepository.findById(userId).ifPresent(answers::add);
        }
        return answers;
    }

    public Optional<Answer> findByMessage(String message) {
        return answerRepository.findByMessage(message);
    }

    public Page<Answer> findAllOrderedBySolutionAndCreationDate(Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    @Transactional
    public AnswerDetailingDTO insert(AnswerCreationDTO data) {
        Topic topic = topicRepository.getReferenceById(data.topicId());
        User author = userRepository.getReferenceById(data.authorId());
        Answer answer = new Answer(data.message(), topic, author);
        answerRepository.save(answer);
        return new AnswerDetailingDTO(answer);
    }

    @Transactional
    public void delete(Long id) {
        try {
            answerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public AnswerDetailingDTO update(Long id, AnswerUpdatingDTO data) {
        try {
            Answer answer = answerRepository.getReferenceById(id);
            updateData(answer, data);
            answerRepository.save(answer);
            return new AnswerDetailingDTO(answer);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Answer answer, AnswerUpdatingDTO data) {
        if (data.message() != null && !data.message().isBlank()) {
            answer.setMessage(data.message());
        }
        if (data.solution() != null) {
            answer.setSolution(data.solution());
        }
    }
}
