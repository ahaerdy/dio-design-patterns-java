package one.digitalinnovation.gof;

/**
 * Uma implementação concreta do Strategy: comportamento agressivo.
 */
public class ComportamentoAgressivo implements Comportamento {

    @Override
    public void mover() {
        System.out.println("ComportamentoAgressivo: movendo para o ataque!");
    }
}
