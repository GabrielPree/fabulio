package br.com.fabulio.Fabulio.repository;

import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    Optional<Filme> findByTituloContainingIgnoreCase(String nomeFilme);

    @Query("SELECT f FROM Filme f ORDER BY f.avaliacao DESC LIMIT 40")
    List<Filme> obterMelhoresFilmes();

    @Query("SELECT f FROM Filme f JOIN f.generos g WHERE g = :categoria")
    List<Filme> filmesGenero(@Param("categoria") Categoria categoria);

    @Query("SELECT f FROM Filme f ORDER BY f.anoLancamento DESC LIMIT 40")
    List<Filme> filmesLancamentos();
}
