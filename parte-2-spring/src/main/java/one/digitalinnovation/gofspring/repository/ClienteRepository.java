package one.digitalinnovation.gofspring.repository;

import one.digitalinnovation.gofspring.model.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Padrão STRATEGY + REPOSITORY aplicado pelo Spring.
 *
 * Ao estender CrudRepository, o Spring Data JPA gera automaticamente
 * em tempo de execução toda a implementação dos métodos:
 * findAll(), findById(), save(), deleteById()...
 *
 * Você não escreve nenhum SQL. Não cria nenhuma classe de implementação.
 * A interface sozinha é suficiente.
 */
@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}