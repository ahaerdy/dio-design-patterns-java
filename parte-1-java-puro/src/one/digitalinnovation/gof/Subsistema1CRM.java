package one.digitalinnovation.gof;

public class Subsistema1CRM {
    public String buscarCliente(String cpf) {
        System.out.println("Subsistema1CRM.buscarCliente: buscando cliente de CPF " + cpf);
        // Simulando retorno de um banco de dados
        return "ROBERTO";
    }
}
