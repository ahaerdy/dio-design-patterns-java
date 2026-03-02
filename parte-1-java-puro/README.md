# Parte 1 — Padrões de Projeto com Java Puro

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=for-the-badge)
![Padrão](https://img.shields.io/badge/Padr%C3%B5es-GoF-blue?style=for-the-badge)
![DIO](https://img.shields.io/badge/DIO-Bootcamp-purple?style=for-the-badge)

> Implementação manual dos padrões de projeto **Singleton**, **Strategy** e **Facade** utilizando Java puro — sem frameworks — para compreender os mecanismos internos de cada solução antes de avançar para o Spring.

---

## 📚 O que são Padrões de Projeto (Design Patterns)?

Imagine que você é um arquiteto e percebe que toda vez que projeta um prédio, o problema de "como fazer a escada de emergência" aparece. Você poderia resolver do zero toda vez — ou poderia usar uma **solução consolidada** que já foi testada e aprovada em centenas de prédios.

Design Patterns são exatamente isso para o desenvolvimento de software: **soluções reutilizáveis para problemas que aparecem repetidamente** em projetos orientados a objetos.

### O livro que originou tudo — "Gang of Four" (GoF)

Em 1995, quatro engenheiros de software — **Erich Gamma, Richard Helm, Ralph Johnson e John Vlissides** — publicaram o livro *"Design Patterns: Elements of Reusable Object-Oriented Software"*. O apelido **"Gang of Four"** (gangue dos quatro) ficou famoso e é uma referência obrigatória até hoje.

Eles catalogaram **23 padrões**, divididos em 3 categorias:

| Categoria | O que resolve | Exemplos |
|---|---|---|
| **Criacional** | Como os objetos são **criados** | Singleton, Builder, Factory |
| **Comportamental** | Como os objetos **se comunicam** | Strategy, Observer, Command |
| **Estrutural** | Como os objetos são **compostos/organizados** | Facade, Adapter, Decorator |

Neste projeto, implementamos **um representante de cada categoria**:
- 🔵 **Singleton** — Criacional
- 🟢 **Strategy** — Comportamental
- 🟠 **Facade** — Estrutural

---

## 📁 Estrutura do Projeto

```
parte-1-java-puro/
└── src/
    └── one/
        └── digitalinnovation/
            └── gof/
                ├── Test.java                    ← Programa principal (ponto de entrada)
                │
                ├── SingletonLazy.java            ← Padrão Singleton (variação 1)
                ├── SingletonEager.java           ← Padrão Singleton (variação 2)
                ├── SingletonLazyHolder.java      ← Padrão Singleton (variação 3 — melhor)
                │
                ├── Comportamento.java            ← Interface do padrão Strategy
                ├── ComportamentoAgressivo.java   ← Estratégia concreta 1
                ├── ComportamentoDefensivo.java   ← Estratégia concreta 2
                ├── ComportamentoNormal.java      ← Estratégia concreta 3
                ├── Robo.java                     ← Contexto do padrão Strategy
                │
                ├── Subsistema1CRM.java           ← Subsistema complexo 1
                ├── Subsistema2CEP.java           ← Subsistema complexo 2
                └── FachadaCRM.java               ← Padrão Facade
```

> **Por que o pacote `one.digitalinnovation.gof`?**  
> Em Java, o nome de pacote segue uma convenção de domínio reverso. `one` representa a DIO (Digital Innovation One), `digitalinnovation` é o nome da organização e `gof` faz referência ao *Gang of Four*.

---

## 🔵 Padrão 1 — Singleton (Criacional)

### O problema que ele resolve

Algumas classes não devem ter mais de uma instância em toda a aplicação. Por exemplo: a conexão com o banco de dados, um arquivo de configurações ou um gerenciador de logs. Criar múltiplas instâncias desperdiçaria recursos e poderia causar comportamentos inconsistentes.

### Analogia do mundo real

> Pense no **Presidente de um país**. Em um momento, só pode existir um. Não importa quantas vezes alguém pergunte "quem é o presidente?" — a resposta sempre aponta para a mesma pessoa. O Singleton funciona da mesma forma: uma classe, uma instância, um ponto de acesso.

### Diagrama UML

```
┌──────────────────────────────────────┐
│           SingletonLazy              │
├──────────────────────────────────────┤
│ - instancia: SingletonLazy  [static] │
├──────────────────────────────────────┤
│ - SingletonLazy()            [priv]  │
│ + getInstancia(): Singleton  [stat]  │
└──────────────────────────────────────┘
```

### As 3 variações implementadas

---

#### Variação 1: `SingletonLazy` — O Preguiçoso

```java
public class SingletonLazy {

    private static SingletonLazy instancia; // começa como null

    private SingletonLazy() {} // ninguém pode chamar 'new' de fora

    public static SingletonLazy getInstancia() {
        if (instancia == null) {
            instancia = new SingletonLazy(); // cria só se não existir
        }
        return instancia;
    }
}
```

**Como funciona, linha por linha:**

- `private static SingletonLazy instancia` → O atributo começa como `null`. A palavra `static` significa que ele pertence à **classe**, não a um objeto específico. Assim, é compartilhado por toda a aplicação.
- `private SingletonLazy()` → O construtor é **privado**. Isso impede que qualquer outro código faça `new SingletonLazy()` diretamente — a única forma de obter uma instância é pelo método `getInstancia()`.
- `if (instancia == null)` → Só cria a instância se ainda não existir nenhuma. Na segunda chamada em diante, essa condição é falsa e retorna a instância já existente.

**⚠️ Problema:** Se duas threads chamarem `getInstancia()` ao **mesmo tempo**, ambas podem passar pelo `if (instancia == null)` antes de qualquer uma criar o objeto — resultando em **duas instâncias**. Para aplicações simples (single-thread), funciona perfeitamente.

---

#### Variação 2: `SingletonEager` — O Ansioso

```java
public class SingletonEager {

    private static final SingletonEager instancia = new SingletonEager(); // cria imediatamente

    private SingletonEager() {}

    public static SingletonEager getInstancia() {
        return instancia;
    }
}
```

**Como funciona:**

- `private static final SingletonEager instancia = new SingletonEager()` → A instância é criada **no momento em que a JVM carrega a classe**, antes mesmo de qualquer código a chamar. A palavra `final` garante que essa referência nunca poderá apontar para outro objeto.

**✅ Vantagem:** É thread-safe por natureza — a JVM garante que a inicialização estática acontece uma única vez.  
**⚠️ Desvantagem:** Consome memória **mesmo que nunca seja utilizado** durante a execução do programa.

---

#### Variação 3: `SingletonLazyHolder` — O Melhor dos Dois Mundos ⭐

```java
public class SingletonLazyHolder {

    private static class InstanceHolder {
        private static final SingletonLazyHolder instancia = new SingletonLazyHolder();
    }

    private SingletonLazyHolder() {}

    public static SingletonLazyHolder getInstancia() {
        return InstanceHolder.instancia;
    }
}
```

**Como funciona:**

- A classe interna `InstanceHolder` só é carregada pela JVM **quando `getInstancia()` é chamado pela primeira vez**. Isso é uma garantia do comportamento da JVM com classes internas estáticas.
- Quando ela é carregada, a instância é criada — e por ser uma inicialização estática, é **automaticamente thread-safe**.
- Resultado: criação "preguiçosa" + segurança para múltiplas threads. ✅

**Por que usar esta variação em produção?**  
Ela combina as vantagens das duas anteriores sem nenhum dos problemas. É a abordagem recomendada pela comunidade Java para implementar Singleton.

---

#### Por que não simplesmente fazer um objeto estático e usar direto?

| Abordagem | Problema |
|---|---|
| Variável estática pública | Qualquer código pode substituir a referência, quebrar o contrato |
| Lazy sem proteção | Não funciona com múltiplas threads |
| Eager | Desperdiça memória se nunca for usado |
| **LazyHolder** | ✅ Sem problemas — é a forma correta |

---

## 🟢 Padrão 2 — Strategy (Comportamental)

### O problema que ele resolve

Imagine que você tem um código com um `if/else` (ou `switch`) gigante para decidir qual algoritmo usar:

```java
// ❌ Sem Strategy — difícil de manter
if (tipo == "agressivo") {
    // 50 linhas de lógica agressiva
} else if (tipo == "defensivo") {
    // 50 linhas de lógica defensiva
} else {
    // 50 linhas de lógica normal
}
```

Cada vez que você precisa adicionar um novo comportamento, tem que **mexer nesse bloco** — arriscando quebrar os comportamentos que já funcionavam. O Strategy resolve isso encapsulando cada algoritmo em uma classe própria.

### Analogia do mundo real

> Pense em um **GPS de carro**. Ele pode calcular a rota mais rápida, a mais econômica ou a que evita pedágios. O destino (o "contexto") é o mesmo — só a **estratégia de navegação** muda. Você pode trocar a estratégia sem precisar trocar o GPS.

### Diagrama UML

```
        «interface»
       Comportamento
      ┌─────────────┐
      │  + mover()  │
      └──────┬──────┘
             │ implementa
    ┌────────┼────────┐
    ▼        ▼        ▼
Agressivo Defensivo Normal
  mover()  mover()  mover()

      Robo
   ┌──────────────────────┐
   │ - comportamento      │◄── recebe qualquer implementação
   │ + setComportamento() │
   │ + mover()            │
   └──────────────────────┘
```

### O código

#### Interface `Comportamento` — o "contrato"

```java
public interface Comportamento {
    void mover();
}
```

Uma **interface** em Java define um contrato: qualquer classe que implemente `Comportamento` **obrigatoriamente** precisa ter um método `mover()`. Ela não diz *como* mover — só diz *que* é preciso saber mover.

#### Implementações concretas

```java
public class ComportamentoAgressivo implements Comportamento {
    @Override
    public void mover() {
        System.out.println("ComportamentoAgressivo: movendo para o ataque!");
    }
}
```

- `implements Comportamento` → essa classe "assina o contrato" da interface.
- `@Override` → anotação que indica que este método está cumprindo o contrato definido na interface. O compilador verifica que o método realmente existe na interface — ajuda a evitar erros de digitação.

As classes `ComportamentoDefensivo` e `ComportamentoNormal` seguem o mesmo padrão, cada uma com seu próprio comportamento.

#### Classe `Robo` — o contexto que usa a Strategy

```java
public class Robo {

    private Comportamento comportamento; // referência para QUALQUER implementação

    public void setComportamento(Comportamento comportamento) {
        this.comportamento = comportamento; // troca o comportamento em tempo real
    }

    public void mover() {
        comportamento.mover(); // delega a decisão para a Strategy atual
    }
}
```

**O ponto chave:** O atributo é do tipo `Comportamento` (a interface), não `ComportamentoAgressivo` ou qualquer implementação específica. Isso significa que o `Robo` aceita **qualquer** objeto que implemente essa interface — sem precisar conhecer os detalhes de cada um.

**Como funciona na prática:**

```java
Robo robo = new Robo();

robo.setComportamento(new ComportamentoNormal());
robo.mover(); // → "patrulhando a área"

robo.setComportamento(new ComportamentoAgressivo());
robo.mover(); // → "movendo para o ataque!" — sem recriar o Robo!
```

O comportamento foi **trocado em tempo de execução** sem alterar uma linha da classe `Robo`.

---

#### Por que não usar herança ao invés de interface?

| Herança (`extends`) | Interface + Strategy |
|---|---|
| `RoboAgressivo extends Robo` | Um único `Robo` com comportamento injetado |
| Para trocar, precisa criar um **novo objeto** | Troca o comportamento **no mesmo objeto** |
| Explosão de subclasses com combinações | Combinações ilimitadas sem novas classes |

> **Regra de ouro em OOP:** *"Prefira composição à herança"* — e o Strategy é um exemplo claro dessa prática.

---

## 🟠 Padrão 3 — Facade (Estrutural)

### O problema que ele resolve

Sistemas reais são compostos de muitos subsistemas complexos. Quando um cliente precisa interagir com vários deles para realizar uma única tarefa, o código fica acoplado e difícil de manter. O Facade cria uma **camada simplificada** que esconde essa complexidade.

### Analogia do mundo real

> Pense em um **controle remoto de home theater**. Para assistir um filme, você precisaria ligar a TV, ajustar o input do HDMI, ligar o receiver de áudio, configurar o canal de som surround e escurecer as luzes. O controle remoto tem um botão **"Assistir Filme"** que faz tudo isso de uma vez. Esse botão é a Facade — você não sabe (nem precisa saber) o que acontece por baixo.

### Diagrama UML

```
  [Cliente]
      │
      │ chama apenas
      ▼
  FachadaCRM
  ┌────────────────┐
  │ migrarCliente()│
  └───────┬────────┘
          │ orquestra internamente
    ┌─────┴──────┐
    ▼            ▼
Subsistema1   Subsistema2
   CRM           CEP
buscarCliente() recuperarCep()
```

### O código

#### Subsistemas — as partes complexas

```java
public class Subsistema1CRM {
    public String buscarCliente(String cpf) {
        System.out.println("Subsistema1CRM.buscarCliente: buscando cliente de CPF " + cpf);
        return "ROBERTO"; // simulando retorno de um banco de dados
    }
}

public class Subsistema2CEP {
    public String recuperarCep(String cpf) {
        System.out.println("Subsistema2CEP.recuperarCep: buscando CEP do CPF " + cpf);
        return "01310-100"; // simulando retorno de um serviço externo
    }
}
```

Em um sistema real, cada subsistema poderia ter dezenas de métodos, conectar a bancos de dados diferentes, chamar APIs externas, etc.

#### Classe `FachadaCRM` — a simplificação

```java
public class FachadaCRM {

    private Subsistema1CRM crm = new Subsistema1CRM();
    private Subsistema2CEP cep = new Subsistema2CEP();

    public void migrarCliente(String cpf) {
        String cliente = crm.buscarCliente(cpf);   // passo 1 escondido
        String cepCliente = cep.recuperarCep(cpf);  // passo 2 escondido
        System.out.println("FachadaCRM: Migrando cliente " + cliente + " do CEP " + cepCliente);
    }
}
```

**O que mudou para o cliente?** Em vez de:

```java
// ❌ Sem Facade — cliente sabe demais sobre os subsistemas
Subsistema1CRM crm = new Subsistema1CRM();
Subsistema2CEP cep = new Subsistema2CEP();
String cliente = crm.buscarCliente("123.456.789-00");
String cepCliente = cep.recuperarCep("123.456.789-00");
// ... mais lógica de orquestração
```

Basta:

```java
// ✅ Com Facade — simples e direto
FachadaCRM facade = new FachadaCRM();
facade.migrarCliente("123.456.789-00");
```

---

#### Por que não simplesmente colocar tudo em uma classe?

| Uma classe gigante ("God Class") | Facade + Subsistemas |
|---|---|
| Código difícil de testar | Cada subsistema é testável independentemente |
| Qualquer mudança afeta tudo | Mudanças nos subsistemas não afetam o cliente |
| Um único dev responsável por tudo | Times diferentes podem trabalhar em paralelo |

---

## ▶️ Como Executar

### Pré-requisitos
- JDK 11 ou superior instalado
- IntelliJ IDEA (Community ou Ultimate)

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/ahaerdy/dio-design-patterns-java.git
   ```

2. Abra o IntelliJ IDEA → **File → Open** → selecione a pasta `parte-1-java-puro`

3. Aguarde o IntelliJ indexar o projeto

4. Abra o arquivo `src/one/digitalinnovation/gof/Test.java`

5. Clique no botão **▶ Run** ao lado do método `main` (ou pressione `Shift + F10`)

### Saída esperada no console

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

---

## 💡 Resumo Comparativo dos 3 Padrões

| Padrão | Categoria | Problema que resolve | Palavra-chave |
|---|---|---|---|
| **Singleton** | Criacional | Garantir **uma única instância** de uma classe | *Unicidade* |
| **Strategy** | Comportamental | Trocar **algoritmos/comportamentos** em tempo de execução | *Intercambiabilidade* |
| **Facade** | Estrutural | **Simplificar** o acesso a sistemas complexos | *Simplicidade* |

---

## 🔗 Referências

- [Design Patterns — GoF (Wikipedia)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Refactoring Guru — catálogo visual de Design Patterns](https://refactoring.guru/design-patterns)
- [DIO — Lab Padrões de Projeto Java (repositório original)](https://github.com/digitalinnovationone/lab-padroes-projeto-java)
- Livro: *"Design Patterns: Elements of Reusable Object-Oriented Software"* — Gamma, Helm, Johnson, Vlissides (1995)

---

> ➡️ **Próxima etapa:** [Parte 2 — Padrões de Projeto com Spring Boot](../parte-2-spring/README.md)  
> Veremos como o Spring Framework abstrai automaticamente esses mesmos padrões, reduzindo drasticamente a quantidade de código necessário.