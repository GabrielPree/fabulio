package br.com.fabulio.Fabulio.service;

import br.com.fabulio.Fabulio.dto.FilmeDTO;
import br.com.fabulio.Fabulio.dto.FilmeResumoDTO;
import br.com.fabulio.Fabulio.exception.InvalidParameterException;
import br.com.fabulio.Fabulio.exception.ResourceNotFoundException;
import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Filme;
import br.com.fabulio.Fabulio.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository repositorio;

    public FilmeDTO obterFilmeID(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID do filme deve ser um valor positivo");
        }

        Filme filme = repositorio.findFilmeCompletoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filme", id));

        return new FilmeDTO(filme);
    }

    public List<FilmeResumoDTO> obterTodosFilmes() {
        List<FilmeResumoDTO> filmes = repositorio.todosFilmes();

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme encontrado no sistema");
        }
        return filmes;
    }

    public List<FilmeResumoDTO> obterMelhoresFilmes() {
        List<FilmeResumoDTO> filmes = repositorio.obterMelhoresFilmes();
        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme com avaliação alta encontrado");
        }
        return filmes;
    }

    public List<FilmeResumoDTO> obterLancamentos() {
        List<FilmeResumoDTO> filmes = repositorio.filmesLancamentos();

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum lançamento encontrado no momento");
        }

        return filmes;
    }

    public List<FilmeResumoDTO> obterFilmesGenero(String nomeGenero) {
        if (nomeGenero == null || nomeGenero.trim().isEmpty()) {
            throw new InvalidParameterException("O nome do gênero não pode ser vazio");
        }

        Categoria categoria;
        try {
            categoria = Categoria.fromPortugues(nomeGenero);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Gênero inválido: " + nomeGenero);
        }

        List<FilmeResumoDTO> filmes = repositorio.filmesGenero(categoria);

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme encontrado para o gênero: " + nomeGenero);
        }

        return filmes;
    }
}
