package br.com.fabulio.Fabulio.repository;

import br.com.fabulio.Fabulio.dto.FilmeResumoDTO;
import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    Optional<Filme> findByTituloContainingIgnoreCase(String nomeFilme);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.FilmeResumoDTO(f.id, f.titulo, f.avaliacao, f.poster) " +
            "FROM Filme f ORDER BY f.avaliacao DESC LIMIT 40")
    List<FilmeResumoDTO> obterMelhoresFilmes();

    @Query("SELECT new br.com.fabulio.Fabulio.dto.FilmeResumoDTO(f.id, f.titulo, f.avaliacao, f.poster) " +
            "FROM Filme f JOIN f.generos g WHERE g = :categoria")
    List<FilmeResumoDTO> filmesGenero(@Param("categoria") Categoria categoria);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.FilmeResumoDTO(f.id, f.titulo, f.avaliacao, f.poster) " +
            "FROM Filme f ORDER BY f.anoLancamento DESC LIMIT 40")
    List<FilmeResumoDTO> filmesLancamentos();

    @Query("SELECT f FROM Filme f WHERE f.id = :id")
    Optional<Filme> findFilmeCompletoPorId(@Param("id") Long id);

    @Query("SELECT new br.com.fabulio.Fabulio.dto.FilmeResumoDTO(f.id, f.titulo, f.avaliacao, f.poster) FROM Filme f")
    List<FilmeResumoDTO> todosFilmes();
}
