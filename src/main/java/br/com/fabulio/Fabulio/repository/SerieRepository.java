package br.com.fabulio.Fabulio.repository;

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

    @Query("SELECT s FROM Serie s ORDER BY s.avaliacao DESC LIMIT 40")
    List<Serie> melhoresSeries();

    @Query("SELECT s FROM Serie s JOIN s.generos g WHERE g = :categoria")
    List<Serie> seriesGenero(@Param("categoria") Categoria categoria);

    @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 40")
    List<Serie> seriesLancamentos();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Integer numero);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> obterTopEpisodios(Long id);

    @Query("SELECT s FROM Serie s WHERE s.anoLancamento IS NULL OR s.anoLancamento = ''")
    List<Serie> findSeriesSemAnoLancamento();
}