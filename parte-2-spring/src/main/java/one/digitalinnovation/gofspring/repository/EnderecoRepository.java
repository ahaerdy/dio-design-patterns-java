package one.digitalinnovation.gofspring.repository;

import one.digitalinnovation.gofspring.model.Endereco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para a entidade Endereco.
 * A chave primária é String (o CEP).
 */
@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, String> {
}