package one.digitalinnovation.gofspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Representa um cliente da aplicação.
 *
 * @Id + @GeneratedValue: o banco de dados gera o ID automaticamente,
 * começando em 1 e incrementando a cada novo cliente.
 *
 * @ManyToOne: muitos clientes podem ter o mesmo endereço (mesmo CEP).
 * O Spring cuida do JOIN entre as tabelas automaticamente.
 */
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    @ManyToOne
    private Endereco endereco;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}