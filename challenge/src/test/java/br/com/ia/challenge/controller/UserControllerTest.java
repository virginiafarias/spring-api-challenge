package br.com.ia.challenge.controller;

import br.com.ia.challenge.model.GithubDTO;
import br.com.ia.challenge.model.GithubRepoDTO;
import br.com.ia.challenge.model.User;
import br.com.ia.challenge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

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
    public void setUp() throws Exception {
        when(userService.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userService.findById(3)).thenReturn(Optional.empty());
        doNothing().when(userService).delete(1);
        when(userService.update(user1)).thenReturn(user1Updated);
        when(userService.findGithub("userone")).thenReturn(githubUser1);
        when(userService.findGithub("usertwo")).thenThrow(new Exception("Github user does not exist"));
    }

    @Test
    public void whenFindFirstPageWithSizeTwoWithThreeElements_thenReturnTwoElements() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<User> pageUsers = new PageImpl<>(List.of(user1, user2), pageRequest, users.size());
        when(userService.findAll(pageRequest)).thenReturn(pageUsers);
        mockMvc.perform(get("/users?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.last", is(false)));
    }

    @Test
    public void whenFindSecondPageWithSizeTwoWithThreeElements_thenReturnOneElements() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 2);
        Page<User> pageUsers = new PageImpl<>(List.of(user3), pageRequest, users.size());
        when(userService.findAll(pageRequest)).thenReturn(pageUsers);
        mockMvc.perform(get("/users?page=1&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.first", is(false)))
                .andExpect(jsonPath("$.last", is(true)));
    }

    @Test
    public void whenFindThirdPageWithSizeTwoWithThreeElements_thenReturnNoElements() throws Exception {
        PageRequest pageRequest = PageRequest.of(2, 2);
        Page<User> pageUsers = new PageImpl<>(List.of(), pageRequest, users.size());
        when(userService.findAll(pageRequest)).thenReturn(pageUsers);
        mockMvc.perform(get("/users?page=2&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    public void whenFindExistingUser_thenSuccess() throws Exception {
        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("User One")));
    }

    @Test
    public void whenFindNotExistingUser_thenNotFound() throws Exception {
        mockMvc.perform(get("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteExistingUser_thenSuccess() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteNotExistingUser_thenNotFound() throws Exception {
        mockMvc.perform(delete("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateExistingUser_thenSuccess() throws Exception {
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("_User One")))
                .andExpect(jsonPath("$.email", is("_userone@mail.com")))
                .andExpect(jsonPath("$.github", is("_userone")))
                .andExpect(jsonPath("$.login", is("_userone")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    public void whenUpdateNotExistingUser_thenNotFound() throws Exception {
        mockMvc.perform(put("/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateUserWithoutId_thenSuccess() throws Exception {
        User user = User.builder().name("User One").email("userone@mail.com")
                .github("userone").login("userone").admin(true).build();
        when(userService.save(user)).thenReturn(user1);

        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("User One")))
                .andExpect(jsonPath("$.email", is("userone@mail.com")))
                .andExpect(jsonPath("$.github", is("userone")))
                .andExpect(jsonPath("$.login", is("userone")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    public void whenCreateUserWithId_thenUpdate() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("_User One")))
                .andExpect(jsonPath("$.email", is("_userone@mail.com")))
                .andExpect(jsonPath("$.github", is("_userone")))
                .andExpect(jsonPath("$.login", is("_userone")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    public void whenFindGihubExistingUserExistingRepository_thenSuccess() throws Exception {
        mockMvc.perform(get("/users/1/github")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(githubUser1.getId())))
                .andExpect(jsonPath("$.avatar_url", is(githubUser1.getAvatar_url())))
                .andExpect(jsonPath("$.repositories", hasSize(3)))
                .andExpect(jsonPath("$.repositories[0]", is("repo1")))
                .andExpect(jsonPath("$.repositories[1]", is("repo2")))
                .andExpect(jsonPath("$.repositories[2]", is("repo3")));
    }

    @Test
    public void whenFindGihubExistingUserNotExistingRepository_thenNotFound() throws Exception {
        mockMvc.perform(get("/users/2/github")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindGihubNotExistingUser_thenNotFound() throws Exception {
        mockMvc.perform(get("/users/3/github")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
