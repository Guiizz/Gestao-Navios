package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.ArrayList;
import java.util.List;

public class TripulanteBLL {

    private final TripulanteDAL tripulanteDAL;
    private final ViagemDAL viagemDAL;

    public TripulanteBLL(TripulanteDAL tripulanteDAL, ViagemDAL viagemDAL) {
        this.tripulanteDAL = tripulanteDAL;
        this.viagemDAL = viagemDAL;
    }

    public List<Tripulante> listarTodos() {
        return tripulanteDAL.listarTodos();
    }

    public List<Tripulante> listarDisponiveis() {
        return tripulanteDAL.listarDisponiveis();
    }

    public Tripulante buscarPorId(int id) {
        return tripulanteDAL.buscarPorId(id);
    }

    public List<Tripulante> listarPorFuncao(FuncaoTripulante funcao) {
        List<Tripulante> resultado = new ArrayList<>();
        for (Tripulante t : tripulanteDAL.listarTodos())
            if (t.getFuncaoEnum() == funcao) resultado.add(t);
        return resultado;
    }

    public List<Tripulante> pesquisarPorNome(String termo) {
        List<Tripulante> resultado = new ArrayList<>();
        String lower = termo.toLowerCase();
        for (Tripulante t : tripulanteDAL.listarTodos())
            if (t.getNome().toLowerCase().contains(lower)) resultado.add(t);
        return resultado;
    }

    public void registar(Tripulante tripulante) throws Exception {
        ValidacaoUtils.exigirTexto(tripulante.getNome(), "O nome do tripulante");
        ValidacaoUtils.exigirTexto(tripulante.getNif(), "O NIF do tripulante");
        ValidacaoUtils.exigirFormatoNif(tripulante.getNif());
        ValidacaoUtils.exigirTexto(tripulante.getNacionalidade(), "A nacionalidade do tripulante");
        ValidacaoUtils.exigirObjeto(tripulante.getFuncaoEnum(), "A função do tripulante");
        for (Tripulante existente : tripulanteDAL.listarTodos())
            if (existente.getNif().equalsIgnoreCase(tripulante.getNif()))
                throw new Exception("Já existe um tripulante com o NIF '" + tripulante.getNif() + "'.");
        tripulanteDAL.adicionar(tripulante);
    }

    public void atualizar(Tripulante tripulante) throws Exception {
        ValidacaoUtils.exigirExistencia(tripulanteDAL.buscarPorId(tripulante.getId()), "Tripulante", tripulante.getId());
        ValidacaoUtils.exigirTexto(tripulante.getNome(), "O nome do tripulante");
        ValidacaoUtils.exigirTexto(tripulante.getNif(), "O NIF do tripulante");
        ValidacaoUtils.exigirFormatoNif(tripulante.getNif());
        ValidacaoUtils.exigirTexto(tripulante.getNacionalidade(), "A nacionalidade do tripulante");
        ValidacaoUtils.exigirObjeto(tripulante.getFuncaoEnum(), "A função do tripulante");
        for (Tripulante existente : tripulanteDAL.listarTodos())
            if (existente.getId() != tripulante.getId()
                    && existente.getNif().equalsIgnoreCase(tripulante.getNif()))
                throw new Exception("Já existe outro tripulante com o NIF '" + tripulante.getNif() + "'.");
        tripulanteDAL.atualizar(tripulante);
    }

    public void remover(int id) throws Exception {
        ValidacaoUtils.exigirExistencia(tripulanteDAL.buscarPorId(id), "Tripulante", id);
        if (viagemDAL.tripulanteEmViagemAtiva(id))
            throw new Exception("Não é possível remover este tripulante — está associado a uma viagem ativa.");
        if (!viagemDAL.listarPorTripulante(id).isEmpty())
            throw new Exception("Não é possível remover este tripulante porque tem histórico de viagens associado.");
        tripulanteDAL.remover(id);
    }
}
