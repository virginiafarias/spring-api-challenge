package br.com.ia.challenge.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GithubRepoDTO {

    private String name;

    private String html_url;

}
