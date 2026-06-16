// Capitao.java
package Model;
import Model.enums.FuncaoTripulante;

public class Capitao extends Tripulante {
    public Capitao() { super(); }
    public Capitao(int id, String nome, String nif, boolean disponivel,
                   String nacionalidade, String certificacoes) {
        super(id, nome, nif, FuncaoTripulante.CAPITAO, disponivel, nacionalidade, certificacoes);
    }
    @Override
    public String getFuncao() { return FuncaoTripulante.CAPITAO.name(); }
}