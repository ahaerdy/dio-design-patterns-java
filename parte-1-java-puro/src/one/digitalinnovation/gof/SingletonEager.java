package one.digitalinnovation.gof;

/**
 * Singleton "Eager" (Ansioso).
 *
 * A instância é criada assim que a classe é carregada pela JVM,
 * mesmo que ninguém a tenha pedido ainda.
 * VANTAGEM: Thread-safe por natureza.
 * DESVANTAGEM: Consome memória mesmo se nunca for usado.
 */
public class SingletonEager {

    // A instância é criada IMEDIATAMENTE quando a classe é carregada
    private static final SingletonEager instancia = new SingletonEager();

    // Construtor PRIVADO
    private SingletonEager() {}

    // Método público para obter a instância única
    public static SingletonEager getInstancia() {
        return instancia;
    }
}