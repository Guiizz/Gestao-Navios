package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.ArrayList;
import java.util.List;

public class CargaBLL {

    private final CargaDAL  cargaDAL;
    private final ViagemDAL viagemDAL;

    public CargaBLL(CargaDAL cargaDAL, ViagemDAL viagemDAL) {
        this.cargaDAL  = cargaDAL;
        this.viagemDAL = viagemDAL;
    }

    public List<Carga> listarTodos() {
        return cargaDAL.listarTodos();
    }

    public Carga buscarPorId(int id) {
        return cargaDAL.buscarPorId(id);
    }

    public List<Carga> listarPorTipo(TipoCarga tipo) {
        List<Carga> resultado = new ArrayList<>();
        for (Carga c : cargaDAL.listarTodos())
            if (c.getTipoCarga() != null && c.getTipoCarga().getId() == tipo.getId())
                resultado.add(c);
        return resultado;
    }

    public List<Carga> pesquisarPorNome(String termo) {
        List<Carga> resultado = new ArrayList<>();
        String lower = termo.toLowerCase();
        for (Carga c : cargaDAL.listarTodos())
            if (c.getDesignacao().toLowerCase().contains(lower)) resultado.add(c);
        return resultado;
    }

    public void registar(Carga carga) throws Exception {
        ValidacaoUtils.exigirTexto(carga.getDesignacao(), "A designação da carga");
        ValidacaoUtils.exigirObjeto(carga.getTipoCarga(), "O tipo de carga");
        ValidacaoUtils.exigirPositivo(carga.getVolume(), "O volume");
        ValidacaoUtils.exigirPositivo(carga.getPeso(), "O peso");
        validarPortos(carga);
        cargaDAL.adicionar(carga);
    }

    public void atualizar(Carga carga) throws Exception {
        ValidacaoUtils.exigirExistencia(cargaDAL.buscarPorId(carga.getId()), "Carga", carga.getId());
        ValidacaoUtils.exigirTexto(carga.getDesignacao(), "A designação da carga");
        ValidacaoUtils.exigirObjeto(carga.getTipoCarga(), "O tipo de carga");
        ValidacaoUtils.exigirPositivo(carga.getVolume(), "O volume");
        ValidacaoUtils.exigirPositivo(carga.getPeso(), "O peso");
        validarPortos(carga);
        cargaDAL.atualizar(carga);
    }

    public void remover(int id) throws Exception {
        ValidacaoUtils.exigirExistencia(cargaDAL.buscarPorId(id), "Carga", id);
        if (viagemDAL.cargaEmViagemAtiva(id))
            throw new Exception("Não é possível remover esta carga — está associada a uma viagem ativa.");
        cargaDAL.remover(id);
    }

    private void validarPortos(Carga carga) throws Exception {
        if (carga.getPortoCarga() != null && carga.getPortoDescarga() != null
                && carga.getPortoCarga().getId() == carga.getPortoDescarga().getId())
            throw new Exception("O porto de carga e o porto de descarga não podem ser o mesmo.");
    }
}
