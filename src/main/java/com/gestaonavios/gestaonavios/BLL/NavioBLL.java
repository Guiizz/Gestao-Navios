package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.ArrayList;
import java.util.List;

public class NavioBLL {

    private NavioDAL navioDAL;
    private ViagemDAL viagemDAL;

    public NavioBLL(NavioDAL navioDAL, ViagemDAL viagemDAL) {
        this.navioDAL = navioDAL;
        this.viagemDAL = viagemDAL;
    }

    public List<Navio> listarTodos() {
        return navioDAL.listarTodos();
    }

    public Navio buscarPorId(int id) {
        return navioDAL.buscarPorId(id);
    }

    public List<Navio> listarDisponiveis() {
        List<Navio> resultado = new ArrayList<>();
        for (Navio n : navioDAL.listarTodos())
            if (podeIniciarViagem(n)) resultado.add(n);
        return resultado;
    }

    public List<Navio> listarPorEstado(EstadoOperacional estado) {
        return navioDAL.listarPorEstado(estado);
    }

    public List<Navio> pesquisarPorNome(String termo) {
        List<Navio> resultado = new ArrayList<>();
        String lower = termo.toLowerCase();
        for (Navio n : navioDAL.listarTodos())
            if (n.getNome().toLowerCase().contains(lower)) resultado.add(n);
        return resultado;
    }

    public void registar(Navio navio) throws Exception {
        ValidacaoUtils.exigirTexto(navio.getNome(), "O nome do navio");
        ValidacaoUtils.exigirTexto(navio.getCodigoIMO(), "O código IMO");
        ValidacaoUtils.exigirFormatoIMO(navio.getCodigoIMO());
        ValidacaoUtils.exigirTexto(navio.getBandeira(), "A bandeira do navio");
        ValidacaoUtils.exigirPositivo(navio.getCapacidadeMaxima(), "A capacidade máxima");
        ValidacaoUtils.exigirPositivo(navio.getNumeroTanques(), "O número de tanques");
        ValidacaoUtils.exigirAnoFabrico(navio.getAnoFabrico());
        for (Navio existente : navioDAL.listarTodos())
            if (existente.getCodigoIMO().equalsIgnoreCase(navio.getCodigoIMO()))
                throw new Exception("Já existe um navio com o código IMO '" + navio.getCodigoIMO() + "'.");
        navioDAL.adicionar(navio);
    }

    public void atualizar(Navio navio) throws Exception {
        ValidacaoUtils.exigirExistencia(navioDAL.buscarPorId(navio.getId()), "Navio", navio.getId());
        ValidacaoUtils.exigirTexto(navio.getNome(), "O nome do navio");
        ValidacaoUtils.exigirTexto(navio.getCodigoIMO(), "O código IMO");
        ValidacaoUtils.exigirFormatoIMO(navio.getCodigoIMO());
        ValidacaoUtils.exigirTexto(navio.getBandeira(), "A bandeira do navio");
        ValidacaoUtils.exigirPositivo(navio.getCapacidadeMaxima(), "A capacidade máxima");
        ValidacaoUtils.exigirPositivo(navio.getNumeroTanques(), "O número de tanques");
        ValidacaoUtils.exigirAnoFabrico(navio.getAnoFabrico());
        for (Navio existente : navioDAL.listarTodos())
            if (existente.getId() != navio.getId()
                    && existente.getCodigoIMO().equalsIgnoreCase(navio.getCodigoIMO()))
                throw new Exception("Já existe outro navio com o código IMO '" + navio.getCodigoIMO() + "'.");
        navioDAL.atualizar(navio);
    }

    public void remover(int id) throws Exception {
        ValidacaoUtils.exigirExistencia(navioDAL.buscarPorId(id), "Navio", id);
        if (temViagemAtiva(id))
            throw new Exception("Não é possível remover um navio com viagem ativa.");
        if (!viagemDAL.listarPorNavio(id).isEmpty())
            throw new Exception("Não é possível remover este navio porque tem viagens associadas (histórico).");
        navioDAL.remover(id);
    }

    public boolean temViagemAtiva(int idNavio) {
        List<Viagem> viagens = viagemDAL.listarPorNavio(idNavio);
        for (Viagem v : viagens) {
            if (v.getEstado() == EstadoViagem.PLANEADA || v.getEstado() == EstadoViagem.EM_CURSO)
                return true;
        }
        return false;
    }

    public boolean podeIniciarViagem(Navio navio) {
        // Pode agendar-se uma viagem a um navio desde que esteja ATIVO.
        // A não-sobreposição de datas é verificada no ViagemBLL.
        return navio.getEstadoOperacional() == EstadoOperacional.ATIVO;
    }
}
