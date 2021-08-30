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

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User save(User user);

    User update(User user);

    Optional<User> findById(Integer id);

    void delete(Integer id);

    GithubDTO findGithub(String githubUser) throws Exception;


}
