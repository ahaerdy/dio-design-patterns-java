package one.digitalinnovation.gofspring.service.viaCep;

import one.digitalinnovation.gofspring.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Padrão FACADE aplicado pelo Spring com OpenFeign.
 *
 * Esta interface, sozinha, é suficiente para consumir a API externa
 * do ViaCEP. O OpenFeign gera toda a implementação HTTP automaticamente.
 *
 * @FeignClient: diz ao Spring "este é um cliente HTTP para a URL informada".
 * @GetMapping: mapeia o método para a rota GET /ws/{cep}/json/
 * @PathVariable: substitui {cep} na URL pelo valor passado como parâmetro.
 *
 * Sem OpenFeign, você precisaria criar manualmente uma conexão HTTP,
 * montar a URL, fazer o parse do JSON... O Feign faz tudo isso por você.
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Endereco consultarCep(@PathVariable("cep") String cep);
}