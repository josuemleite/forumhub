package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.domain.dto.authentication.AuthenticationDTO;
import br.com.josuemleite.forumhub.domain.dto.authentication.TokenJWTDTO;
import br.com.josuemleite.forumhub.domain.model.User;
import br.com.josuemleite.forumhub.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager manager;

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJWTDTO> logIn(@RequestBody @Valid AuthenticationDTO data) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticate = manager.authenticate(authenticationToken);
        String tokenJWT = tokenService.generateToken((User) authenticate.getPrincipal());
        return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
    }
}
