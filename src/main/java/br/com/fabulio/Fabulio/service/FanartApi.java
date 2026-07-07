package br.com.fabulio.Fabulio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FanartApi {

    private static final String API_KEY = System.getenv("API_FANART");
    private static final String BASE_URL = "https://webservice.fanart.tv/v3";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String buscarLogo(String imdbId) {
        if (imdbId == null || imdbId.isEmpty() || API_KEY == null) {
            return null;
        }

        try {
            String url = String.format("%s/movies/%s?api_key=%s", BASE_URL, imdbId, API_KEY);
            String json = obterDados(url);

            if (json == null || json.isEmpty()) return null;

            JsonNode root = mapper.readTree(json);

            // Tenta hdmovielogo primeiro
            JsonNode logos = root.path("hdmovielogo");
            if (logos.isArray() && logos.size() > 0) {
                // Prioriza inglês
                for (JsonNode logo : logos) {
                    if ("en".equals(logo.path("lang").asText())) {
                        return logo.path("url").asText();
                    }
                }
                return logos.get(0).path("url").asText();
            }

            // Fallback para hdmovieclearart
            JsonNode cleararts = root.path("hdmovieclearart");
            if (cleararts.isArray() && cleararts.size() > 0) {
                for (JsonNode art : cleararts) {
                    if ("en".equals(art.path("lang").asText())) {
                        return art.path("url").asText();
                    }
                }
                return cleararts.get(0).path("url").asText();
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar logo: " + e.getMessage());
        }
        return null;
    }

    public static String buscarBackground(String imdbId) {
        if (imdbId == null || imdbId.isEmpty() || API_KEY == null) {
            return null;
        }

        try {
            String url = String.format("%s/movies/%s?api_key=%s", BASE_URL, imdbId, API_KEY);
            String json = obterDados(url);

            if (json == null || json.isEmpty()) return null;

            JsonNode root = mapper.readTree(json);

            JsonNode backgrounds = root.path("moviebackground");
            if (backgrounds.isArray() && backgrounds.size() > 0) {
                return backgrounds.get(0).path("url").asText();
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar background: " + e.getMessage());
        }
        return null;
    }

    public static String buscarLogoSerie(String imdbId) {
        if (imdbId == null || imdbId.isEmpty() || API_KEY == null) {
            return null;
        }

        try {
            // Converte IMDB ID para TVDB ID usando TMDb
            String tvdbId = TmdbApi.buscarTvdbIdPorImdbId(imdbId);
            if (tvdbId == null) {
                System.out.println("  Não foi possível obter TVDB ID para: " + imdbId);
                return null;
            }

            // Agora usa o TVDB ID com a Fanart (igual aos filmes)
            String url = String.format("%s/tv/%s?api_key=%s", BASE_URL, tvdbId, API_KEY);
            String json = obterDados(url);

            if (json == null || json.isEmpty()) return null;

            JsonNode root = mapper.readTree(json);

            // Tenta clearlogo primeiro (logo transparente)
            JsonNode logos = root.path("clearlogo");
            if (logos.isArray() && logos.size() > 0) {
                for (JsonNode logo : logos) {
                    if ("en".equals(logo.path("lang").asText())) {
                        return logo.path("url").asText();
                    }
                }
                return logos.get(0).path("url").asText();
            }

            // Fallback para hdtvlogo
            JsonNode hdtvlogos = root.path("hdtvlogo");
            if (hdtvlogos.isArray() && hdtvlogos.size() > 0) {
                return hdtvlogos.get(0).path("url").asText();
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar logo da série: " + e.getMessage());
        }
        return null;
    }

    public static String buscarBackgroundSerie(String imdbId) {
        if (imdbId == null || imdbId.isEmpty() || API_KEY == null) {
            return null;
        }

        try {
            // Converte IMDB ID para TVDB ID usando TMDb
            String tvdbId = TmdbApi.buscarTvdbIdPorImdbId(imdbId);
            if (tvdbId == null) {
                System.out.println("  Não foi possível obter TVDB ID para: " + imdbId);
                return null;
            }

            // Agora usa o TVDB ID com a Fanart
            String url = String.format("%s/tv/%s?api_key=%s", BASE_URL, tvdbId, API_KEY);
            String json = obterDados(url);

            if (json == null || json.isEmpty()) return null;

            JsonNode root = mapper.readTree(json);

            // Background da série
            JsonNode backgrounds = root.path("showbackground");
            if (backgrounds.isArray() && backgrounds.size() > 0) {
                return backgrounds.get(0).path("url").asText();
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar background da série: " + e.getMessage());
        }
        return null;
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
                System.out.println("Erro Fanart - Status: " + response.statusCode());
                return null;
            }

        } catch (Exception e) {
            System.out.println("Erro na requisição: " + e.getMessage());
            return null;
        }
    }
}