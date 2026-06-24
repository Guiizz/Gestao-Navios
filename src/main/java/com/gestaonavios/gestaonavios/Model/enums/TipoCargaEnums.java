package com.gestaonavios.gestaonavios.Model.enums;

public enum TipoCargaEnums {
    PETROLEO_BRUTO(true, false, false),
    GASOLINA(true, false, false),
    DIESEL_GASOLEO(true, false, false),
    JET_FUEL_QUEROSENE(true, false, false),
    FUELOLEO_BETUME(true, false, false),
    PRODUTOS_QUIMICOS_LIQUIDOS(true, true, true);

    private final boolean inflamavel;
    private final boolean corrosiva;
    private final boolean toxica;

    TipoCargaEnums(boolean inflamavel, boolean corrosiva, boolean toxica) {
        this.inflamavel = inflamavel;
        this.corrosiva = corrosiva;
        this.toxica = toxica;
    }

    public boolean isInflamavel() {
        return inflamavel;
    }

    public boolean isCorrosiva() {
        return corrosiva;
    }

    public boolean isToxica() {
        return toxica;
    }

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }

    public String propriedades() {
        StringBuilder sb = new StringBuilder();
        if (inflamavel) sb.append("Inflamável ");
        if (corrosiva) sb.append("Corrosiva ");
        if (toxica) sb.append("Tóxica");
        String result = sb.toString().trim();
        return result.isEmpty() ? "Nenhuma" : result;
    }
}
