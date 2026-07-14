package br.com.fabulio.Fabulio.repository;

import br.com.fabulio.Fabulio.dto.SerieResumoDTO;
import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Episodio;
import br.com.fabulio.Fabulio.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository  extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.SerieResumoDTO(s.id, s.titulo, s.avaliacao, s.poster) " +
            "FROM Serie s ORDER BY s.avaliacao DESC LIMIT 40")
    List<SerieResumoDTO> melhoresSeries();

    @Query("SELECT new br.com.fabulio.Fabulio.dto.SerieResumoDTO(s.id, s.titulo, s.avaliacao, s.poster) " +
            "FROM Serie s JOIN s.generos g WHERE g = :categoria")
    List<SerieResumoDTO> seriesGenero(@Param("categoria") Categoria categoria);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.SerieResumoDTO(s.id, s.titulo, s.avaliacao, s.poster) " +
            "FROM Serie s JOIN s.episodios e " +
            "GROUP BY s.id, s.titulo, s.avaliacao, s.poster " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 40")
    List<SerieResumoDTO> seriesLancamentos();

    @Query("SELECT s FROM Serie s WHERE s.id = :id")
    Optional<Serie> findSerieCompletaPorId(@Param("id") Long id);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Integer numero);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> obterTopEpisodios(Long id);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.SerieResumoDTO(s.id, s.titulo, s.avaliacao, s.poster) FROM Serie s")
    List<SerieResumoDTO> todasSeries();
}