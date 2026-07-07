package br.com.fabulio.Fabulio.principal;

import br.com.fabulio.Fabulio.model.*;
import br.com.fabulio.Fabulio.repository.FilmeRepository;
import br.com.fabulio.Fabulio.repository.SerieRepository;
import br.com.fabulio.Fabulio.service.ConsumoAPI;
import br.com.fabulio.Fabulio.service.ConverteDados;
import br.com.fabulio.Fabulio.service.FanartApi;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = System.getenv("API_OMDB");
    private SerieRepository repositorioSerie;
    private FilmeRepository repositorioFilme;
    private List<Serie> series = new ArrayList<>();
    private List<Filme> filmes = new ArrayList<>();

    public Principal(SerieRepository repositorioSerie, FilmeRepository repositorioFilme) {
        this.repositorioSerie = repositorioSerie;
        this.repositorioFilme = repositorioFilme;
    }

    public void exibeMenu(){
        while (true) {
        var menu = """
                1 - Adicionar filme
                2 - Adicionar séries
                3 - Adicionar episódios para a Série
                4 - Listar Filmes
                5 - Listar séries buscadas
                6 - Adicionar Lista de Filmes
                7 - Adicionar Lista de Séries(Já com seus episódios)
                8 - Buscar imagens Fan Art filmes
                9 - Buscar imagens fan art séries
                10- apagar filme por id
                11- apagar série por id
                0 - Sair
                """;
        System.out.println(menu);
        String entrada = leitura.nextLine();

        int opcao;
        try {
            opcao = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
            continue;
        }
        switch (opcao) {
            case 1:
                adicionarFilme();
                break;
            case 2:
                adicionarSerie();
                break;
            case 3:
                adicionarEpisodioSerie();
                break;
            case 4:
                listarFilmes();
                break;
            case 5:
                listarSeries();
                break;
            case 6:
                adicionarListaFilmes();
                break;
            case 7:
                try {
                    adicionarListaSeries();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 8:
                adicionarImagensFilmes();
                break;
            case 9:
                adicionarImagensSeries();
                break;
            case 10:
                apagarFilmePorId();
                break;
            case 11:
                apagarSeriePorId();
                break;
            case 0:
                System.out.println("Saindo...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida");
        }
    }
}

    private void adicionarFilme() {
        DadosFilme dados = getDadosFilme();

        if (dados == null) {
            System.out.println("Filme não encontrado!");
            return;
        }

        Optional<Filme> filmeExistente =
                repositorioFilme.findByTituloContainingIgnoreCase(dados.titulo());

        if (filmeExistente.isPresent()) {
            System.out.println("Filme já cadastrado!");
            return;
        }

        Filme filme =  new Filme(dados);
        repositorioFilme.save(filme);
        System.out.println("Filme Salvo: " + dados);
    }

    private DadosFilme getDadosFilme() {
        System.out.println("Digite o nome do Filme: ");
        String nomeFilme = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeFilme.replace(" ", "+") + API_KEY);
        DadosFilme dados = conversor.obterDados(json, DadosFilme.class);
        return dados;
    }

    private void adicionarSerie() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorioSerie.save(serie);
        System.out.println("Série Salva:" + dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void adicionarEpisodioSerie(){
        listarSeries();
        System.out.println("Digite o nome da série para buscar os episódios");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorioSerie.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada
                        .getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorioSerie.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarFilmes() {
        filmes = repositorioFilme.findAll();
        filmes.stream()
                .sorted(Comparator.comparing(Filme::getTitulo))
                .forEach(System.out::println);
    }

    private void listarSeries() {
       series = repositorioSerie.findAll();
       series.stream()
               .sorted(Comparator.comparing(Serie::getTitulo))
               .forEach(System.out::println);
    }

    private void adicionarListaFilmes() {
        List<String> titulosFilmes = Arrays.asList(
                //Lista com filmes para teste
                "The Shawshank Redemption", "The Godfather", "The Dark Knight", "The Godfather Part II",
                "12 Angry Men", "Schindler's List", "The Lord of the Rings The Return of the King",
                "Pulp Fiction", "The Lord of the Rings The Fellowship of the Ring", "The Good the Bad and the Ugly",
                "Forrest Gump", "Fight Club", "The Lord of the Rings The Two Towers", "Inception",
                "The Matrix", "Goodfellas",
                "One Flew Over the Cuckoo's Nest", "Seven", "It's a Wonderful Life", "The Silence of the Lambs",
                "Saving Private Ryan", "Interstellar", "Casablanca", "Gladiator",
                "Taxi Driver", "Scarface", "ET the Extra-Terrestrial", "Indiana Jones and the Last Crusade",
                "Jurassic Park", "Back to the Future", "Back to the Future Part II", "Back to the Future Part III",
                "The Terminator", "Terminator 2 Judgment Day", "Top Gun",
                "The Pianist", "Kill Bill Vol 1", "Kill Bill Vol 2", "Inglourious Basterds", "The Social Network",
                "The Departed", "No Country for Old Men", "There Will Be Blood", "Slumdog Millionaire",
                "The Dark Knight Rises", "Django Unchained", "12 Years a Slave", "The Wolf of Wall Street",
                "Whiplash", "Nightcrawler", "Mad Max Fury Road", "La La Land", "Arrival",
                 "Logan", "Dunkirk", "Blade Runner 2049", "Green Book", "Roma",
                "Once Upon a Time in Hollywood", "Parasite", "1917", "Joker",
                "The Jungle Book", "Cinderella", "Sleeping Beauty", "Snow White and the Seven Dwarfs",
                "Pinocchio", "Bambi", "Dumbo", "Alice in Wonderland", "Peter Pan",
                "Lady and the Tramp", "101 Dalmatians", "Robin Hood",
                "The Fox and the Hound", "The Little Mermaid", "Beauty and the Beast",
                "Aladdin", "The Lion King", "Pocahontas", "The Hunchback of Notre Dame",
                "Hercules", "Mulan", "Tarzan", "Dune", "Top Gun Maverick", "The Batman", "Bullet Train",
                "Oppenheimer", "Barbie", "Creed", "Creed II", "Creed III", "The Super Mario Bros Movie",
                "Dune Part Two", "Mickey 17", "The Fall Guy", "drive",
                "Police Academy", "Ghostbusters", "Superbad", "The 40 Year Old Virgin", "White Chicks",
                "Rush Hour", "Rush Hour 2", "Rush Hour 3","Shanghai Noon", "Shanghai Knights", "Drunken Master",
                "The Exorcist", "The Shining", "Halloween", "Friday the 13th",
                "A Nightmare on Elm Street", "The Texas Chain Saw Massacre",
                "The Blair Witch Project", "The Ring", "The Grudge", "Paranormal Activity", "Insidious", "Sinister",
                "It", "Hereditary", "2001 A Space Odyssey", "Blade Runner", "The War of the Worlds",
                "Planet of the Apes", "Soylent Green", "Logan's Run", "RoboCop", "The Martian", "Ad Astra",
                "Demolition Man", "The Fifth Element", "I Robot", "Source Code", "Edge of Tomorrow",
                "The Last Boy Scout", "The Rock", "Face Off", "The Bourne Identity", "Rocky",
                "John wick", "John Wick Chapter 2", "John Wick Chapter 3",
                "Speed", "Twister", "Independence Day", "The Patriot", "The Foreigner",
                "Bad Boys", "Bad Boys II", "Bad Boys for Life", "Die Hard", "Die Hard 2", "Die Hard with a Vengeance",
                "The Green Mile", "The Usual Suspects", "The Sixth Sense", "Memento", "Argo", "The Shape of Water"
        );

        int totalSalvos = 0;
        int totalExistentes = 0;
        int totalErros = 0;

        System.out.println("\nComeçando a adicionar " + titulosFilmes.size() + " filmes!\n");

        for (int i = 0; i < titulosFilmes.size(); i++) {
            String titulo = titulosFilmes.get(i);

            try {
                Optional<Filme> existente = repositorioFilme.findByTituloContainingIgnoreCase(titulo);

                if (existente.isEmpty()) {
                    System.out.print("[" + (i + 1) + "/" + titulosFilmes.size() + "] Buscando: " + titulo + "... ");

                    // Corrigir formatação para busca (remover dois pontos e outros caracteres especiais)
                    String tituloBusca = titulo.replace(":", "").replace("!", "")
                            .replace("?", "").trim();
                    var json = consumo.obterDados(ENDERECO + tituloBusca
                            .replace(" ", "+") + API_KEY);
                    DadosFilme dados = conversor.obterDados(json, DadosFilme.class);

                    if (dados != null && dados.titulo() != null && !dados.titulo().isEmpty()) {
                        Filme filme = new Filme(dados);
                        repositorioFilme.save(filme);
                        System.out.println("✓ Filme salvo: " + titulo);
                        totalSalvos++;
                    } else {
                        System.out.println("✗ Filme não encontrado: " + titulo);
                        totalErros++;
                    }
                } else {
                    System.out.println("[" + (i + 1) + "/" + titulosFilmes.size() + "] " + titulo + " - Já Existente!");
                    totalExistentes++;
                }

                // Delay para não sobrecarregar a API
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    System.out.println("\nProcesso interrompido!");
                    Thread.currentThread().interrupt();
                    break;
                }

            } catch (Exception e) {
                System.out.println("Erro ao buscar: " + titulo);
                totalErros++;
            }
        }

        System.out.println("\nResumo dos Filmes adicionados: ");
        System.out.println("Filmes salvos: " + totalSalvos);
        System.out.println("Filmes já existentes: " + totalExistentes);
        System.out.println("Filmes não encontrados/erros: " + totalErros);
        System.out.println("Total processado: " + titulosFilmes.size());
        System.out.println("=".repeat(40));
    }

    private void adicionarListaSeries() throws InterruptedException {
        List<String> titulosSeries = Arrays.asList(
                //Lista de séries para teste
                "This Is Us", "Ozark", "Peaky Blinders", "Succession", "Yellowstone",
                "The Last of Us", "House of the Dragon", "Stranger Things", "The Walking Dead",
                "Money Heist", "Narcos", "The Witcher", "Vikings", "The Boys", "Tom Clancy Jack Ryan",
                "Reacher", "24", "Black Mirror", "The Expanse", "Westworld", "Dark",
                "Star Trek The Next Generation", "The X-Files", "Lost", "Fringe", "Orphan Black", "Silo",
                "The Big Bang Theory", "How I Met Your Mother", "Brooklyn Nine-Nine", "Modern Family",
                "The Good Place", "Seinfeld", "Parks and Recreation", "The Fresh Prince of Bel-Air",
                "Ted Lasso", "Fleabag"
        );

        int totalSalvos = 0;
        int totalExistentes = 0;
        int totalErros = 0;

        System.out.println("\nComeçando a adicionar " + titulosSeries.size() + " séries!\n");

        for (int i = 0; i < titulosSeries.size(); i++) {
            String titulo = titulosSeries.get(i);

            try {
                Optional<Serie> existente = repositorioSerie.findByTituloContainingIgnoreCase(titulo);

                if (existente.isEmpty()) {
                    System.out.print("[" + (i + 1) + "/" + titulosSeries.size() + "] Buscando: " + titulo + "... ");

                    // Corrigir formatação para busca (remover dois pontos e outros caracteres especiais)
                    String tituloBusca = titulo.replace(":", "").replace("!", "")
                            .replace("?", "").trim();
                    var json = consumo.obterDados(ENDERECO + tituloBusca
                            .replace(" ", "+") + API_KEY);
                    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

                    if (dados != null && dados.titulo() != null && !dados.titulo().isEmpty()) {
                        Serie serie = new Serie(dados);
                        repositorioSerie.save(serie);
                        System.out.println("✓ Série salva: " + titulo);
                        totalSalvos++;

                        // Buscar episódios automaticamente
                        buscarEpisodiosParaSerie(serie);
                    } else {
                        System.out.println("✗ Série não encontrada: " + titulo);
                        totalErros++;
                    }
                } else {
                    System.out.println("[" + (i + 1) + "/" + titulosSeries.size() + "] " + titulo + " - Já Existente!");
                    totalExistentes++;
                }

                // Delay para não sobrecarregar a API
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    System.out.println("\nProcesso interrompido!");
                    Thread.currentThread().interrupt();
                    break;
                }

            } catch (Exception e) {
                System.out.println("Erro ao buscar: " + titulo);
                totalErros++;
            }
        }

        System.out.println("\nResumo das Séries adicionadas: ");
        System.out.println("Séries salvas: " + totalSalvos);
        System.out.println("Séries já existentes: " + totalExistentes);
        System.out.println("Séries não encontradas/erros: " + totalErros);
        System.out.println("Total processado: " + titulosSeries.size());
        System.out.println("=".repeat(40));
    }

    private void buscarEpisodiosParaSerie(Serie serie) {
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + serie.getTitulo()
                    .replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

            // Delay entre temporadas
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        List<Episodio> episodios = temporadas.stream()
                .flatMap(d -> d.episodios().stream()
                        .map(e -> new Episodio(d.numero(), e)))
                .collect(Collectors.toList());

        serie.setEpisodios(episodios);
        repositorioSerie.save(serie);
        System.out.println("  Episódios salvos: " + episodios.size());
    }

    private void adicionarImagensFilmes() {
        List<Filme> filmes = repositorioFilme.findAll();

        List<Filme> filmesParaAtualizar = filmes.stream()
                .filter(f -> f.getImdbID() != null && !f.getImdbID().isEmpty())
                .filter(f -> f.getLogo() == null || f.getBackground() == null)
                .collect(Collectors.toList());

        if (filmesParaAtualizar.isEmpty()) {
            System.out.println("\nTodos os filmes já possuem logo e background!");
            return;
        }

        System.out.println("\nBuscando imagens no FanArt API para filmes");
        System.out.println("Total de filmes no banco: " + filmes.size());
        System.out.println("Filmes que precisam de imagens: " + filmesParaAtualizar.size());
        System.out.println("Filmes já completos: " + (filmes.size() - filmesParaAtualizar.size()));
        System.out.println("=".repeat(40));

        int logoEncontrado = 0;
        int bgEncontrado = 0;
        int processados = 0;

        for (Filme filme : filmesParaAtualizar) {
            processados++;
            boolean atualizou = false;

            System.out.print("[" + processados + "/" + filmesParaAtualizar.size() + "] " + filme.getTitulo());

            if (filme.getLogo() == null) {
                String logo = FanartApi.buscarLogo(filme.getImdbID());
                if (logo != null) {
                    filme.setLogo(logo);
                    logoEncontrado++;
                    atualizou = true;
                    System.out.print(" ✓ Logo");
                } else {
                    System.out.print(" ✗ Sem logo");
                }
            } else {
                System.out.print(" ✓ Logo já existe");
            }

            if (filme.getBackground() == null) {
                String bg = FanartApi.buscarBackground(filme.getImdbID());
                if (bg != null) {
                    filme.setBackground(bg);
                    bgEncontrado++;
                    atualizou = true;
                    System.out.print(" ✓ Background");
                } else {
                    System.out.print(" ✗ Sem background");
                }
            } else {
                System.out.print(" ✓ Background já existe");
            }

            if (atualizou) {
                repositorioFilme.save(filme);
                System.out.println(" → SALVO!");
            } else {
                System.out.println(" → Nenhuma imagem nova");
            }

            try { Thread.sleep(2000); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("\nProcesso interrompido!");
                break;
            }
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("Resumo da atualização de imagens: ");
        System.out.println("Filmes processados: " + processados);
        System.out.println("Logos encontrados: " + logoEncontrado);
        System.out.println("Backgrounds encontrados: " + bgEncontrado);
        System.out.println("Filmes que continuam sem imagens: " +
                (filmesParaAtualizar.size() - (filmesParaAtualizar.stream()
                        .filter(f -> f.getLogo() == null || f.getBackground() == null)
                        .count())));
        System.out.println("=".repeat(40));
    }

    private void adicionarImagensSeries() {
        List<Serie> series = repositorioSerie.findAll();

        List<Serie> seriesParaAtualizar = series.stream()
                .filter(s -> s.getImdbID() != null && !s.getImdbID().isEmpty())
                .filter(s -> s.getLogo() == null || s.getBackground() == null)
                .collect(Collectors.toList());

        if (seriesParaAtualizar.isEmpty()) {
            System.out.println("\nTodas as séries já possuem logo e background!");
            return;
        }

        System.out.println("\nBuscando imagens no FanArt API para séries");
        System.out.println("Total de séries no banco: " + series.size());
        System.out.println("Séries que precisam de imagens: " + seriesParaAtualizar.size());
        System.out.println("Séries já completas: " + (series.size() - seriesParaAtualizar.size()));
        System.out.println("----------------------------------------\n");

        int logoEncontrado = 0;
        int bgEncontrado = 0;
        int processados = 0;

        for (Serie serie : seriesParaAtualizar) {
            processados++;
            boolean atualizou = false;

            System.out.print("[" + processados + "/" + seriesParaAtualizar.size() + "] " + serie.getTitulo());

            if (serie.getLogo() == null) {
                String logo = FanartApi.buscarLogoSerie(serie.getImdbID());
                if (logo != null) {
                    serie.setLogo(logo);
                    logoEncontrado++;
                    atualizou = true;
                    System.out.print(" ✓ Logo");
                } else {
                    System.out.print(" ✗ Sem logo");
                }
            } else {
                System.out.print(" ✓ Logo já existe");
            }

            if (serie.getBackground() == null) {
                String bg = FanartApi.buscarBackgroundSerie(serie.getImdbID());
                if (bg != null) {
                    serie.setBackground(bg);
                    bgEncontrado++;
                    atualizou = true;
                    System.out.print(" ✓ Background");
                } else {
                    System.out.print(" ✗ Sem background");
                }
            } else {
                System.out.print(" ✓ Background já existe");
            }

            if (atualizou) {
                repositorioSerie.save(serie);
                System.out.println(" → SALVO!");
            } else {
                System.out.println(" → Nenhuma imagem nova");
            }

            // Delay menor (1 segundo)
            try { Thread.sleep(2000); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("\nProcesso interrompido!");
                break;
            }
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("Resumo da atualização de imagens: ");
        System.out.println("Séries processadas: " + processados);
        System.out.println("Logos encontrados: " + logoEncontrado);
        System.out.println("Backgrounds encontrados: " + bgEncontrado);
        System.out.println("Séries que continuam sem imagens: " +
                (seriesParaAtualizar.size() - (seriesParaAtualizar.stream()
                        .filter(s -> s.getLogo() == null || s.getBackground() == null)
                        .count())));
        System.out.println("=".repeat(40));
    }

    private void apagarFilmePorId() {
        listarFilmes();
        System.out.print("\nDigite o ID do filme para apagar: ");
        try {
            Long id = Long.parseLong(leitura.nextLine());

            Optional<Filme> filmeOpt = repositorioFilme.findById(id);

            if (filmeOpt.isPresent()) {
                Filme filme = filmeOpt.get();
                System.out.println("\nFilme encontrado: " + filme.getTitulo());
                System.out.print("Confirmar exclusão do filme " + filme.getTitulo() + "? (S/N): ");

                if (leitura.nextLine().toLowerCase().equals("s")) {
                    repositorioFilme.deleteById(id);
                    System.out.println("Filme apagado!");
                } else {
                    System.out.println("Operação cancelada!");
                }
            } else {
                System.out.println("Filme com ID " + id + " não encontrado!");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        }
    }

    private void apagarSeriePorId() {
        listarSeries();
        System.out.print("\nDigite o ID da série para apagar: ");
        try {
            Long id = Long.parseLong(leitura.nextLine());

            Optional<Serie> serieOpt = repositorioSerie.findById(id);

            if (serieOpt.isPresent()) {
                Serie serie = serieOpt.get();
                System.out.println("\nSérie encontrada: " + serie.getTitulo());
                System.out.print("Confirmar exclusão da série " + serie.getTitulo() + "? (S/N): ");

                if (leitura.nextLine().toLowerCase().equals("s")) {
                    repositorioSerie.deleteById(id);
                    System.out.println("Série apagada!");
                } else {
                    System.out.println("Operação cancelada!");
                }
            } else {
                System.out.println("Série com ID " + id + " não encontrada!");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        }
    }
}