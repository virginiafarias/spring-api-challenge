package br.com.ia.challenge.service;

import br.com.ia.challenge.model.GithubDTO;
import br.com.ia.challenge.model.GithubRepoDTO;
import br.com.ia.challenge.model.User;
import br.com.ia.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    private final User user1 = User.builder()
            .id(1).name("User One").email("userone@mail.com")
            .github("userone").login("userone").admin(true)
            .build();
    private final User user1Updated = User.builder()
            .id(1).name("_User One").email("_userone@mail.com")
            .github("_userone").login("_userone").admin(false)
            .build();
    private final User user2 = User.builder()
            .id(2).name("User Two").email("usertwo@mail.com")
            .github("usertwo").login("usertwo").admin(false)
            .build();
    private final User user3 = User.builder()
            .id(3).name("User Three").email("userthree@mail.com")
            .github("userthree").login("userthree").admin(false)
            .build();
    private final List<User> users = List.of(user1, user2, user3);
    private final GithubDTO githubUser1 = GithubDTO.builder()
            .id(Long.getLong("7314878"))
            .avatar_url("https://avatars.githubusercontent.com/u/7314878?v=4")
            .repositories(List.of(
                    new GithubRepoDTO("repo1", "https://github.com/userone/repo1"),
                    new GithubRepoDTO("repo2", "https://github.com/userone/repo2"),
                    new GithubRepoDTO("repo3", "https://github.com/userone/repo3")
                    ))
            .build();

    @BeforeEach
    public void setUp() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(3)).thenReturn(Optional.empty());
        when(userRepository.save(user1)).thenReturn(user1Updated);
        when(restTemplate.getForObject("https://api.github.com/users/userone", GithubDTO.class)).thenReturn(githubUser1);
    }

    @Test
    public void whenFindFirstPageWithSizeTwoWithThreeElements_thenReturnTwoElements() throws Exception {
        Pageable pageable = Pageable.ofSize(2).withPage(0);
        Page<User> pageUsers = new PageImpl<>(List.of(user1, user2), pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(pageUsers);
        Page<User> actual = userService.findAll(pageable);
        assertEquals(pageUsers.getContent(), actual.getContent());
        assertEquals(3, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertEquals(2, actual.getNumberOfElements());
        assertTrue(pageUsers.isFirst());
        assertFalse(pageUsers.isLast());
        assertTrue(pageUsers.hasNext());
        assertFalse(pageUsers.hasPrevious());
    }

    @Test
    public void whenFindSecondPageWithSizeTwoWithThreeElements_thenReturnOneElements() throws Exception {
        Pageable pageable = Pageable.ofSize(2).withPage(1);
        Page<User> pageUsers = new PageImpl<>(List.of(user3), pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(pageUsers);
        Page<User> actual = userService.findAll(pageable);
        assertEquals(pageUsers.getContent(), actual.getContent());
        assertEquals(3, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertEquals(1, actual.getNumberOfElements());
        assertFalse(pageUsers.isFirst());
        assertTrue(pageUsers.isLast());
        assertFalse(pageUsers.hasNext());
        assertTrue(pageUsers.hasPrevious());
    }

    @Test
    public void whenFindThirdPageWithSizeTwoWithThreeElements_thenReturnNoElements() throws Exception {
        Pageable pageable = Pageable.ofSize(2).withPage(2);
        Page<User> pageUsers = new PageImpl<>(List.of(), pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(pageUsers);
        Page<User> actual = userService.findAll(pageable);
        assertEquals(pageUsers.getContent(), actual.getContent());
        assertEquals(3, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertEquals(0, actual.getNumberOfElements());
        assertFalse(pageUsers.isFirst());
        assertTrue(pageUsers.isLast());
        assertFalse(pageUsers.hasNext());
        assertTrue(pageUsers.hasPrevious());
    }

    @Test
    public void whenFindExistingUser_sucess() {
        Optional<User> user = userService.findById(1);
        assertTrue(user.isPresent());
        assertEquals(user1, user.get());
    }

    @Test
    public void whenFindNotExistingUser_empty() {
        Optional<User> user = userService.findById(3);
        assertFalse(user.isPresent());
    }

    @Test
    public void whenSaveUser_success() {
        User user = User.builder().name("User One").email("userone@mail.com")
                .github("userone").login("userone").password("userone").admin(true).build();
        when(userRepository.save(user)).thenReturn(user1);
        User userSaved = userService.save(user);
        assertEquals(1, userSaved.getId());
        assertEquals("User One", userSaved.getName());
        assertEquals("userone@mail.com", userSaved.getEmail());
        assertEquals("userone", userSaved.getGithub());
        assertEquals("userone", userSaved.getLogin());
        assertEquals(true, userSaved.isAdmin());
    }

    @Test
    public void whenUpdateExistingUser_success() {
        User userUpdated = userService.update(user1);
        assertEquals(1, userUpdated.getId());
        assertEquals("_User One", userUpdated.getName());
        assertEquals("_userone@mail.com", userUpdated.getEmail());
        assertEquals("_userone", userUpdated.getGithub());
        assertEquals("_userone", userUpdated.getLogin());
        assertEquals(false, userUpdated.isAdmin());
    }

    @Test
    public void whenUpdateNotExistingUser_save() {
        User user = User.builder().name("User One").email("userone@mail.com")
                .github("userone").login("userone").password("userone").admin(true).build();
        when(userRepository.save(user)).thenReturn(user1);
        User userUpdated = userService.update(user);
        assertEquals(1, userUpdated.getId());
        assertEquals("User One", userUpdated.getName());
        assertEquals("userone@mail.com", userUpdated.getEmail());
        assertEquals("userone", userUpdated.getGithub());
        assertEquals("userone", userUpdated.getLogin());
        assertEquals(true, userUpdated.isAdmin());
    }

    @Test
    public void whenFindExistingGithub_success() {
        try {
            GithubDTO githubDTO = userService.findGithub("userone");
            assertEquals(githubUser1.getId(), githubDTO.getId());
            assertEquals(githubUser1.getAvatar_url(), githubDTO.getAvatar_url());
            assertEquals(githubUser1.getRepositories(), githubDTO.getRepositories());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void whenFindNotExistingGithub_exception() {
        when(restTemplate.getForObject("https://api.github.com/users/usertwo", GithubDTO.class)).thenThrow(new Exception("Github user does not exist"));
        Exception exception = assertThrows(Exception.class,
                ()-> userService.findGithub("usertwo"));
        assertEquals("Github user does not exist", exception.getMessage());
    }

}
