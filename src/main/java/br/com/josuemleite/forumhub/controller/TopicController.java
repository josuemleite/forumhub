package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.domain.dto.topic.TopicCreationDTO;
import br.com.josuemleite.forumhub.domain.dto.topic.TopicDetailingDTO;
import br.com.josuemleite.forumhub.domain.dto.topic.TopicListingDTO;
import br.com.josuemleite.forumhub.domain.dto.topic.TopicUpdatingDTO;
import br.com.josuemleite.forumhub.domain.model.Topic;
import br.com.josuemleite.forumhub.service.TopicService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("topicos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class TopicController {

    private final TopicService service;

    @PostMapping
    public ResponseEntity<TopicDetailingDTO> create(@RequestBody @Valid TopicCreationDTO data, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Topic> existingTopic = service.findByTitleAndMessage(data.title(), data.message());
        if (existingTopic.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new TopicDetailingDTO(existingTopic.get()));
        }
        TopicDetailingDTO topicDetailingDTO = service.createTopic(data);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topicDetailingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(topicDetailingDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TopicListingDTO>> list(@PageableDefault(sort = {"creationDate"}) Pageable pageable) {
        Page<TopicListingDTO> page = service.findAll(pageable).map(TopicListingDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDetailingDTO> update(@PathVariable Long id, @RequestBody @Valid TopicUpdatingDTO data) {
        Optional<Topic> existingTopic = service.findByTitleAndMessage(data.title(), data.message());
        if (existingTopic.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new TopicDetailingDTO(existingTopic.get()));
        }
        Optional<Topic> optTopic = service.findById(id);
        if (optTopic.isPresent()) {
            TopicDetailingDTO topicDetailingDTO = service.update(id, data);
            return ResponseEntity.ok(topicDetailingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Topic> optTopic = service.findById(id);
        if (optTopic.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailingDTO> detail(@PathVariable Long id) {
        Optional<Topic> optTopic = service.findById(id);
        if (optTopic.isPresent()) {
            Topic topic = optTopic.get();
            return ResponseEntity.ok(new TopicDetailingDTO(topic));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
