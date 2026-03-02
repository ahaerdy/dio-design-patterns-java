package one.digitalinnovation.gof;

/**
 * Contexto do padrão Strategy.
 * O Robô pode ter seu comportamento trocado em tempo de execução,
 * sem precisar mudar o código do próprio Robô.
 */
public class Robo {

    // O comportamento atual do robô (pode ser qualquer Strategy)
    private Comportamento comportamento;

    // Permite trocar o comportamento em tempo de execução
    public void setComportamento(Comportamento comportamento) {
        this.comportamento = comportamento;
    }

    public void mover() {
        comportamento.mover();
    }
}