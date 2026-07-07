package br.com.fabulio.Fabulio.controller;

import br.com.fabulio.Fabulio.dto.EpisodioDTO;
import br.com.fabulio.Fabulio.dto.SerieDTO;
import br.com.fabulio.Fabulio.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servico;

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return servico.todasSeries();
    }

    @GetMapping("melhores")
    public List<SerieDTO> obterMelhoresSeries(){
        return servico.obterMelhoresSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterSeriesLancamentos(){
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return servico.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return servico.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Integer numero){
        return servico.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterCategoriasPorGenero(@PathVariable String nomeGenero){
        return servico.obterSeriesGenero(nomeGenero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTopEpisodios(@PathVariable Long id){
        return servico.obterTopEpisodios(id);
    }
}