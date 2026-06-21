package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.List;

public class TipoNavioBLL {

    private final TipoNavioDAL tipoNavioDAL;

    public TipoNavioBLL(TipoNavioDAL tipoNavioDAL) {
        this.tipoNavioDAL = tipoNavioDAL;
    }

    public List<TipoNavio> listarTodos() {
        return tipoNavioDAL.listarTodos();
    }

    public TipoNavio buscarPorId(int id) {
        return tipoNavioDAL.buscarPorId(id);
    }

    public void registar(TipoNavio tipoNavio) throws Exception {
        ValidacaoUtils.exigirTexto(tipoNavio.getDesignacao(), "A designação do tipo de navio");
        tipoNavioDAL.adicionar(tipoNavio);
    }
}
