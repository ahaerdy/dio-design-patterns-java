package one.digitalinnovation.gof;

/**
 * Singleton "Lazy" (Preguiçoso).
 *
 * A instância só é criada quando alguém a pede pela primeira vez.
 * PROBLEMA: Não é seguro para uso em múltiplas threads simultâneas
 * (pode criar mais de uma instância em condições de corrida).
 */
public class SingletonLazy {

    // O atributo 'instancia' começa como null (ainda não foi criado)
    private static SingletonLazy instancia;

    // Construtor PRIVADO: ninguém de fora pode fazer 'new SingletonLazy()'
    private SingletonLazy() {}

    // Método público para obter a instância única
    public static SingletonLazy getInstancia() {
        if (instancia == null) {
            // Só cria se ainda não existir
            instancia = new SingletonLazy();
        }
        return instancia;
    }
}
