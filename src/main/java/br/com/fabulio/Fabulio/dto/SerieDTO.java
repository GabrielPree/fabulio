package br.com.fabulio.Fabulio.dto;

import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Serie;

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
    public SerieDTO(Serie serie) {
        this(
                serie.getId(),
                serie.getTitulo(),
                serie.getTotalTemporadas(),
                serie.getAvaliacao(),
                serie.getGeneros(),
                serie.getAtores(),
                serie.getPoster(),
                serie.getAnoLancamento(),
                serie.getSinopse(),
                serie.getBackground(),
                serie.getLogo()
        );
    }
}
