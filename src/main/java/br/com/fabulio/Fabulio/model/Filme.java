package br.com.fabulio.Fabulio.model;

import br.com.fabulio.Fabulio.service.ConsultaGroq;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "filmes")
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 200)
    private String titulo;
    private Double avaliacao;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Categoria> generos;
    @Column(length = 500)
    private String diretor;
    @Column(length = 500)
    private String atores;
    @Column(length = 500)
    private String poster;
    @Column(length = 20)
    private Integer anoLancamento;
    @Column(length = 20)
    private Integer duracao;
    @Column(length = 2000)
    private String sinopse;
    @Column(length = 255)
    private String imdbID;
    @Column(length = 500)
    private String background;
    @Column(length = 500)
    private String logo;

    public Filme() {
    }

    public Filme(DadosFilme dadosFilme) {
        this.titulo = dadosFilme.titulo();
        this.avaliacao = Optional.ofNullable(dadosFilme.avaliacao())
                .map(Double::valueOf)
                .orElse(0.0);
        this.generos = Arrays.stream(dadosFilme.genero().split(","))
                .map(String::trim)
                .map(Categoria::fromString)
                .toList();
        this.diretor = dadosFilme.diretor();
        this.atores = dadosFilme.atores();
        this.poster = dadosFilme.poster();
        this.anoLancamento = dadosFilme.anoLancamento();
        this.duracao = Optional.ofNullable(dadosFilme.runtime())
                .filter(runtime -> !runtime.equals("N/A"))
                .map(runtime -> runtime.replace(" min", "").trim())
                .map(Integer::valueOf)
                .orElse(null);
        this.sinopse = ConsultaGroq.obterTraducao(dadosFilme.sinopse());
        this.imdbID = dadosFilme.imdbID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public List<Categoria> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Categoria> generos) {
        this.generos = generos;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return  "titulo='" + titulo + '\'' +
                ", avaliacao=" + avaliacao +
                ", genero=" + generos +
                ", diretor='" + diretor + '\'' +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", anoLancamento=" + anoLancamento +
                ", duracao=" + duracao +
                ", sinopse='" + sinopse + '\'' +
                ", imdbID='" + imdbID +
                ", background='" + background + '\'' +
                ", logo='" + logo;
    }
}
