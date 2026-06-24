// Operador.java
package com.gestaonavios.gestaonavios.Model;

import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

public class Operador extends Tripulante {
    public Operador() {
        super();
    }

    public Operador(int id, String nome, String nif, boolean disponivel,
                    String nacionalidade, String certificacoes) {
        super(id, nome, nif, FuncaoTripulante.OPERADOR, disponivel, nacionalidade, certificacoes);
    }

    @Override
    public String getFuncao() {
        return FuncaoTripulante.OPERADOR.name();
    }
}