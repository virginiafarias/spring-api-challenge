package br.com.ia.challenge.controller;

import br.com.ia.challenge.model.User;
import br.com.ia.challenge.model.UserDTO;
import br.com.ia.challenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users
                .map(u -> u.getDTO()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findOne(@PathVariable Integer id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(u.getDTO()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user).getDTO(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody User user, @PathVariable Integer id,
                                          @AuthenticationPrincipal User authenticatedUser) {
        return userService.findById(id)
                .map(u -> {
                    u.setAdmin(user.isAdmin());
                    u.setEmail(user.getEmail());
                    u.setGithub(user.getGithub());
                    u.setName(user.getName());
                    u.setLogin(user.getLogin());
                    if (authenticatedUser.isAdmin())
                        u.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public ResponseEntity<?> findGithub(@PathVariable("id") Integer userId) {
        return userService.findById(userId)
                .map(u -> {
                    try {
                        return ResponseEntity.ok(userService.findGithub(u.getGithub()));
                    } catch (Exception e) {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
