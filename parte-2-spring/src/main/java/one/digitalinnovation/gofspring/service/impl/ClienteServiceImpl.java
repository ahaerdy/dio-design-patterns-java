package one.digitalinnovation.gofspring.service.impl;

import one.digitalinnovation.gofspring.model.Cliente;
import one.digitalinnovation.gofspring.model.Endereco;
import one.digitalinnovation.gofspring.repository.ClienteRepository;
import one.digitalinnovation.gofspring.repository.EnderecoRepository;
import one.digitalinnovation.gofspring.service.ClienteService;
import one.digitalinnovation.gofspring.service.viaCep.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação de ClienteService.
 *
 * Aqui os três padrões se encontram:
 *
 * SINGLETON: @Service diz ao Spring para criar APENAS UMA instância
 * desta classe em toda a aplicação. Não há construtor privado,
 * sem getInstancia() — o Spring gerencia isso automaticamente.
 *
 * STRATEGY: @Autowired injeta as implementações de ClienteRepository
 * e EnderecoRepository geradas automaticamente pelo Spring Data JPA.
 *
 * FACADE: o método salvarClienteComCep() orquestra os dois repositórios
 * e o serviço ViaCEP, escondendo toda essa complexidade do controller.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    // @Autowired: o Spring injeta automaticamente as instâncias.
    // Você não precisa escrever "new ClienteRepository()" em nenhum lugar.
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        if (clienteRepository.existsById(id)) {
            cliente.setId(id);
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * Método privado que implementa a lógica de Facade:
     * 1. Verifica se o endereço (CEP) já está salvo no banco local
     * 2. Se não estiver, consulta a API externa ViaCEP
     * 3. Salva o endereço no banco
     * 4. Associa o endereço ao cliente e salva o cliente
     */
    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();

        // Primeiro tenta buscar o endereço no banco local (evita chamada desnecessária à API)
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Se não encontrou localmente, consulta a API ViaCEP
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            // Salva no banco para consultas futuras
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });

        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}