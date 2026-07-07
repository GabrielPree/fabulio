package br.com.fabulio.Fabulio.dto;

import br.com.fabulio.Fabulio.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       java.util.List<Categoria> generos,
                       String atores,
                       String poster,
                       String anoLancamento,
                       String sinopse,
                       String background,
                       String logo) {
}
