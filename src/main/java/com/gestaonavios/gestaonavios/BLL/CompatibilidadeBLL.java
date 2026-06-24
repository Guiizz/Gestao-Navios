package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.CompatibilidadeCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.Model.CompatibilidadeCarga;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * Regras de compatibilidade navio-carga e limite de cargas por viagem,
 * lidas diretamente da base de dados (tabelas TIPO_NAVIO e COMPATIBILIDADE_CARGA),
 * que são a fonte da verdade. Evita duplicar estas regras em código.
 */
public class CompatibilidadeBLL {

    private final TipoNavioDAL tipoNavioDAL;
    private final CompatibilidadeCargaDAL compatibilidadeCargaDAL;

    public CompatibilidadeBLL(TipoNavioDAL tipoNavioDAL, CompatibilidadeCargaDAL compatibilidadeCargaDAL) {
        this.tipoNavioDAL = tipoNavioDAL;
        this.compatibilidadeCargaDAL = compatibilidadeCargaDAL;
    }

    /** Resolve o registo de TIPO_NAVIO a partir do enum (designacao == nome do enum). */
    private TipoNavio resolverTipo(TipoNavioEnums tipoNavio) {
        if (tipoNavio == null) return null;
        return tipoNavioDAL.buscarPorDesignacao(tipoNavio.name());
    }

    /** Indica se o tipo de navio aceita o tipo de carga, segundo COMPATIBILIDADE_CARGA. */
    public boolean aceita(TipoNavioEnums tipoNavio, TipoCarga tipoCarga) {
        if (tipoCarga == null) return false;
        TipoNavio tn = resolverTipo(tipoNavio);
        if (tn == null) return false;
        for (CompatibilidadeCarga cc : compatibilidadeCargaDAL.listarPorTipoNavio(tn.getId())) {
            if (cc.getTipoCarga() != null && cc.getTipoCarga().getId() == tipoCarga.getId()) return true;
        }
        return false;
    }

    /** Número máximo de cargas por viagem definido no TIPO_NAVIO. */
    public int maxCargasPorViagem(TipoNavioEnums tipoNavio) {
        TipoNavio tn = resolverTipo(tipoNavio);
        if (tn != null) return tn.getMaxCargasPorViagem();
        return tipoNavio != null ? tipoNavio.getMaxCargasPorViagem() : 0;
    }

    /** Lista dos tipos de carga compatíveis com o tipo de navio. */
    public List<TipoCarga> cargasCompativeis(TipoNavioEnums tipoNavio) {
        List<TipoCarga> out = new ArrayList<>();
        TipoNavio tn = resolverTipo(tipoNavio);
        if (tn == null) return out;
        for (CompatibilidadeCarga cc : compatibilidadeCargaDAL.listarPorTipoNavio(tn.getId())) {
            if (cc.getTipoCarga() != null) out.add(cc.getTipoCarga());
        }
        return out;
    }
}
