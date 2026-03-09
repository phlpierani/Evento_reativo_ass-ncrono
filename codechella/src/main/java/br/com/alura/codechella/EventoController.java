package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    private final Sinks.Many<EventoDto> eventoSink;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
        // O Sinks.Many é uma classe do Project Reactor que permite criar um fluxo de eventos reativo.
        // Ele é configurado para ser multicast, o que significa que os eventos emitidos serão enviados para todos os assinantes,
        // e onBackpressureBuffer, o que significa que se os assinantes não conseguirem acompanhar a taxa de emissão dos eventos,
        // eles serão armazenados em um buffer até que possam processá-los.
    }

    @GetMapping
    //(produces = MediaType.TEXT_EVENT_STREAM_VALUE) // Configura o endpoint para retornar um stream de eventos
    public Flux<EventoDto> obterTodos() {
        return eventoService.obterTodos();
    }

    @GetMapping("/{id}") // Configura o endpoint para obter um evento por ID
    public Mono<EventoDto> obterPorId(@PathVariable Long id) {
        return eventoService.obterPorId(id);
    }

    @GetMapping("/{id}/traduzir/{idioma}") // Configura o endpoint para obter um evento por ID e traduzir para um idioma específico
    public Mono<String> obterTraducao(@PathVariable Long id, @PathVariable String idioma) {
        return eventoService.obterTraducao(id, idioma);
    }

    @GetMapping(value = "/categoria/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // Configura o endpoint para obter eventos por categoria e retornar um stream de eventos
    public Flux<EventoDto> obterPorCategoria(@PathVariable String tipo) {
        return Flux.merge(eventoService.obterPorCategoria(tipo), eventoSink.asFlux()) // Combina o fluxo de eventos obtidos do serviço com o fluxo de eventos emitidos pelo Sinks.Many
                .delayElements(Duration.ofSeconds(4)); // Adiciona um atraso de 4 segundos entre cada evento emitido no stream
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EventoDto> cadastrar(@RequestBody EventoDto dto) {
        return eventoService.cadastrar(dto)
                .doOnSuccess(e -> eventoSink.tryEmitNext(e));
        // O método doOnSuccess é usado para executar uma ação quando a operação de cadastro for bem-sucedida.
        // ele emite o evento DTO para o Sinks.Many usando o método tryEmitNext, permitindo que os assinantes do fluxo de eventos recebam o novo evento cadastrado.
    }

    @DeleteMapping("{id}")
    public Mono<Void> deletar(@PathVariable Long id) {
        return eventoService.deletar(id);
    }
}
