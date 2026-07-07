/*
package br.com.fabulio.Fabulio;

import br.com.fabulio.Fabulio.principal.Principal;
import br.com.fabulio.Fabulio.repository.FilmeRepository;
import br.com.fabulio.Fabulio.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Inicia o principal para adicionar ou remover filmes e séries na api
@SpringBootApplication
class FabulioApplicationSemWeb implements CommandLineRunner {

    @Autowired
    private SerieRepository repositorioSerie;

    @Autowired
    private FilmeRepository repositorioFilme;

    public static void main(String[] args) {

        SpringApplication app =
                new SpringApplication(FabulioApplicationSemWeb.class);

        app.setWebApplicationType(WebApplicationType.NONE);

        app.run(args);
    }

    @Override
    public void run(String... args) {
        Principal principal =
                new Principal(repositorioSerie, repositorioFilme);

        principal.exibeMenu();
    }
}

 */