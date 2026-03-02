package one.digitalinnovation.gofspring.service;

import one.digitalinnovation.gofspring.model.Cliente;

/**
 * Define o contrato das operações disponíveis para Cliente.
 * É o mesmo conceito de interface que você viu na Parte 1 com o Strategy:
 * separar O QUE fazer de COMO fazer.
 */
public interface ClienteService {

    Iterable<Cliente> buscarTodos();

    Cliente buscarPorId(Long id);

    void inserir(Cliente cliente);

    void atualizar(Long id, Cliente cliente);

    void deletar(Long id);
}