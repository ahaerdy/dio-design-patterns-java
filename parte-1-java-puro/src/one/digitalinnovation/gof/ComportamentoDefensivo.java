package one.digitalinnovation.gof;

public class ComportamentoDefensivo implements Comportamento {

    @Override
    public void mover() {
        System.out.println("ComportamentoDefensivo: recuando para se proteger!");
    }
}
