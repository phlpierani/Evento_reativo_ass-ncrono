package br.com.alura.codechella;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Configura o ambiente de teste para usar uma porta aleatória
@AutoConfigureWebTestClient
class CodechellaApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	// O WebTestClient é uma classe do Spring Framework que permite realizar testes de integração em aplicações web reativas.
	// Ele é usado para enviar requisições HTTP e verificar as respostas de forma fluida e intuitiva.

	@Test
	void cadastroNovoEvento() {
		EventoDto dto = new EventoDto(null, Tipo.SHOW, "linkin park",
				LocalDate.parse("2024-10-10"), "show de rock, da melhor banda");

		webTestClient.post().uri("/eventos").bodyValue(dto)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(EventoDto.class)
				.value(response -> {
					assertNotNull(response.id());
					assertEquals(dto.tipo(),response.tipo());
					assertEquals(dto.nome(),response.nome());
					assertEquals(dto.data(),response.data());
					assertEquals(dto.descricao(),response.descricao());
				});
	}

	@Test
	void buscarEvento() {
		EventoDto dto = new EventoDto(13L, Tipo.SHOW, "The Weeknd",
				LocalDate.parse("2025-11-02"), "Um show eletrizante ao ar livre com muitos efeitos especiais.");

		webTestClient.get().uri("/eventos")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(EventoDto.class)
				.value(response -> {
					EventoDto eventoReponse = response.get(12);
					assertEquals(dto.id(),eventoReponse.id());
					assertEquals(dto.tipo(),eventoReponse.tipo());
					assertEquals(dto.nome(),eventoReponse.nome());
					assertEquals(dto.data(),eventoReponse.data());
					assertEquals(dto.descricao(),eventoReponse.descricao());
				});
	}
}
