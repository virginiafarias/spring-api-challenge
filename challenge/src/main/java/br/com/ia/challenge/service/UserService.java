package br.com.ia.challenge.service;

import br.com.ia.challenge.model.GithubDTO;
import br.com.ia.challenge.model.User;
import br.com.ia.challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        //TODO: encrypt password
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public GithubDTO findGithub(Integer userId) {
        //TODO: connect to github API
        return new GithubDTO();
    }
}
