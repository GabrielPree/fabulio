package br.com.fabulio.Fabulio.dto;

import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Filme;

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

    public FilmeDTO(Filme filme) {
        this(
                filme.getId(),
                filme.getTitulo(),
                filme.getAvaliacao(),
                filme.getGeneros(),
                filme.getDiretor(),
                filme.getAtores(),
                filme.getPoster(),
                filme.getAnoLancamento(),
                filme.getDuracao(),
                filme.getSinopse(),
                filme.getBackground(),
                filme.getLogo()
        );
    }
}
