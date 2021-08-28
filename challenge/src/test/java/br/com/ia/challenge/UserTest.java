package br.com.ia.challenge;

import br.com.ia.challenge.model.User;
import br.com.ia.challenge.model.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void getUserDTO() {
        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime updatedDate = LocalDateTime.now();
        final User user = User.builder()
                .id(1).name("User").email("user@mail.com")
                .github("user").login("user").admin(true)
                .createdDate(createdDate).updatedDate(updatedDate)
                .build();

        UserDTO actual = user.getDTO();
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getGithub(), actual.getGithub());
        assertEquals(user.getLogin(), actual.getLogin());
        assertEquals(user.isAdmin(), actual.isAdmin());
        assertEquals(user.getCreatedDate(), actual.getCreatedDate());
        assertEquals(user.getUpdatedDate(), actual.getUpdatedDate());
    }

}
