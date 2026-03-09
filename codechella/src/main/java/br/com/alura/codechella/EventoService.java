package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Flux<EventoDto> obterTodos() {
        return eventoRepository.findAll()
                .map(EventoDto::toDto);
    }

    public Mono<EventoDto> obterPorId(Long id) {
        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"))) // Lança uma exceção se o evento não for encontrado
                .map(EventoDto::toDto); // Converte o Evento para EventoDto usando o método toDto
    }

    public Mono<EventoDto> cadastrar(EventoDto eventoDto) {
        return eventoRepository.save(eventoDto.toEntity())
                .map(EventoDto::toDto); // Converte o Evento salvo para EventoDto usando o método toDto
    }

    public Mono<Void> deletar(Long id) {
        return eventoRepository.findById(id)
                .flatMap(eventoRepository::delete);
        // O método flatMap é usado para encadear a operação de exclusão após encontrar o evento pelo ID.
        //  Se o evento for encontrado, ele será excluído usando o método delete do repositório.
    }

    public Flux<EventoDto> obterPorCategoria(String tipo) {
        Tipo tipoEvento = Tipo.valueOf(tipo.toUpperCase()); // Converte a string para o tipo enum Tipo
        return eventoRepository.findByTipo(tipoEvento)
                .map(EventoDto::toDto); // Converte os Eventos encontrados para EventoDto usando o método toDto

    }

    public Mono<String> obterTraducao(Long id, String idioma) {
        return eventoRepository.findById(id)
                .flatMap(e -> TraducaoDeTexto.obterTraducao(e.getDescricao(), idioma));
        // Obtém a descrição do evento e traduz para o idioma especificado usando o método obterTraducao da classe TraducaoDeTexto
    }
}
