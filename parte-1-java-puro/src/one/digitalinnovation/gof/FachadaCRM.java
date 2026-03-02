package one.digitalinnovation.gof;

/**
 * FACADE: Simplifica a interação com dois subsistemas complexos.
 * Em vez de o cliente chamar o CRM e o serviço de CEP separadamente,
 * ele usa apenas esta fachada.
 */
public class FachadaCRM {

    // A fachada conhece os subsistemas por baixo
    private Subsistema1CRM crm = new Subsistema1CRM();
    private Subsistema2CEP cep = new Subsistema2CEP();

    // Um único método simples que orquestra os dois subsistemas
    public void migrarCliente(String cpf) {
        String cliente = crm.buscarCliente(cpf);
        String cepCliente = cep.recuperarCep(cpf);
        System.out.println("FachadaCRM: Migrando cliente " + cliente + " do CEP " + cepCliente);
    }
}