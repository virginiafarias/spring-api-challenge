package br.com.ia.challenge.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GithubDTO {

    private String id;

    private String avatarUrl;

    private List<String> repositories;

}
