package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TanqueDAL;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Tanque;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.List;

/**
 * Lógica de negócio dos tanques (compartimentos) de um navio.
 * Além do CRUD, garante a coerência entre o número de tanques declarado no
 * navio (n_tanques) e os registos individuais da tabela TANQUE, gerando-os
 * automaticamente quando ainda não existem.
 */
public class TanqueBLL {

    private final TanqueDAL tanqueDAL;

    public TanqueBLL(TanqueDAL tanqueDAL) {
        this.tanqueDAL = tanqueDAL;
    }

    public List<Tanque> listarPorNavio(int idNavio) {
        return tanqueDAL.listarPorNavio(idNavio);
    }

    public Tanque buscarPorId(int id) {
        return tanqueDAL.buscarPorId(id);
    }

    public void registar(Tanque tanque) throws Exception {
        ValidacaoUtils.exigirPositivo(tanque.getNumero(), "O número do tanque");
        ValidacaoUtils.exigirPositivo(tanque.getCapacidade(), "A capacidade do tanque");
        for (Tanque existente : tanqueDAL.listarPorNavio(tanque.getIdNavio()))
            if (existente.getNumero() == tanque.getNumero())
                throw new Exception("Já existe um tanque com o número " + tanque.getNumero()
                        + " neste navio.");
        tanqueDAL.adicionar(tanque);
    }

    public void atualizar(Tanque tanque) throws Exception {
        ValidacaoUtils.exigirExistencia(tanqueDAL.buscarPorId(tanque.getId()), "Tanque", tanque.getId());
        ValidacaoUtils.exigirPositivo(tanque.getNumero(), "O número do tanque");
        ValidacaoUtils.exigirPositivo(tanque.getCapacidade(), "A capacidade do tanque");
        tanqueDAL.atualizar(tanque);
    }

    public void remover(int id) throws Exception {
        ValidacaoUtils.exigirExistencia(tanqueDAL.buscarPorId(id), "Tanque", id);
        tanqueDAL.remover(id);
    }

    /**
     * Garante que o navio tem os seus tanques individuais criados na tabela TANQUE.
     * Se ainda não existir nenhum e o navio declarar n_tanques > 0, gera tanques
     * numerados de 1 a n com capacidade igual a (capacidade máxima / nº de tanques).
     * Devolve a lista atual de tanques do navio.
     */
    public List<Tanque> garantirTanques(Navio navio) {
        if (navio == null) return List.of();
        List<Tanque> existentes = tanqueDAL.listarPorNavio(navio.getId());
        if (!existentes.isEmpty() || navio.getNumeroTanques() <= 0) return existentes;

        int n = navio.getNumeroTanques();
        double capacidadePorTanque = navio.getCapacidadeMaxima() / n;
        for (int i = 1; i <= n; i++)
            tanqueDAL.adicionar(new Tanque(0, i, capacidadePorTanque, navio.getId()));
        return tanqueDAL.listarPorNavio(navio.getId());
    }
}
