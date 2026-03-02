# Explorando Padrões de Projeto na Prática com Java

![Java](https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=for-the-badge&logo=springboot&logoColor=white)
![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=for-the-badge)
![DIO](https://img.shields.io/badge/DIO-Bootcamp-purple?style=for-the-badge)

> Projeto desenvolvido como parte do bootcamp **TQI Fullstack Developer** na [DIO](https://www.dio.me), sob orientação do instrutor **Venilton FalvoJr**. O objetivo é explorar os padrões de projeto **Gang of Four (GoF)** na prática — primeiro implementando manualmente com Java puro, depois observando como o Spring Boot aplica os mesmos padrões de forma automática.

---

## 📖 Sobre o Projeto

Este repositório está dividido em **duas partes complementares**. Elas implementam os mesmos três padrões de projeto — Singleton, Strategy e Facade — de formas completamente diferentes, permitindo uma comparação direta entre as abordagens.

| | Parte 1 | Parte 2 |
|---|---|---|
| **Tecnologia** | Java Puro | Spring Boot |
| **Objetivo** | Entender os mecanismos internos de cada padrão | Ver como o Spring os aplica automaticamente |
| **Tipo de projeto** | Aplicação console | API REST completa |
| **Banco de dados** | Nenhum | H2 (em memória) |
| **Integração externa** | Nenhuma | API pública ViaCEP |

---

## 🗂️ Estrutura do Repositório

```
dio-design-patterns-java/
│
├── parte-1-java-puro/               ← Padrões implementados manualmente
│   ├── src/one/digitalinnovation/gof/
│   │   ├── Test.java
│   │   ├── SingletonLazy.java
│   │   ├── SingletonEager.java
│   │   ├── SingletonLazyHolder.java
│   │   ├── Comportamento.java
│   │   ├── ComportamentoAgressivo.java
│   │   ├── ComportamentoDefensivo.java
│   │   ├── ComportamentoNormal.java
│   │   ├── Robo.java
│   │   ├── Subsistema1CRM.java
│   │   ├── Subsistema2CEP.java
│   │   └── FachadaCRM.java
│   └── README.md
│
├── parte-2-spring/                  ← Padrões aplicados pelo Spring Boot
│   ├── src/main/java/one/digitalinnovation/gofspring/
│   │   ├── GofSpringApplication.java
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── pom.xml
│   └── README.md
│
├── .gitignore
└── README.md                        ← este arquivo
```

---

## 🔵 Parte 1 — Java Puro

Implementação manual dos três padrões GoF, sem frameworks ou bibliotecas externas. O objetivo é entender **o que cada padrão faz por baixo dos panos** antes de confiar em abstrações automáticas.

### Padrões implementados

**Singleton** — 3 variações:
- `SingletonLazy`: cria a instância somente quando solicitada pela primeira vez
- `SingletonEager`: cria a instância imediatamente ao carregar a classe
- `SingletonLazyHolder`: combina criação sob demanda com segurança para múltiplas threads ⭐

**Strategy** — robô com comportamento intercambiável:
- Interface `Comportamento` define o contrato
- `ComportamentoAgressivo`, `ComportamentoDefensivo` e `ComportamentoNormal` são as estratégias
- Classe `Robo` troca o comportamento em tempo de execução sem ser recriada

**Facade** — integração simplificada de subsistemas:
- `Subsistema1CRM` e `Subsistema2CEP` simulam sistemas complexos
- `FachadaCRM` expõe um único método que orquestra os dois

### Como executar

```bash
# Clone o repositório
git clone https://github.com/ahaerdy/dio-design-patterns-java.git

# Abra a pasta parte-1-java-puro no IntelliJ
# Execute a classe Test.java
```

**Saída esperada:**
```
=== SINGLETON ===
SingletonLazy — mesma instância? true
SingletonEager — mesma instância? true
SingletonLazyHolder — mesma instância? true

=== STRATEGY ===
ComportamentoNormal: patrulhando a área.
ComportamentoDefensivo: recuando para se proteger!
ComportamentoAgressivo: movendo para o ataque!

=== FACADE ===
Subsistema1CRM.buscarCliente: buscando cliente de CPF 123.456.789-00
Subsistema2CEP.recuperarCep: buscando CEP do CPF 123.456.789-00
FachadaCRM: Migrando cliente ROBERTO do CEP 01310-100
```

📄 [Documentação completa da Parte 1](./parte-1-java-puro/README.md)

---

## 🟢 Parte 2 — Spring Boot

Os mesmos padrões aplicados automaticamente pelo framework. O projeto é uma **API REST completa** que gerencia clientes — ao informar apenas o CEP, o sistema consulta a API pública ViaCEP e preenche o endereço automaticamente.

### Padrões aplicados pelo Spring

**Singleton** via `@Service`: o Spring cria e gerencia uma única instância do `ClienteServiceImpl` em toda a aplicação.

**Strategy/Repository** via `@Repository` + Spring Data JPA: interfaces sem implementação — o Spring gera automaticamente todos os métodos de acesso ao banco (`findAll`, `save`, `deleteById`, etc.).

**Facade** via `@Service` + OpenFeign: o `ClienteServiceImpl` orquestra repositórios e a integração HTTP com o ViaCEP, expondo para o controller apenas métodos simples como `inserir(cliente)`.

### Endpoints disponíveis

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/clientes` | Lista todos os clientes |
| `GET` | `/clientes/{id}` | Busca cliente por ID |
| `POST` | `/clientes` | Insere cliente (busca CEP no ViaCEP) |
| `PUT` | `/clientes/{id}` | Atualiza cliente |
| `DELETE` | `/clientes/{id}` | Remove cliente |

### Como executar

```bash
# Abra a pasta parte-2-spring no IntelliJ
# Execute GofSpringApplication.java
# A API estará disponível em http://localhost:8083/clientes
```

**Exemplo de uso:**
```bash
curl -s -X POST http://localhost:8083/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Venilton", "endereco": {"cep": "01310-100"}}' | python3 -m json.tool
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Venilton",
  "endereco": {
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "ddd": "11"
  }
}
```

📄 [Documentação completa da Parte 2](./parte-2-spring/README.md)

---

## ⚖️ Comparação: Java Puro vs Spring Boot

Esta é a conclusão central do projeto. Os padrões são **os mesmos** — o que muda é quem faz o trabalho.

### Singleton

```java
// ✋ Java Puro — você implementa o padrão
public class SingletonLazyHolder {
    private static class InstanceHolder {
        private static final SingletonLazyHolder instancia = new SingletonLazyHolder();
    }
    private SingletonLazyHolder() {}
    public static SingletonLazyHolder getInstancia() {
        return InstanceHolder.instancia;
    }
}

// 🤖 Spring Boot — o framework aplica o padrão
@Service
public class ClienteServiceImpl implements ClienteService {
    // Uma única instância garantida pelo Spring. Zero código extra.
}
```

### Strategy

```java
// ✋ Java Puro — você cria a interface e todas as implementações
public interface Comportamento { void mover(); }
public class ComportamentoAgressivo implements Comportamento { ... }
public class ComportamentoDefensivo implements Comportamento { ... }
public class ComportamentoNormal implements Comportamento { ... }

// 🤖 Spring Boot — você declara a interface, o Spring gera a implementação
@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    // Vazio. O Spring cria findAll(), save(), deleteById()... em tempo de execução.
}
```

### Facade

```java
// ✋ Java Puro — você cria os subsistemas e a fachada manualmente
public class FachadaCRM {
    private Subsistema1CRM crm = new Subsistema1CRM();
    private Subsistema2CEP cep = new Subsistema2CEP();
    public void migrarCliente(String cpf) {
        String cliente = crm.buscarCliente(cpf);
        String cepCliente = cep.recuperarCep(cpf);
        System.out.println("Migrando " + cliente + " do CEP " + cepCliente);
    }
}

// 🤖 Spring Boot — o Feign gera o cliente HTTP, o @Service orquestra
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {
    @GetMapping("/{cep}/json/")
    Endereco consultarCep(@PathVariable("cep") String cep);
    // O Spring gera toda a lógica HTTP automaticamente.
}
```

---

### Quando usar cada abordagem?

| Situação | Recomendação |
|---|---|
| Aprendizado — entender como os padrões funcionam por dentro | ✅ Java Puro |
| Scripts simples, utilitários, sem dependências externas | ✅ Java Puro |
| APIs REST, sistemas web, microsserviços | ✅ Spring Boot |
| Projetos com banco de dados, autenticação, cache | ✅ Spring Boot |
| Equipes grandes, projetos de longa duração | ✅ Spring Boot |

> **Conclusão:** o Spring Boot não substitui o conhecimento dos padrões — ele os utiliza internamente. Entender o Java puro primeiro é o que permite usar o Spring com consciência, sabendo exatamente o que cada anotação faz por baixo dos panos.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.3 | Framework da Parte 2 |
| Spring Data JPA | — | Padrão Repository (Parte 2) |
| Spring Cloud OpenFeign | 5.0.0 | Cliente HTTP para ViaCEP (Parte 2) |
| H2 Database | — | Banco em memória (Parte 2) |
| Maven | — | Gerenciador de dependências (Parte 2) |
| IntelliJ IDEA | — | IDE utilizada no desenvolvimento |

---

## 📚 Referências

- [Refactoring Guru — catálogo visual de Padrões de Projeto](https://refactoring.guru/pt-br/design-patterns)
- [Documentação oficial do Spring Boot](https://spring.io/projects/spring-boot)
- [API pública ViaCEP](https://viacep.com.br)
- [Repositório original — Java Puro (DIO)](https://github.com/digitalinnovationone/lab-padroes-projeto-java)
- [Repositório original — Spring Boot (DIO)](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
- Livro de referência: *"Padrões de Projeto"* — Gamma, Helm, Johnson, Vlissides (1995)

---

## 👨‍💻 Autor

Desenvolvido por **Arthur Haerdy** como parte do bootcamp TQI Fullstack Developer na DIO.

- GitHub: [@ahaerdy](https://github.com/ahaerdy)

---

*Projeto baseado no Lab "Explorando Padrões de Projetos na Prática com Java" ministrado por [Venilton FalvoJr](https://www.linkedin.com/in/falvojr/) na [DIO](https://www.dio.me).*