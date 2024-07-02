package br.com.josuemleite.forumhub.repository;

import br.com.josuemleite.forumhub.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByNameAndCategory(String name, String category);
}
