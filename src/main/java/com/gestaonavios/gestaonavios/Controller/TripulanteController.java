package com.gestaonavios.gestaonavios.Controller;

import com.gestaonavios.gestaonavios.BLL.TripulanteBLL;
import com.gestaonavios.gestaonavios.BLL.ViagemBLL;
import com.gestaonavios.gestaonavios.Model.Capitao;
import com.gestaonavios.gestaonavios.Model.Engenheiro;
import com.gestaonavios.gestaonavios.Model.Oficial;
import com.gestaonavios.gestaonavios.Model.Operador;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

import java.util.List;

public class TripulanteController {

    private final TripulanteBLL tripulanteBLL;
    private final ViagemBLL     viagemBLL;

    public TripulanteController(TripulanteBLL tripulanteBLL, ViagemBLL viagemBLL) {
        this.tripulanteBLL = tripulanteBLL;
        this.viagemBLL     = viagemBLL;
    }

    public List<Tripulante> listarTodos() {
        return tripulanteBLL.listarTodos();
    }

    public List<Tripulante> listarDisponiveis() {
        return tripulanteBLL.listarDisponiveis();
    }

    public Tripulante buscarPorId(int id) {
        return tripulanteBLL.buscarPorId(id);
    }

    public void registar(String nome, String nif, FuncaoTripulante funcao,
                         String nacionalidade, String certificacoes, boolean disponivel) throws Exception {
        Tripulante tripulante = criarTripulante(0, nome, nif, funcao, disponivel, nacionalidade, certificacoes);
        tripulanteBLL.registar(tripulante);
    }

    public void atualizar(Tripulante tripulante) throws Exception {
        tripulanteBLL.atualizar(tripulante);
    }

    public void alterarFuncao(Tripulante atual, FuncaoTripulante novaFuncao) throws Exception {
        Tripulante novo = criarTripulante(
                atual.getId(), atual.getNome(), atual.getNif(),
                novaFuncao, atual.isDisponivel(),
                atual.getNacionalidade(), atual.getCertificacoes());
        tripulanteBLL.atualizar(novo);
    }

    public List<Tripulante> listarPorFuncao(FuncaoTripulante funcao) {
        return tripulanteBLL.listarPorFuncao(funcao);
    }

    public List<Tripulante> pesquisarPorNome(String termo) {
        return tripulanteBLL.pesquisarPorNome(termo);
    }

    public List<Viagem> historicoPorTripulante(int idTripulante) {
        return viagemBLL.listarPorTripulante(idTripulante);
    }

    public void remover(int id) throws Exception {
        tripulanteBLL.remover(id);
    }

    private Tripulante criarTripulante(int id, String nome, String nif,
                                       FuncaoTripulante funcao, boolean disponivel,
                                       String nacionalidade, String certificacoes) {
        if (funcao == FuncaoTripulante.CAPITAO) {
            return new Capitao(id, nome, nif, disponivel, nacionalidade, certificacoes);
        } else if (funcao == FuncaoTripulante.OFICIAL) {
            return new Oficial(id, nome, nif, disponivel, nacionalidade, certificacoes);
        } else if (funcao == FuncaoTripulante.ENGENHEIRO) {
            return new Engenheiro(id, nome, nif, disponivel, nacionalidade, certificacoes);
        } else if (funcao == FuncaoTripulante.OPERADOR) {
            return new Operador(id, nome, nif, disponivel, nacionalidade, certificacoes);
        } else {
            throw new IllegalArgumentException("Função desconhecida: " + funcao);
        }
    }
}
