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

    public boolean aceita(TipoNavioEnums tipoNavioEnum, TipoCarga tipoCarga) {
        if (tipoNavioEnum == null || tipoCarga == null) return false;
        TipoNavio tipoNavio = tipoNavioDAL.buscarPorDesignacao(tipoNavioEnum.name());
        if (tipoNavio == null) return false;
        List<CompatibilidadeCarga> lista = compatibilidadeCargaDAL.listarPorTipoNavio(tipoNavio.getId());
        for (CompatibilidadeCarga c : lista)
            if (c.getTipoCarga() != null && c.getTipoCarga().getId() == tipoCarga.getId()) return true;
        return false;
    }

    public int maxCargasPorViagem(TipoNavioEnums tipoNavioEnum) {
        if (tipoNavioEnum == null) return 0;
        return tipoNavioEnum.getMaxCargasPorViagem();
    }

    public List<TipoCarga> cargasCompativeis(TipoNavioEnums tipoNavioEnum) {
        if (tipoNavioEnum == null) return List.of();
        TipoNavio tipoNavio = tipoNavioDAL.buscarPorDesignacao(tipoNavioEnum.name());
        if (tipoNavio == null) return List.of();
        List<CompatibilidadeCarga> lista = compatibilidadeCargaDAL.listarPorTipoNavio(tipoNavio.getId());
        List<TipoCarga> resultado = new java.util.ArrayList<>();
        for (CompatibilidadeCarga c : lista)
            if (c.getTipoCarga() != null) resultado.add(c.getTipoCarga());
        return resultado;
    }
}
