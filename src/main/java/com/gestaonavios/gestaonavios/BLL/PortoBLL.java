package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.List;

public class PortoBLL {

    private final PortoDAL portoDAL;
    private final NavioDAL navioDAL;
    private final CargaDAL cargaDAL;
    private final ViagemDAL viagemDAL;

    public PortoBLL(PortoDAL portoDAL) {
        this(portoDAL, null, null, null);
    }

    public PortoBLL(PortoDAL portoDAL, NavioDAL navioDAL,
                    CargaDAL cargaDAL, ViagemDAL viagemDAL) {
        this.portoDAL = portoDAL;
        this.navioDAL = navioDAL;
        this.cargaDAL = cargaDAL;
        this.viagemDAL = viagemDAL;
    }

    public List<Porto> listarTodos() {
        return portoDAL.listarTodos();
    }

    public Porto buscarPorId(int id) {
        return portoDAL.buscarPorId(id);
    }

    public void registar(Porto porto) throws Exception {
        porto.setCodigoUNLOCODE(porto.getCodigoUNLOCODE() != null
                ? porto.getCodigoUNLOCODE().toUpperCase().trim() : "");
        ValidacaoUtils.exigirTexto(porto.getNome(), "O nome do porto");
        ValidacaoUtils.exigirTexto(porto.getPais(), "O país do porto");
        ValidacaoUtils.exigirTexto(porto.getCodigoUNLOCODE(), "O código UNLOCODE");
        ValidacaoUtils.exigirFormatoUnlocode(porto.getCodigoUNLOCODE());
        for (Porto existente : portoDAL.listarTodos())
            if (existente.getCodigoUNLOCODE().equalsIgnoreCase(porto.getCodigoUNLOCODE()))
                throw new Exception("Já existe um porto com o código UNLOCODE '"
                        + porto.getCodigoUNLOCODE() + "'.");
        portoDAL.adicionar(porto);
    }

    public void atualizar(Porto porto) throws Exception {
        porto.setCodigoUNLOCODE(porto.getCodigoUNLOCODE() != null
                ? porto.getCodigoUNLOCODE().toUpperCase().trim() : "");
        ValidacaoUtils.exigirExistencia(portoDAL.buscarPorId(porto.getId()), "Porto", porto.getId());
        ValidacaoUtils.exigirTexto(porto.getNome(), "O nome do porto");
        ValidacaoUtils.exigirTexto(porto.getPais(), "O país do porto");
        ValidacaoUtils.exigirTexto(porto.getCodigoUNLOCODE(), "O código UNLOCODE");
        ValidacaoUtils.exigirFormatoUnlocode(porto.getCodigoUNLOCODE());
        for (Porto existente : portoDAL.listarTodos())
            if (existente.getId() != porto.getId()
                    && existente.getCodigoUNLOCODE().equalsIgnoreCase(porto.getCodigoUNLOCODE()))
                throw new Exception("Já existe outro porto com o código UNLOCODE '"
                        + porto.getCodigoUNLOCODE() + "'.");
        portoDAL.atualizar(porto);
    }

    public void remover(int id) throws Exception {
        ValidacaoUtils.exigirExistencia(portoDAL.buscarPorId(id), "Porto", id);

        if (navioDAL != null) {
            for (Navio n : navioDAL.listarTodos()) {
                if (n.getPortoAtual() != null && n.getPortoAtual().getId() == id)
                    throw new Exception("Não é possível remover o porto — é o porto atual do navio '"
                            + n.getNome() + "'.");
            }
        }

        if (viagemDAL != null) {
            for (Viagem v : viagemDAL.listarTodos()) {
                if (v.getOrigem() != null && v.getOrigem().getId() == id)
                    throw new Exception("Não é possível remover o porto — é porto de origem da viagem #"
                            + v.getId() + ".");
                if (v.getDestino() != null && v.getDestino().getId() == id)
                    throw new Exception("Não é possível remover o porto — é porto de destino da viagem #"
                            + v.getId() + ".");
            }
        }

        if (cargaDAL != null) {
            for (Carga c : cargaDAL.listarTodos()) {
                if (c.getPortoCarga() != null && c.getPortoCarga().getId() == id)
                    throw new Exception("Não é possível remover o porto — é porto de carga de '"
                            + c.getDesignacao() + "'.");
                if (c.getPortoDescarga() != null && c.getPortoDescarga().getId() == id)
                    throw new Exception("Não é possível remover o porto — é porto de descarga de '"
                            + c.getDesignacao() + "'.");
            }
        }

        portoDAL.remover(id);
    }
}
