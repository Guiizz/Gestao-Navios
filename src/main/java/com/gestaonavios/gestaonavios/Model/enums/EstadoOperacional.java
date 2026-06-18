package com.gestaonavios.gestaonavios.Model.enums;

public enum EstadoOperacional {
    ATIVO,
    EM_MANUTENCAO,
    INATIVO;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
