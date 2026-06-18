package Model.enums;

public enum EstadoViagem {
    PLANEADA,
    EM_CURSO,
    CONCLUIDA,
    CANCELADA;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}