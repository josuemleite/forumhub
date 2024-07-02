package br.com.josuemleite.forumhub.service;

import br.com.josuemleite.forumhub.domain.dto.course.CourseCreationDTO;
import br.com.josuemleite.forumhub.domain.dto.course.CourseDetailingDTO;
import br.com.josuemleite.forumhub.domain.dto.course.CourseUpdatingDTO;
import br.com.josuemleite.forumhub.domain.model.Course;
import br.com.josuemleite.forumhub.domain.model.Topic;
import br.com.josuemleite.forumhub.repository.CourseRepository;
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

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final TopicService topicService;

    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> findByNameAndCategory(String name, String category) {
        return courseRepository.findByNameAndCategory(name, category);
    }

    @Transactional
    public CourseDetailingDTO insert(CourseCreationDTO data) {
        Set<Topic> topics = topicService.findTopicsById(data.topics());
        Course course = new Course(data.name(), data.category(), topics);
        courseRepository.save(course);
        return new CourseDetailingDTO(course);
    }

    @Transactional
    public void delete(Long id) {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public CourseDetailingDTO update(Long id, CourseUpdatingDTO obj) {
        try {
            Course course = courseRepository.getReferenceById(id);
            updateData(course, obj);
            courseRepository.save(course);
            return new CourseDetailingDTO(course);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Course course, CourseUpdatingDTO data) {
        if (data.name() != null && !data.name().isBlank()) {
            course.setName(data.name());
        }
        if (data.category() != null && !data.category().isBlank()) {
            course.setCategory(data.category());
        }
        if (data.topics() != null) {
            Set<Topic> topics = topicService.findTopicsById(data.topics());
            Set<Topic> existingTopics = course.getTopics();
            if (existingTopics != null) {
                course.setTopics(existingTopics);
                course.getTopics().addAll(topics);
            } else {
                course.setTopics(topics);
            }
        }
    }
}
