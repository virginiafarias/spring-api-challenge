package br.com.ia.challenge.model;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GithubDTO {

    private Long id;

    private String avatar_url;

    private List<GithubRepoDTO> repositories;

}
