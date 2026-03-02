package one.digitalinnovation.gof;

/**
 * Classe principal para testar todos os padrões de projeto implementados.
 */
public class Test {

    public static void main(String[] args) {

        // ==========================================
        // SINGLETON — testando as 3 variações
        // ==========================================
        System.out.println("=== SINGLETON ===");

        SingletonLazy lazy1 = SingletonLazy.getInstancia();
        SingletonLazy lazy2 = SingletonLazy.getInstancia();
        System.out.println("SingletonLazy — mesma instância? " + (lazy1 == lazy2));

        SingletonEager eager1 = SingletonEager.getInstancia();
        SingletonEager eager2 = SingletonEager.getInstancia();
        System.out.println("SingletonEager — mesma instância? " + (eager1 == eager2));

        SingletonLazyHolder holder1 = SingletonLazyHolder.getInstancia();
        SingletonLazyHolder holder2 = SingletonLazyHolder.getInstancia();
        System.out.println("SingletonLazyHolder — mesma instância? " + (holder1 == holder2));

        // ==========================================
        // STRATEGY — trocando comportamento do Robô
        // ==========================================
        System.out.println("\n=== STRATEGY ===");

        Robo robo = new Robo();

        robo.setComportamento(new ComportamentoNormal());
        robo.mover();

        robo.setComportamento(new ComportamentoDefensivo());
        robo.mover();

        robo.setComportamento(new ComportamentoAgressivo());
        robo.mover();

        // ==========================================
        // FACADE — simplificando subsistemas
        // ==========================================
        System.out.println("\n=== FACADE ===");

        FachadaCRM facade = new FachadaCRM();
        facade.migrarCliente("123.456.789-00");
    }
}