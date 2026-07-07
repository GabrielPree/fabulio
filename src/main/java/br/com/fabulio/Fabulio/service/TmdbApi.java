package br.com.fabulio.Fabulio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbApi {

    private static final String API_KEY = System.getenv("API_TMDB");
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();


    //Converte IMDB ID para TVDB ID (necessário para o Fanart)
    public static String buscarTvdbIdPorImdbId(String imdbId) {
        if (imdbId == null || imdbId.isEmpty() || API_KEY == null) {
            System.out.println("  IMDB ID vazio ou API_KEY não configurada");
            return null;
        }

        try {
            // chamada 1: encontrar o TMDb ID
            String findUrl = String.format("%s/find/%s?api_key=%s&external_source=imdb_id",
                    BASE_URL, imdbId, API_KEY);
            String jsonFind = obterDados(findUrl);

            if (jsonFind == null || jsonFind.isEmpty()) {
                System.out.println("  Resposta vazia do TMDb para IMDB: " + imdbId);
                return null;
            }

            JsonNode rootFind = mapper.readTree(jsonFind);
            JsonNode tvResults = rootFind.path("tv_results");

            if (tvResults.isEmpty() || !tvResults.isArray() || tvResults.size() == 0) {
                System.out.println("  Nenhuma série encontrada no TMDb para IMDB: " + imdbId);
                return null;
            }

            String tmdbId = tvResults.get(0).path("id").asText();
            if (tmdbId == null || tmdbId.isEmpty()) {
                System.out.println("  TMDb ID não encontrado");
                return null;
            }

            // chamada 2: buscar IDs externos (incluindo TVDB)
            String externalUrl = String.format("%s/tv/%s/external_ids?api_key=%s",
                    BASE_URL, tmdbId, API_KEY);
            String jsonExternal = obterDados(externalUrl);

            if (jsonExternal == null || jsonExternal.isEmpty()) {
                System.out.println("  Resposta vazia ao buscar external IDs");
                return null;
            }

            JsonNode rootExternal = mapper.readTree(jsonExternal);
            String tvdbId = rootExternal.path("tvdb_id").asText();

            if (tvdbId != null && !tvdbId.isEmpty() && !"null".equals(tvdbId)) {
                System.out.println("  TVDB ID encontrado: " + tvdbId + " (para " + imdbId + ")");
                return tvdbId;
            } else {
                System.out.println("  TVDB ID não disponível para esta série");
                return null;
            }

        } catch (Exception e) {
            System.out.println("  Erro ao buscar TVDB ID: " + e.getMessage());
            return null;
        }
    }

    private static String obterDados(String endereco) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 404) {
                return null;
            } else {
                System.out.println("  Erro TMDb - Status: " + response.statusCode());
                return null;
            }

        } catch (Exception e) {
            System.out.println("  Erro na requisição TMDb: " + e.getMessage());
            return null;
        }
    }
}

