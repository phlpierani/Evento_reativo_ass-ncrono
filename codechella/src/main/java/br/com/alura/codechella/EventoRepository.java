package br.com.alura.codechella;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {
    // O ReactiveCrudRepository é uma interface do Spring Data que fornece métodos reativos para operações CRUD em entidades.
    // como o R2DBC, e permite que você execute operações de forma assíncrona e não bloqueante.

    Flux<Evento> findByTipo(Tipo tipo);

}
