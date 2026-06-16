// Engenheiro.java
package Model;
import Model.enums.FuncaoTripulante;

public class Engenheiro extends Tripulante {
    public Engenheiro() { super(); }
    public Engenheiro(int id, String nome, String nif, boolean disponivel,
                      String nacionalidade, String certificacoes) {
        super(id, nome, nif, FuncaoTripulante.ENGENHEIRO, disponivel, nacionalidade, certificacoes);
    }
    @Override
    public String getFuncao() { return FuncaoTripulante.ENGENHEIRO.name(); }
}