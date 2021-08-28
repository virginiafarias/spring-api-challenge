package br.com.ia.challenge.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Builder @EqualsAndHashCode
public class UserDTO {

    private Integer id;

    private String name;

    private String login;

    private String github;

    private String email;

    private boolean admin;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
