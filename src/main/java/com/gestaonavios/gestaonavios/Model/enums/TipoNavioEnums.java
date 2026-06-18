package Model.enums;

public enum TipoNavioEnums {
    PETROLEIRO_CRUDE  (1),
    PRODUTOS_REFINADOS(4),
    QUIMICO           (1),
    QUIMICO_PRODUTOS  (4);

    private final int maxCargasPorViagem;

    TipoNavioEnums(int maxCargasPorViagem) {
        this.maxCargasPorViagem = maxCargasPorViagem;
    }

    public int getMaxCargasPorViagem() { return maxCargasPorViagem; }

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}