package one.digitalinnovation.gofspring.controller;

import one.digitalinnovation.gofspring.model.Cliente;
import one.digitalinnovation.gofspring.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST: expõe as operações de Cliente como endpoints HTTP.
 *
 * @RestController: combina @Controller + @ResponseBody.
 * Diz ao Spring que esta classe responde requisições HTTP e retorna
 * JSON automaticamente — sem configuração manual.
 *
 * @RequestMapping: define a rota base "/clientes" para todos os métodos.
 *
 * O controller não tem nenhuma lógica de negócio — apenas recebe
 * requisições HTTP e delega para o ClienteService (Facade).
 */
@RestController
@RequestMapping("clientes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Iterable<Cliente>> buscarTodos() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> inserir(@RequestBody Cliente cliente) {
        clienteService.inserir(cliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
                                             @RequestBody Cliente cliente) {
        clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.ok().build();
    }
}