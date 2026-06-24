package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.CompatibilidadeCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.Model.CompatibilidadeCarga;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;

import java.util.List;

public class CompatibilidadeBLL {

    private final TipoNavioDAL tipoNavioDAL;
    private final CompatibilidadeCargaDAL compatibilidadeCargaDAL;

    public CompatibilidadeBLL(TipoNavioDAL tipoNavioDAL, CompatibilidadeCargaDAL compatibilidadeCargaDAL) {
        this.tipoNavioDAL = tipoNavioDAL;
        this.compatibilidadeCargaDAL = compatibilidadeCargaDAL;
    }

    public List<CompatibilidadeCarga> listarTodos() {
        return compatibilidadeCargaDAL.listarTodos();
    }

    // Resolve o TipoNavio (entidade da BD) a partir do enum Java. Um único ponto
    // de tradução enum -> BD; avisa de forma visível se o tipo não existir na BD.
    private TipoNavio resolverTipoNavio(TipoNavioEnums tipoNavioEnum) {
        if (tipoNavioEnum == null) return null;
        TipoNavio tipoNavio = tipoNavioDAL.buscarPorDesignacao(tipoNavioEnum.name());
        if (tipoNavio == null)
            System.err.println("[CompatibilidadeBLL] Tipo de navio '" + tipoNavioEnum.name()
                    + "' não existe na tabela TIPO_NAVIO — verificar coerência entre o enum e a BD.");
        return tipoNavio;
    }

    public boolean aceita(TipoNavioEnums tipoNavioEnum, TipoCarga tipoCarga) {
        if (tipoCarga == null) return false;
        TipoNavio tipoNavio = resolverTipoNavio(tipoNavioEnum);
        if (tipoNavio == null) return false;
        List<CompatibilidadeCarga> lista = compatibilidadeCargaDAL.listarPorTipoNavio(tipoNavio.getId());
        for (CompatibilidadeCarga c : lista)
            if (c.getTipoCarga() != null && c.getTipoCarga().getId() == tipoCarga.getId()) return true;
        return false;
    }

    // O limite de cargas por viagem é lido da BD (fonte da verdade); o enum
    // serve apenas de recurso se o tipo ainda não existir na tabela.
    public int maxCargasPorViagem(TipoNavioEnums tipoNavioEnum) {
        if (tipoNavioEnum == null) return 0;
        TipoNavio tipoNavio = resolverTipoNavio(tipoNavioEnum);
        if (tipoNavio != null) return tipoNavio.getMaxCargasPorViagem();
        return tipoNavioEnum.getMaxCargasPorViagem();
    }

    public List<TipoCarga> cargasCompativeis(TipoNavioEnums tipoNavioEnum) {
        TipoNavio tipoNavio = resolverTipoNavio(tipoNavioEnum);
        if (tipoNavio == null) return List.of();
        List<CompatibilidadeCarga> lista = compatibilidadeCargaDAL.listarPorTipoNavio(tipoNavio.getId());
        List<TipoCarga> resultado = new java.util.ArrayList<>();
        for (CompatibilidadeCarga c : lista)
            if (c.getTipoCarga() != null) resultado.add(c.getTipoCarga());
        return resultado;
    }
}
