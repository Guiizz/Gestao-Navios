// Oficial.java
package com.gestaonavios.gestaonavios.Model;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

public class Oficial extends Tripulante {
    public Oficial() { super(); }
    public Oficial(int id, String nome, String nif, boolean disponivel,
                   String nacionalidade, String certificacoes) {
        super(id, nome, nif, FuncaoTripulante.OFICIAL, disponivel, nacionalidade, certificacoes);
    }
    @Override
    public String getFuncao() { return FuncaoTripulante.OFICIAL.name(); }
}