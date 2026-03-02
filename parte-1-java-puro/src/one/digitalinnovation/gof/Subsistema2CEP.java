package one.digitalinnovation.gof;

public class Subsistema2CEP {
    public String recuperarCep(String cpf) {
        System.out.println("Subsistema2CEP.recuperarCep: buscando CEP do CPF " + cpf);
        // Simulando retorno de um serviço externo
        return "01310-100";
    }
}
