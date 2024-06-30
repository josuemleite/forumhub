package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.dto.user.UserCreationDTO;
import br.com.josuemleite.forumhub.dto.user.UserDetailingDTO;
import br.com.josuemleite.forumhub.dto.user.UserListingDTO;
import br.com.josuemleite.forumhub.dto.user.UserUpdatingDTO;
import br.com.josuemleite.forumhub.model.User;
import br.com.josuemleite.forumhub.service.UserService;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDetailingDTO> create(@RequestBody @Valid UserCreationDTO data, UriComponentsBuilder uriComponentsBuilder) {
        Optional<User> existingUser = service.findByEmail(data.email());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserDetailingDTO(existingUser.get()));
        }
        UserDetailingDTO userDetailingDTO = service.insert(data);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(userDetailingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(userDetailingDTO);
    }

    @GetMapping
    public ResponseEntity<Page<UserListingDTO>> list(@PageableDefault(sort = {"name"}) Pageable pageable) {
        Page<UserListingDTO> page = service.findAll(pageable).map(UserListingDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailingDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdatingDTO data) {
        Optional<User> existingUser = service.findByEmail(data.email());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserDetailingDTO(existingUser.get()));
        }
        Optional<User> optUser = service.findById(id);
        if (optUser.isPresent()) {
            UserDetailingDTO userDetailingDTO = service.update(id, data);
            return ResponseEntity.ok(userDetailingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<User> optUser = service.findById(id);
        if (optUser.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailingDTO> detail(@PathVariable Long id) {
        Optional<User> optUser = service.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            return ResponseEntity.ok(new UserDetailingDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

