package com.gestaonavios.gestaonavios.Model.enums;

public enum FuncaoTripulante {
    CAPITAO,
    OFICIAL,
    ENGENHEIRO,
    OPERADOR;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
