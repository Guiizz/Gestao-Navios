package Model;

public class Tanque {
    private int id;
    private int numero;
    private double capacidade;

    public Tanque() {}

    public Tanque(int id, int numero, double capacidade) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    @Override
    public String toString() { return "Tanque #" + numero; }
}
