package br.com.josuemleite.forumhub.service;

import br.com.josuemleite.forumhub.dto.topic.TopicCreationDTO;
import br.com.josuemleite.forumhub.dto.topic.TopicDetailingDTO;
import br.com.josuemleite.forumhub.dto.topic.TopicUpdatingDTO;
import br.com.josuemleite.forumhub.model.Answer;
import br.com.josuemleite.forumhub.model.Course;
import br.com.josuemleite.forumhub.model.Topic;
import br.com.josuemleite.forumhub.model.User;
import br.com.josuemleite.forumhub.repository.TopicRepository;
import br.com.josuemleite.forumhub.service.exceptions.DatabaseException;
import br.com.josuemleite.forumhub.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    private final UserService userService;

    private final CourseService courseService;

    private final AnswerService answerService;

    public TopicService(TopicRepository topicRepository, @Lazy UserService userService, @Lazy CourseService courseService, AnswerService answerService) {
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.answerService = answerService;
    }

    public Page<Topic> findAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    public Set<Topic> findTopicsById(Set<Long> topicsIds) {
        if (topicsIds == null) {
            return new HashSet<>();
        }
        Set<Topic> topics = new HashSet<>();
        for (Long topicId : topicsIds) {
            topicRepository.findById(topicId).ifPresent(topics::add);
        }
        return topics;
    }

    public Optional<Topic> findByTitleAndMessage(String title, String message) {
        return topicRepository.findByTitleAndMessage(title, message);
    }

    @Transactional
    public TopicDetailingDTO createTopic(TopicCreationDTO data) {
        Optional<User> author = userService.findById(data.authorId());
        Optional<Course> course = courseService.findById(data.courseId());
        Set<Answer> answers = answerService.findAnswersById(data.answers());
        Topic topic = new Topic(data.title(), data.message(), author.orElseThrow(), course.orElseThrow(), answers);
        topicRepository.save(topic);
        return new TopicDetailingDTO(topic);
    }

    @Transactional
    public void delete(Long id) {
        try {
            topicRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public TopicDetailingDTO update(Long id, TopicUpdatingDTO obj) {
        try {
            Topic topic = topicRepository.getReferenceById(id);
            updateData(topic, obj);
            topicRepository.save(topic);
            return new TopicDetailingDTO(topic);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void updateData(Topic topic, TopicUpdatingDTO data) {
        if (data.title() != null && !data.title().isBlank()) {
            topic.setTitle(data.title());
        }
        if (data.message() != null && !data.message().isBlank()) {
            topic.setMessage(data.message());
        }
        if (data.status() != null) {
            topic.setStatus(data.status());
        }
        if (data.authorId() != null) {
            Optional<User> author = userService.findById(data.authorId());
            author.ifPresent(topic::setAuthor);
        }
        if (data.courseId() != null) {
            Optional<Course> course = courseService.findById(data.courseId());
            course.ifPresent(topic::setCourse);
        }
        if (data.answers() != null) {
            Set<Answer> answers = answerService.findAnswersById(data.answers());
            Set<Answer> existingAnswers = topic.getAnswers();
            if (existingAnswers != null) {
                topic.setAnswers(existingAnswers);
                topic.getAnswers().addAll(answers);
            } else {
                topic.setAnswers(answers);
            }
        }
    }
}
