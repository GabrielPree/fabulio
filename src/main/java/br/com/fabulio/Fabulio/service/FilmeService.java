package br.com.fabulio.Fabulio.service;

import br.com.fabulio.Fabulio.dto.FilmeDTO;
import br.com.fabulio.Fabulio.exception.InvalidParameterException;
import br.com.fabulio.Fabulio.exception.ResourceNotFoundException;
import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Filme;
import br.com.fabulio.Fabulio.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository repositorio;

    private List<FilmeDTO> converteDados(List<Filme> filmes){
        return filmes.stream()
                .map(f -> new FilmeDTO(
                        f.getId(),
                        f.getTitulo(),
                        f.getAvaliacao(),
                        f.getGeneros(),
                        f.getDiretor(),
                        f.getAtores(),
                        f.getPoster(),
                        f.getAnoLancamento(),
                        f.getDuracao(),
                        f.getSinopse(),
                        f.getBackground(),
                        f.getLogo()
                ))
                .collect(Collectors.toList());
    }

    public FilmeDTO obterFilmeID(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID do filme deve ser um valor positivo");
        }

        Filme filme = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filme", id));

        return new FilmeDTO(
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

    public List<FilmeDTO> obterTodosFilmes() {
        List<Filme> filmes = repositorio.findAll();

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme encontrado no sistema");
        }

        return converteDados(filmes);
    }

    public List<FilmeDTO> obterMelhoresFilmes() {
        List<Filme> filmes = repositorio.obterMelhoresFilmes();

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme com avaliação alta encontrado");
        }

        return converteDados(filmes);
    }

    public List<FilmeDTO> obterLancamentos() {
        List<Filme> filmes = repositorio.filmesLancamentos();

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum lançamento encontrado no momento");
        }

        return converteDados(filmes);
    }

    public List<FilmeDTO> obterFilmesGenero(String nomeGenero) {
        if (nomeGenero == null || nomeGenero.trim().isEmpty()) {
            throw new InvalidParameterException("O nome do gênero não pode ser vazio");
        }

        Categoria categoria;
        try {
            categoria = Categoria.fromPortugues(nomeGenero);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Gênero inválido: " + nomeGenero);
        }

        List<Filme> filmes = repositorio.filmesGenero(categoria);

        if (filmes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum filme encontrado para o gênero: " + nomeGenero);
        }

        return converteDados(filmes);
    }
}
