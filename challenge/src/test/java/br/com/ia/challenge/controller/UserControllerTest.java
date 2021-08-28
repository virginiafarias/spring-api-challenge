package br.com.ia.challenge.controller;

import br.com.ia.challenge.model.User;
import br.com.ia.challenge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

    private final User user1 = User.builder().id(1).name("User One").email("userone@mail.com")
            .github("userone").login("userone").admin(true).build();
    private final User user1Updated = User.builder().id(1).name("_User One").email("_userone@mail.com")
            .github("_userone").login("_userone").admin(false).build();
    private final User user2 = User.builder().id(2).name("User Two").email("usertwo@mail.com")
            .github("usertwo").login("usertwo").admin(false).build();

    @BeforeEach
    public void setUp() {
        when(userService.findAll()).thenReturn(List.of(user1, user2));
        when(userService.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userService.findById(3)).thenReturn(Optional.empty());
        doNothing().when(userService).delete(1);
        when(userService.update(user1)).thenReturn(user1Updated);
    }

    @Test
    public void whenFindAllUsers_thenSuccess() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("User One")))
                .andExpect(jsonPath("$[1].name", is("User Two")));
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

}
