package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.dto.answer.AnswerCreationDTO;
import br.com.josuemleite.forumhub.dto.answer.AnswerDetailingDTO;
import br.com.josuemleite.forumhub.dto.answer.AnswerListingDTO;
import br.com.josuemleite.forumhub.dto.answer.AnswerUpdatingDTO;
import br.com.josuemleite.forumhub.model.Answer;
import br.com.josuemleite.forumhub.service.AnswerService;
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
@RequestMapping("/respostas")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;

    @PostMapping
    public ResponseEntity<AnswerDetailingDTO> create(@RequestBody @Valid AnswerCreationDTO data, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Answer> existingAnswer = service.findByMessage(data.message());
        if (existingAnswer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AnswerDetailingDTO(existingAnswer.get()));
        }
        AnswerDetailingDTO answerDetailingDTO = service.insert(data);
        URI uri = uriComponentsBuilder.path("/respostas/{id}").buildAndExpand(answerDetailingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(answerDetailingDTO);
    }

    @GetMapping
    public ResponseEntity<Page<AnswerListingDTO>> list(@PageableDefault(sort = {"creationDate"}) Pageable pageable) {
        Page<AnswerListingDTO> page = service.findAllOrderedBySolutionAndCreationDate(pageable).map(AnswerListingDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerDetailingDTO> update(@PathVariable Long id, @RequestBody @Valid AnswerUpdatingDTO data) {
        Optional<Answer> existingAnswer = service.findByMessage(data.message());
        if (existingAnswer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AnswerDetailingDTO(existingAnswer.get()));
        }
        Optional<Answer> optAnswer = service.findById(id);
        if (optAnswer.isPresent()) {
            AnswerDetailingDTO answerDetailingDTO = service.update(id, data);
            return ResponseEntity.ok(answerDetailingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Answer> optAnswer = service.findById(id);
        if (optAnswer.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDetailingDTO> detail(@PathVariable Long id) {
        Optional<Answer> optAnswer = service.findById(id);
        if (optAnswer.isPresent()) {
            Answer answer = optAnswer.get();
            return ResponseEntity.ok(new AnswerDetailingDTO(answer));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
