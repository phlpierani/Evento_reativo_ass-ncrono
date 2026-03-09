package br.com.alura.codechella;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TraducaoDeTexto {

    public static Mono<String> obterTraducao(String texto, String idioma){
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api-free.deepl.com/v2/translate")
                .build();


        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        // O MultiValueMap é uma estrutura de dados que permite armazenar múltiplos valores para uma mesma chave,
        // o que é útil para representar os parâmetros de uma requisição HTTP, onde uma chave pode ter vários valores associados.

        req.add("text", texto);
        req.add("target_lang", idioma);

        return webClient.post()
                .header("Authorization", "DeepL-Auth-Key 12f659d3-1ae3-45d8-a58a-2e4745024723:fx")
                .body(BodyInserters.fromFormData(req)) //
                .retrieve()
                .bodyToMono(Traducao.class)
                .map(Traducao::getTexto);

    }
}
