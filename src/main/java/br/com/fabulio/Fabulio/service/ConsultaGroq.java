package br.com.fabulio.Fabulio.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaGroq {

    private static final String API_KEY = System.getenv("GROQ_APIKEY");

    private static final String URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Map<String, String> cache = new HashMap<>();

    public static String obterTraducao(String texto) {

        if (texto == null || texto.isBlank()) return texto;

        if (jaEstaEmPortugues(texto)) return texto;

        if (cache.containsKey(texto)) {
            return cache.get(texto);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", "Traduza para português do Brasil de forma fiel, " +
                                            "Responda APENAS com a tradução, sem títulos, sem explicações, sem formatação, " +
                                            "Preserve nomes próprios e termos técnicos:\n" + texto
                            )
                    ),
                    "temperature", 0.2,
                    "max_tokens", 200
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);

            Map resposta = response.getBody();

            if (resposta == null) return texto;

            List choices = (List) resposta.get("choices");
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");

            String traducao = (String) message.get("content");

            cache.put(texto, traducao);

            return traducao;

        } catch (Exception e) {
            System.out.println("Erro ao traduzir: " + e.getMessage());
            return texto;
        }
    }

    private static boolean jaEstaEmPortugues(String texto) {
        return texto.matches(".*[ãáàâéêíóôõúç].*");
    }
}