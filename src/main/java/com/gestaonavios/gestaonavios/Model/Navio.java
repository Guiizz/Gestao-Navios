package Model;

import Model.enums.EstadoOperacional;
import Model.enums.TipoCargaEnums;
import Model.enums.TipoNavioEnums;

import java.util.ArrayList;
import java.util.List;

public class Navio {

    private int id;
    private String nome;
    private String codigoIMO;
    private TipoNavioEnums tipoNavioEnums;
    private double capacidadeMaxima;
    private int numeroTanques;
    private String bandeira;
    private int anoFabrico;
    private EstadoOperacional estadoOperacional;
    private Porto portoAtual;

    private static final List<TipoCargaEnums> CARGAS_CRUDE = List.of(TipoCargaEnums.PETROLEO_BRUTO);
    private static final List<TipoCargaEnums> CARGAS_REFINADOS = List.of(TipoCargaEnums.GASOLINA, TipoCargaEnums.DIESEL_GASOLEO, TipoCargaEnums.JET_FUEL_QUEROSENE, TipoCargaEnums.FUELOLEO_BETUME);
    private static final List<TipoCargaEnums> CARGAS_QUIMICO = List.of(TipoCargaEnums.PRODUTOS_QUIMICOS_LIQUIDOS);
    private static final List<TipoCargaEnums> CARGAS_QUIMICO_PRODUTOS = List.of(TipoCargaEnums.PRODUTOS_QUIMICOS_LIQUIDOS, TipoCargaEnums.GASOLINA, TipoCargaEnums.DIESEL_GASOLEO, TipoCargaEnums.JET_FUEL_QUEROSENE);

    public Navio() {}

    public Navio(int id, String nome, String codigoIMO, TipoNavioEnums tipoNavioEnums, double capacidadeMaxima, int numeroTanques, String bandeira, int anoFabrico, EstadoOperacional estadoOperacional, Porto portoAtual) {
        this.id = id;
        this.nome = nome;
        this.codigoIMO = codigoIMO;
        this.tipoNavioEnums = tipoNavioEnums;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroTanques = numeroTanques;
        this.bandeira = bandeira;
        this.anoFabrico = anoFabrico;
        this.estadoOperacional = estadoOperacional;
        this.portoAtual = portoAtual;
    }

    public boolean aceitaTipoCarga(TipoCargaEnums tipo) {
        return getCargasCompativeis().contains(tipo);
    }

    public List<TipoCargaEnums> getCargasCompativeis() {
        switch (tipoNavioEnums) {
            case PETROLEIRO_CRUDE:return CARGAS_CRUDE;
            case PRODUTOS_REFINADOS:return CARGAS_REFINADOS;
            case QUIMICO:return CARGAS_QUIMICO;
            case QUIMICO_PRODUTOS:return CARGAS_QUIMICO_PRODUTOS;
            default:return new ArrayList<>();
        }
    }

    public int getMaxCargasPorViagem() { return numeroTanques; }

    public boolean podeIniciarViagem() {
        return estadoOperacional == EstadoOperacional.ATIVO;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigoIMO() { return codigoIMO; }
    public void setCodigoIMO(String codigoIMO) { this.codigoIMO = codigoIMO; }

    public TipoNavioEnums getTipoNavio() { return tipoNavioEnums; }
    public void setTipoNavio(TipoNavioEnums tipoNavioEnums) { this.tipoNavioEnums = tipoNavioEnums; }

    public double getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(double capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public int getNumeroTanques() { return numeroTanques; }
    public void setNumeroTanques(int numeroTanques) { this.numeroTanques = numeroTanques; }

    public String getBandeira() { return bandeira; }
    public void setBandeira(String bandeira) { this.bandeira = bandeira; }

    public int getAnoFabrico() { return anoFabrico; }
    public void setAnoFabrico(int anoFabrico) { this.anoFabrico = anoFabrico; }

    public EstadoOperacional getEstadoOperacional() { return estadoOperacional; }
    public void setEstadoOperacional(EstadoOperacional e) { this.estadoOperacional = e; }

    public Porto getPortoAtual() { return portoAtual; }
    public void setPortoAtual(Porto portoAtual) { this.portoAtual = portoAtual; }

    public boolean isDisponivel() {
        return estadoOperacional == EstadoOperacional.ATIVO;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (IMO: " + codigoIMO + ") - " + tipoNavioEnums;
    }
}