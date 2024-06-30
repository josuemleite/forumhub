package br.com.josuemleite.forumhub.service;

import br.com.josuemleite.forumhub.dto.user.UserCreationDTO;
import br.com.josuemleite.forumhub.dto.user.UserDetailingDTO;
import br.com.josuemleite.forumhub.dto.user.UserUpdatingDTO;
import br.com.josuemleite.forumhub.model.Answer;
import br.com.josuemleite.forumhub.model.Profile;
import br.com.josuemleite.forumhub.model.Topic;
import br.com.josuemleite.forumhub.model.User;
import br.com.josuemleite.forumhub.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    private final TopicService topicService;

    private final AnswerService answerService;

    public UserService(UserRepository userRepository, @Lazy ProfileService profileService, TopicService topicService, AnswerService answerService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.topicService = topicService;
        this.answerService = answerService;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Set<User> findUsersById(Set<Long> usersIds) {
        Set<User> users = new HashSet<>();
        for (Long userId : usersIds) {
            userRepository.findById(userId).ifPresent(users::add);
        }
        return users;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDetailingDTO insert(UserCreationDTO data) {
        Set<Profile> profiles = profileService.findProfilesById(data.profiles());
        Set<Topic> topics = topicService.findTopicsById(data.topics());
        Set<Answer> answers = answerService.findAnswersById(data.answers());
        User user = new User(data.name(), data.email(), data.password(), profiles, topics, answers);
        userRepository.save(user);
        return new UserDetailingDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public UserDetailingDTO update(Long id, UserUpdatingDTO obj) {
        try {
            User user = userRepository.getReferenceById(id);
            updateData(user, obj);
            userRepository.save(user);
            return new UserDetailingDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(User user, UserUpdatingDTO data) {
        if (data.name() != null && !data.name().isBlank()) {
            user.setName(data.name());
        }
        if (data.email() != null && !data.email().isBlank()) {
            user.setEmail(data.email());
        }
        if (data.password() != null && !data.password().isBlank()) {
            user.setPassword(data.password());
        }
        if (data.profiles() != null) {
            Set<Profile> profiles = profileService.findProfilesById(data.profiles());
            Set<Profile> existingProfiles = user.getProfiles();
            if (existingProfiles != null) {
                user.setProfiles(existingProfiles);
                user.getProfiles().addAll(profiles);
            } else {
                user.setProfiles(profiles);
            }
        }
        if (data.topics() != null) {
            Set<Topic> topics = topicService.findTopicsById(data.topics());
            Set<Topic> existingTopics = user.getTopics();
            if (existingTopics != null) {
                user.setTopics(existingTopics);
                user.getTopics().addAll(topics);
            } else {
                user.setTopics(topics);
            }
        }
        if (data.answers() != null) {
            Set<Answer> answers = answerService.findAnswersById(data.answers());
            Set<Answer> existingAnswers = user.getAnswers();
            if (existingAnswers != null) {
                user.setAnswers(existingAnswers);
                user.getAnswers().addAll(answers);
            } else {
                user.setAnswers(answers);
            }
        }
    }
}
