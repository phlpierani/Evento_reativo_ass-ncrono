package br.com.alura.codechella;

import java.time.LocalDate;

public record EventoDto(Long id,
                        Tipo tipo,
                        String nome,
                        LocalDate data,
                        String descricao) {

    public static EventoDto toDto(Evento evento) {
        return new EventoDto(evento.getId(), evento.getTipo(),
                evento.getNome(), evento.getData(), evento.getDescricao());
    }
    // A record é uma classe imutável e concisa que foi introduzida no Java 14.
    // Ela é usada para representar dados de forma simples, sem a necessidade de escrever código boilerplate como getters, setters, equals, hashCode e toString.
    // O método toDto é um método estático que converte um objeto do tipo Evento em um objeto do tipo EventoDto, facilitando a transferência de dados entre camadas da aplicação.

    public Evento toEntity() {
        Evento evento = new Evento();
        evento.setId(this.id);
        evento.setTipoEvento(this.tipo);
        evento.setNome(this.nome);
        evento.setData(this.data);
        evento.setDescricao(this.descricao);
        return evento;
        // O método toEntity é um método de instância que converte um objeto do tipo EventoDto em um objeto do tipo Evento,
        //  permitindo a criação de entidades a partir dos dados transferidos.
    }
}
