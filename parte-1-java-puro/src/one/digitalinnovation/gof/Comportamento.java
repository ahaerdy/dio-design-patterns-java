package one.digitalinnovation.gof;

/**
 * Interface que define o "contrato" do padrão Strategy.
 * Qualquer classe que implemente esta interface pode ser usada
 * como um comportamento intercambiável.
 */
public interface Comportamento {
    void mover();
}