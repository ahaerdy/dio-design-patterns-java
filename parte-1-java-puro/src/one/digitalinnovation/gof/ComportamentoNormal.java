package one.digitalinnovation.gof;

public class ComportamentoNormal implements Comportamento {

    @Override
    public void mover() {
        System.out.println("ComportamentoNormal: patrulhando a área.");
    }
}
