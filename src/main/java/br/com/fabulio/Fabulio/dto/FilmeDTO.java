package br.com.fabulio.Fabulio.dto;

import br.com.fabulio.Fabulio.model.Categoria;

public record FilmeDTO(Long id,
                       String titulo,
                       Double avaliacao,
                       java.util.List<Categoria> generos,
                       String diretor,
                       String atores,
                       String poster,
                       Integer anoLancamento,
                       Integer duracao,
                       String sinopse,
                       String background,
                       String logo) {

}
