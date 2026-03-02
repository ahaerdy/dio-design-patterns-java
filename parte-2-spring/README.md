# Parte 2 — Padrões de Projeto com Spring Boot

![Java](https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=for-the-badge&logo=springboot&logoColor=white)
![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=for-the-badge)
![DIO](https://img.shields.io/badge/DIO-Bootcamp-purple?style=for-the-badge)

> Os mesmos padrões **Singleton**, **Strategy** e **Facade** da Parte 1 — agora aplicados automaticamente pelo Spring Boot, com muito menos código e muito mais poder.

---

## 🧠 O que muda em relação à Parte 1?

Na Parte 1, você implementou os padrões **na mão**: construtor privado, interfaces, classes de orquestração. Tudo escrito explicitamente.

Na Parte 2, o Spring Boot **aplica esses mesmos padrões automaticamente** através de anotações. Você declara a intenção — o framework cuida da implementação.

| Padrão | Java Puro (Parte 1) | Spring Boot (Parte 2) |
|---|---|---|
| **Singleton** | Construtor privado + `getInstancia()` | `@Service` — o Spring garante uma única instância |
| **Strategy** | Interface + classes concretas | `@Repository` — o Spring Data JPA gera a implementação |
| **Facade** | Classe que orquestra subsistemas manualmente | `@Service` + OpenFeign que consome a API ViaCEP |

---

## 🏗️ Arquitetura da Aplicação

A aplicação é uma **API REST** completa que gerencia clientes. Ao cadastrar um cliente informando apenas o CEP, o sistema consulta automaticamente a API pública **ViaCEP** e preenche o endereço completo.

```
[Cliente HTTP]
      │
      │  POST /clientes {"nome": "...", "endereco": {"cep": "..."}}
      ▼
[ClienteRestController]     ← recebe a requisição HTTP
      │
      │  delega para
      ▼
[ClienteServiceImpl]        ← orquestra a lógica (Facade + Singleton)
      │
      ├──► [ClienteRepository]   ← persiste no banco H2 (Strategy)
      ├──► [EnderecoRepository]  ← verifica se CEP já existe (Strategy)
      └──► [ViaCepService]       ← consulta API externa se necessário (Facade)
                │
                └──► https://viacep.com.br/ws/{cep}/json/
```

---

## 📁 Estrutura do Projeto

```
gof-spring/
├── src/main/java/one/digitalinnovation/gofspring/
│   ├── GofSpringApplication.java         ← ponto de entrada
│   ├── controller/
│   │   └── ClienteRestController.java    ← endpoints HTTP
│   ├── model/
│   │   ├── Cliente.java                  ← entidade Cliente
│   │   └── Endereco.java                 ← entidade Endereco
│   ├── repository/
│   │   ├── ClienteRepository.java        ← padrão Strategy/Repository
│   │   └── EnderecoRepository.java       ← padrão Strategy/Repository
│   └── service/
│       ├── ClienteService.java           ← contrato do serviço
│       ├── impl/
│       │   └── ClienteServiceImpl.java   ← Singleton + Facade
│       └── viaCep/
│           └── ViaCepService.java        ← cliente HTTP (OpenFeign)
└── src/main/resources/
    └── application.properties            ← configurações
```

> **Por que o pacote `one.digitalinnovation.gofspring`?**
> Segue a mesma convenção de domínio reverso da Parte 1. O sufixo `spring` diferencia este módulo do projeto Java puro.

---

## 🔵 Padrão 1 — Singleton (via `@Service`)

### O problema que ele resolve

O mesmo da Parte 1: garantir que exista apenas uma instância de uma classe em toda a aplicação. Aqui, o `ClienteServiceImpl` não pode ter múltiplas instâncias — cada uma teria seu próprio estado, o que causaria inconsistências.

### Analogia do mundo real

> Pense no **gerente de um banco**. Há apenas um gerente responsável por uma agência. Todos os caixas (controllers) consultam o mesmo gerente (service) para tomar decisões. Se cada caixa tivesse seu próprio gerente independente, as decisões seriam inconsistentes.

### Como o Spring implementa automaticamente

```java
@Service  // ← esta anotação é o Singleton
public class ClienteServiceImpl implements ClienteService {
    // ...
}
```

Apenas uma anotação substitui todo o mecanismo que você escreveu na Parte 1:

```
Java Puro                          Spring Boot
─────────────────────────────      ──────────────
private static instancia;          @Service
private Construtor() {}
public static getInstancia() {}
```

### Diagrama

```
┌─────────────────────────────────────────┐
│          ClienteServiceImpl             │
├─────────────────────────────────────────┤
│  @Service ← Spring garante 1 instância  │
├─────────────────────────────────────────┤
│  + buscarTodos()                        │
│  + buscarPorId(id)                      │
│  + inserir(cliente)                     │
│  + atualizar(id, cliente)               │
│  + deletar(id)                          │
└─────────────────────────────────────────┘
         ▲           ▲           ▲
         │           │           │
  @Autowired    @Autowired   @Autowired
         │           │           │
  ClienteRepo  EnderecoRepo  ViaCepService
```

### O código

```java
/**
 * @Service diz ao Spring:
 * 1. Crie EXATAMENTE UMA instância desta classe
 * 2. Registre-a no contexto da aplicação
 * 3. Injete-a em qualquer lugar que precisar dela
 *
 * Sem @Service, o Spring não gerenciaria este objeto
 * e você precisaria instanciá-lo manualmente.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    // @Autowired: o Spring injeta automaticamente as dependências.
    // Você não escreve "new ClienteRepository()" em lugar nenhum.
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepService viaCepService;

    // ... implementações dos métodos
}
```

---

### Por que não criar as instâncias manualmente?

| Criar manualmente (`new`) | `@Service` + `@Autowired` |
|---|---|
| Cada classe precisa conhecer e instanciar suas dependências | O Spring injeta automaticamente onde necessário |
| Difícil de testar — dependências reais sempre presentes | Fácil de testar — basta injetar versões simuladas (mocks) |
| Risco de múltiplas instâncias com estados diferentes | Uma única instância garantida pelo container do Spring |

---

## 🟢 Padrão 2 — Strategy/Repository (via `@Repository` + Spring Data JPA)

### O problema que ele resolve

Na Parte 1, o Strategy permitia trocar algoritmos sem alterar o código que os usa. Aqui, o problema é parecido: como acessar o banco de dados sem acoplar o código de negócio aos detalhes de SQL e JDBC?

### Analogia do mundo real

> Pense em um **assistente pessoal**. Você diz "me traga os relatórios do mês" — e não precisa saber se ele vai buscar no armário, no computador ou na nuvem. O assistente conhece os detalhes; você só define o que quer. O Spring Data JPA é esse assistente: você declara o que quer e ele descobre como fazer.

### Como o Spring implementa automaticamente

```java
@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    // Vazio — e ainda assim funciona!
}
```

Ao estender `CrudRepository`, o Spring Data JPA **gera automaticamente em tempo de execução** toda a implementação dos métodos:

| Método gerado | O que faz |
|---|---|
| `findAll()` | `SELECT * FROM cliente` |
| `findById(id)` | `SELECT * FROM cliente WHERE id = ?` |
| `save(cliente)` | `INSERT` ou `UPDATE` automaticamente |
| `deleteById(id)` | `DELETE FROM cliente WHERE id = ?` |
| `existsById(id)` | `SELECT COUNT(*) FROM cliente WHERE id = ?` |

Você não escreve uma linha de SQL. Não cria nenhuma classe de implementação. A interface sozinha é suficiente.

### Diagrama

```
         «interface»
        CrudRepository
       ┌───────────────┐
       │ findAll()     │  ← fornecida pelo Spring Data
       │ findById()    │
       │ save()        │
       │ deleteById()  │
       └───────┬───────┘
               │  estendida por
    ┌──────────┴──────────┐
    ▼                     ▼
ClienteRepository    EnderecoRepository
(Cliente, Long)      (Endereco, String)
   @Repository           @Repository
```

### O código

```java
/**
 * Padrão Strategy aplicado pelo Spring Data JPA.
 *
 * CrudRepository<T, ID> recebe dois parâmetros:
 * T   = o tipo da entidade que será persistida (Cliente)
 * ID  = o tipo da chave primária (Long, gerada automaticamente)
 *
 * O Spring gera a implementação completa em tempo de execução.
 * Nenhuma classe concreta precisa ser criada.
 */
@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}

/**
 * Para Endereco, a chave primária é String (o próprio CEP),
 * pois o CEP já identifica unicamente um endereço.
 */
@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, String> {
}
```

### As entidades mapeadas

```java
@Entity  // ← mapeia esta classe como tabela no banco H2
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // ← ID gerado automaticamente
    private Long id;

    private String nome;

    @ManyToOne  // ← muitos clientes podem ter o mesmo CEP
    private Endereco endereco;

    // getters e setters...
}

@Entity
public class Endereco {

    @Id  // ← o CEP é a chave primária (já é único por natureza)
    private String cep;

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    // demais campos do ViaCEP...
}
```

---

### Por que não usar JDBC puro ou SQL manual?

| SQL/JDBC manual | Spring Data JPA + `@Repository` |
|---|---|
| Escrever `SELECT`, `INSERT`, `UPDATE`, `DELETE` para cada operação | Métodos prontos — zero SQL para operações básicas |
| Gerenciar conexões, `PreparedStatement`, `ResultSet` | O Spring gerencia tudo automaticamente |
| Trocar de banco exige reescrever queries | Trocar de banco (H2, MySQL, PostgreSQL) exige apenas mudar uma linha no `application.properties` |

---

## 🟠 Padrão 3 — Facade (via `@Service` + OpenFeign)

### O problema que ele resolve

O sistema precisa consultar a API externa ViaCEP para buscar endereços. Sem a Facade, o controller precisaria conhecer os detalhes dessa integração — a URL, o formato JSON, o tratamento de erros. Com a Facade, tudo isso fica encapsulado no `ClienteServiceImpl`.

### Analogia do mundo real

> Pense em um **balcão de atendimento de hotel**. Você chega e diz "quero um quarto para amanhã". O atendente (Facade) verifica disponibilidade com o sistema de reservas, confirma com a limpeza, registra no sistema financeiro. Você não interage com nenhum desses subsistemas diretamente — só com o balcão.

### Diagrama

```
[Controller]
     │
     │  inserir(cliente)
     ▼
[ClienteServiceImpl]  ← a Facade
     │
     ├─ verifica se CEP existe no banco (EnderecoRepository)
     │       │
     │       └─ NÃO existe → consulta API externa
     │                            │
     │                            ▼
     │                      [ViaCepService]
     │                      GET viacep.com.br
     │                      /{cep}/json/
     │
     ├─ salva o endereço retornado (EnderecoRepository)
     └─ associa ao cliente e salva (ClienteRepository)
```

### Parte 1: o cliente Feign — a Facade da integração HTTP

```java
/**
 * @FeignClient: diz ao Spring "gere um cliente HTTP para esta URL".
 *
 * O OpenFeign cria automaticamente toda a lógica de:
 * - abrir conexão HTTP
 * - montar a URL com o CEP
 * - fazer o GET
 * - converter o JSON de resposta em objeto Endereco
 * - fechar a conexão
 *
 * Sem OpenFeign, você precisaria de pelo menos 20 linhas de código
 * com HttpURLConnection, InputStreamReader, JsonParser, etc.
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Endereco consultarCep(@PathVariable("cep") String cep);
}
```

### Parte 2: o método que orquestra tudo

```java
/**
 * Este método privado é o coração da Facade.
 * O controller não sabe que existe ViaCEP, nem dois repositórios.
 * Ele chama apenas "inserir(cliente)" — e esta lógica cuida do resto.
 */
private void salvarClienteComCep(Cliente cliente) {
    String cep = cliente.getEndereco().getCep();

    // Passo 1: tenta encontrar o endereço no banco local
    // (evita consultar o ViaCEP toda vez para o mesmo CEP)
    Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {

        // Passo 2: se não encontrou localmente, consulta a API externa
        Endereco novoEndereco = viaCepService.consultarCep(cep);

        // Passo 3: salva no banco para consultas futuras (cache)
        enderecoRepository.save(novoEndereco);
        return novoEndereco;
    });

    // Passo 4: associa o endereço completo ao cliente e salva
    cliente.setEndereco(endereco);
    clienteRepository.save(cliente);
}
```

---

### Por que não chamar o ViaCEP diretamente no controller?

| Lógica no controller | Facade no service |
|---|---|
| Controller acoplado à API externa — qualquer mudança no ViaCEP afeta o controller | Controller não sabe que o ViaCEP existe |
| Lógica de cache (verificar banco antes de chamar API) espalhada | Cache encapsulado no método `salvarClienteComCep` |
| Impossível reusar em outros controllers | Qualquer controller pode chamar `inserir()` e ter o mesmo comportamento |

---

## 🌐 Endpoints da API REST

A aplicação expõe os seguintes endpoints na porta `8083`:

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/clientes` | Retorna todos os clientes |
| `GET` | `/clientes/{id}` | Retorna um cliente por ID |
| `POST` | `/clientes` | Insere novo cliente (consulta ViaCEP) |
| `PUT` | `/clientes/{id}` | Atualiza cliente (consulta ViaCEP se CEP mudou) |
| `DELETE` | `/clientes/{id}` | Remove cliente por ID |

### Exemplo de requisição e resposta

**Requisição (POST):**
```json
{
  "nome": "Venilton",
  "endereco": {
    "cep": "01310-100"
  }
}
```

**Resposta (200 OK):** o endereço é preenchido automaticamente pelo ViaCEP
```json
{
  "id": 1,
  "nome": "Venilton",
  "endereco": {
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "complemento": "de 612 a 1510 - lado par",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "ddd": "11"
  }
}
```

---

## ▶️ Como Executar

### Pré-requisitos

- JDK 21 ou superior
- Maven (ou usar o `mvnw` incluso no projeto)
- IntelliJ IDEA
- Conexão com internet (para consultar o ViaCEP)

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/ahaerdy/dio-design-patterns-java.git
   ```

2. Abra o IntelliJ IDEA → **File → Open** → selecione a pasta `parte-2-spring`

3. Aguarde o Maven baixar as dependências (barra de progresso no canto inferior direito)

4. Abra `GofSpringApplication.java` e clique em **▶ Run**

5. Aguarde no console:
   ```
   Tomcat started on port 8083 (http)
   Started GofSpringApplication in X seconds
   ```

### Testando com curl

```bash
# Inserir cliente (consulta ViaCEP automaticamente)
curl -s -X POST http://localhost:8083/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Venilton", "endereco": {"cep": "01310-100"}}' | python3 -m json.tool

# Listar todos
curl -s http://localhost:8083/clientes | python3 -m json.tool

# Buscar por ID
curl -s http://localhost:8083/clientes/1 | python3 -m json.tool

# Atualizar CEP
curl -s -X PUT http://localhost:8083/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome": "Venilton", "endereco": {"cep": "14835-000"}}' | python3 -m json.tool

# Deletar
curl -s -X DELETE http://localhost:8083/clientes/1
```

> 💡 **Nota sobre o banco de dados:** o projeto usa H2, um banco em memória. Os dados são perdidos ao reiniciar a aplicação. Isso é intencional para fins de demonstração — em produção, bastaria trocar por MySQL ou PostgreSQL no `application.properties`.

---

## 💡 Resumo dos 3 Padrões no Spring

| Padrão | Categoria | Como o Spring aplica | Anotação principal |
|---|---|---|---|
| **Singleton** | Criacional | Uma única instância gerenciada pelo container | `@Service` |
| **Strategy/Repository** | Comportamental + Estrutural | Implementação gerada automaticamente pelo Spring Data | `@Repository` |
| **Facade** | Estrutural | Orquestração de subsistemas + cliente HTTP gerado pelo Feign | `@Service` + `@FeignClient` |

---

## 🔗 Referências

- [Documentação oficial do Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação do Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Documentação do Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [API pública ViaCEP](https://viacep.com.br)
- [DIO — repositório original do laboratório Spring](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)

---

> ⬅️ **Etapa anterior:** [Parte 1 — Padrões de Projeto com Java Puro](../parte-1-java-puro/README.md)
>
> 🏠 **Visão geral e comparação das duas partes:** [README principal](../README.md)