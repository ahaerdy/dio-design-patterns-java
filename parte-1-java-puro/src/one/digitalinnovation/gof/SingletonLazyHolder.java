package one.digitalinnovation.gof;

/**
 * Singleton "Lazy Holder" — a forma mais recomendada em Java.
 *
 * Combina as vantagens do Lazy (cria só quando precisa) com
 * thread-safety (seguro para múltiplas threads).
 * Funciona graças à garantia da JVM de que classes internas
 * estáticas são inicializadas de forma segura.
 */
public class SingletonLazyHolder {

    // Classe interna estática que "segura" a instância
    private static class InstanceHolder {
        // Só é criada quando getInstancia() for chamado pela primeira vez
        private static final SingletonLazyHolder instancia = new SingletonLazyHolder();
    }

    // Construtor PRIVADO
    private SingletonLazyHolder() {}

    // Método público para obter a instância única
    public static SingletonLazyHolder getInstancia() {
        return InstanceHolder.instancia;
    }
}
