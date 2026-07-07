package br.com.fabulio.Fabulio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosFilme(@JsonAlias("Title") String titulo,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Director") String diretor,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Year")  Integer anoLancamento,
                         @JsonAlias("Runtime") String runtime,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("imdbID") String imdbID) {
}
