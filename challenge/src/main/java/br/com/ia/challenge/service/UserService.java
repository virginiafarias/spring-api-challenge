package br.com.ia.challenge.service;

import br.com.ia.challenge.model.GithubDTO;
import br.com.ia.challenge.model.GithubRepoDTO;
import br.com.ia.challenge.model.User;
import br.com.ia.challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String githubEndpoint = "https://api.github.com/";

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User save(User user) {
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public GithubDTO findGithub(String githubUser) throws Exception {
        GithubDTO github = restTemplate.getForObject(githubEndpoint.concat("/users/").concat(githubUser), GithubDTO.class);
        if (github == null) {
            throw new Exception("Github user does not exist");
        }
        GithubRepoDTO[] repositories = restTemplate.getForObject(githubEndpoint.concat("/users/")
                .concat(githubUser).concat("/repos"), GithubRepoDTO[].class);
        github.setRepositories(List.of(repositories));
        return github;
    }
}
