package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.dto.course.CourseCreationDTO;
import br.com.josuemleite.forumhub.dto.course.CourseDetailingDTO;
import br.com.josuemleite.forumhub.dto.course.CourseListingDTO;
import br.com.josuemleite.forumhub.dto.course.CourseUpdatingDTO;
import br.com.josuemleite.forumhub.model.Course;
import br.com.josuemleite.forumhub.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping
    public ResponseEntity<CourseDetailingDTO> create(@RequestBody @Valid CourseCreationDTO data, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Course> existingCourse = service.findByNameAndCategory(data.name(), data.category());
        if (existingCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CourseDetailingDTO(existingCourse.get()));
        }
        CourseDetailingDTO courseDetailingDTO = service.insert(data);
        URI uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(courseDetailingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(courseDetailingDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CourseListingDTO>> list(@PageableDefault(sort = {"name"}) Pageable pageable) {
        Page<CourseListingDTO> page = service.findAll(pageable).map(CourseListingDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailingDTO> update(@PathVariable Long id, @RequestBody @Valid CourseUpdatingDTO data) {
        Optional<Course> existingCourse = service.findByNameAndCategory(data.name(), data.category());
        if (existingCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CourseDetailingDTO(existingCourse.get()));
        }
        Optional<Course> optCourse = service.findById(id);
        if (optCourse.isPresent()) {
            CourseDetailingDTO courseDetailingDTO = service.update(id, data);
            return ResponseEntity.ok(courseDetailingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Course> optCourse = service.findById(id);
        if (optCourse.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailingDTO> detail(@PathVariable Long id) {
        Optional<Course> optCourse = service.findById(id);
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            return ResponseEntity.ok(new CourseDetailingDTO(course));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
