package br.com.fabulio.Fabulio.model;

import br.com.fabulio.Fabulio.service.ConsultaGroq;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 200)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Categoria> generos;
    @Column(length = 300)
    private String atores;
    @Column(length = 500)
    private String poster;
    @Column(length = 20)
    private String anoLancamento;
    @Column(length = 2000)
    private String sinopse;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();
    @Column(length = 100)
    private String imdbID;
    @Column(length = 500)
    private String background;
    @Column(length = 500)
    private String logo;

    public Serie() {
    }

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = Optional.ofNullable(dadosSerie.avaliacao())
                .map(Double::valueOf)
                .orElse(0.0);
        this.generos = Arrays.stream(dadosSerie.genero().split(","))
                .map(String::trim)
                .map(Categoria::fromString)
                .toList();
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.anoLancamento = dadosSerie.anoLancamento();
        this.sinopse = ConsultaGroq.obterTraducao(dadosSerie.sinopse());
        this.imdbID = dadosSerie.imdbID();
    }

    private Integer extrairAnoDoYear(String year) {
        if (year == null || year.isEmpty() || year.equals("N/A")) {
            return null;
        }
        // Remove qualquer coisa depois do primeiro ano
        String primeiroAno = year.split("–")[0].split("-")[0].trim();

        try {
            return Integer.parseInt(primeiroAno);
        } catch (NumberFormatException e) {
            return null;
        }
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

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
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

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
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
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", genero=" + generos +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", Episodios='" + episodios + '\'' +
                ", imdbID='" + imdbID +
                ", background='" + background + '\'' +
                ", logo='" + logo;

    }
}