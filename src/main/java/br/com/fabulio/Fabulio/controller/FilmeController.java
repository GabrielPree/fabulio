package br.com.fabulio.Fabulio.controller;

import br.com.fabulio.Fabulio.dto.FilmeDTO;
import br.com.fabulio.Fabulio.dto.FilmeResumoDTO;
import br.com.fabulio.Fabulio.service.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    @Autowired
    private FilmeService servico;

    @GetMapping
    public List<FilmeResumoDTO> obterFilmes() {return servico.obterTodosFilmes();}

    @GetMapping("/melhores")
    public List<FilmeResumoDTO> obterMelhoresFilmes() {return servico.obterMelhoresFilmes();}

    @GetMapping("/lancamentos")
    public List<FilmeResumoDTO> obterLancamentos() {
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")
    public FilmeDTO obterPorId(@PathVariable Long id) {return servico.obterFilmeID(id);}

    @GetMapping("/categoria/{nomeGenero}")
    public List<FilmeResumoDTO> obterCategoriasPorGenero(@PathVariable String nomeGenero) {
        return servico.obterFilmesGenero(nomeGenero);}

}

