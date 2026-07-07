package br.com.fabulio.Fabulio.service;

import br.com.fabulio.Fabulio.dto.EpisodioDTO;
import br.com.fabulio.Fabulio.dto.SerieDTO;
import br.com.fabulio.Fabulio.exception.InvalidParameterException;
import br.com.fabulio.Fabulio.exception.ResourceNotFoundException;
import br.com.fabulio.Fabulio.model.Categoria;
import br.com.fabulio.Fabulio.model.Serie;
import br.com.fabulio.Fabulio.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    private List<SerieDTO> converteDados(List<Serie> series){
      return  series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGeneros(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getAnoLancamento(),
                        s.getSinopse(),
                        s.getBackground(),
                        s.getLogo()
                ))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> todasSeries() {
        List<Serie> series = repositorio.findAll();

        if (series.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma série encontrada no sistema");
        }

        return converteDados(series);
    }

    public List<SerieDTO> obterMelhoresSeries() {
        List<Serie> series = repositorio.melhoresSeries();

        if (series.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma série com avaliação alta encontrada");
        }

        return converteDados(series);
    }

    public List<SerieDTO> obterLancamentos() {
        List<Serie> series = repositorio.seriesLancamentos();

        if (series.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum lançamento de série encontrado no momento");
        }

        return converteDados(series);
    }


    public SerieDTO obterPorId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID da série deve ser um valor positivo");
        }

        Serie serie = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Série", id));

        return new SerieDTO(
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

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID da série deve ser um valor positivo");
        }

        Serie serie = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Série", id));

        List<EpisodioDTO> episodios = serie.getEpisodios().stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());

        if (episodios.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum episódio encontrado para a série: " + serie.getTitulo());
        }

        return episodios;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Integer numero) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID da série deve ser um valor positivo");
        }

        if (numero == null || numero <= 0) {
            throw new InvalidParameterException("Número da temporada deve ser um valor positivo");
        }

        Serie serie = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Série", id));

        if (numero > serie.getTotalTemporadas()) {
            throw new InvalidParameterException(
                    String.format("A série '%s' possui apenas %d temporada(s)",
                            serie.getTitulo(), serie.getTotalTemporadas())
            );
        }

        List<EpisodioDTO> episodios = repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());

        if (episodios.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Nenhum episódio encontrado para a temporada %d da série '%s'",
                            numero, serie.getTitulo())
            );
        }

        return episodios;
    }

    public List<SerieDTO> obterSeriesGenero(String nomeGenero) {
        if (nomeGenero == null || nomeGenero.trim().isEmpty()) {
            throw new InvalidParameterException("O nome do gênero não pode ser vazio");
        }

        Categoria categoria;
        try {
            categoria = Categoria.fromPortugues(nomeGenero);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Gênero inválido: " + nomeGenero);
        }

        List<Serie> series = repositorio.seriesGenero(categoria);

        if (series.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma série encontrada para o gênero: " + nomeGenero);
        }

        return converteDados(series);
    }

    public List<EpisodioDTO> obterTopEpisodios(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID da série deve ser um valor positivo");
        }

        Serie serie = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Série", id));

        List<EpisodioDTO> episodios = repositorio.obterTopEpisodios(id)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());

        if (episodios.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Nenhum episódio encontrado para a série '%s'", serie.getTitulo())
            );
        }

        return episodios;
    }
}
