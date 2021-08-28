package br.com.ia.challenge.controller;

import br.com.ia.challenge.model.GithubDTO;
import br.com.ia.challenge.model.User;
import br.com.ia.challenge.model.UserDTO;
import br.com.ia.challenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream()
                .map(u -> u.getDTO())
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findOne(@PathVariable Integer id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(u.getDTO()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody User user) {
        return userService.findById(user.getId())
                .map(u -> ResponseEntity.ok(userService.update(u).getDTO()))
                .orElseGet(() -> new ResponseEntity<>(userService.save(user).getDTO(), HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody User user, @PathVariable Integer id) {
        return userService.findById(id)
                .map(u -> {
                    u.setAdmin(user.isAdmin());
                    u.setEmail(user.getEmail());
                    u.setGithub(user.getGithub());
                    u.setName(user.getName());
                    u.setLogin(user.getLogin());
                    u.setPassword(user.getPassword());
                    return ResponseEntity.ok(userService.update(u).getDTO());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return userService.findById(id)
            .map(u -> {
                userService.delete(id);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/github")
    public ResponseEntity<GithubDTO> findGithub(@PathVariable Integer userId) {
        GithubDTO githubDTO = userService.findGithub(userId);
        return githubDTO != null ? ResponseEntity.ok(githubDTO) : ResponseEntity.notFound().build();
    }

}
