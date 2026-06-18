package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.List;

public class TipoCargaBLL {

    private final TipoCargaDAL tipoCargaDAL;

    public TipoCargaBLL(TipoCargaDAL tipoCargaDAL) {
        this.tipoCargaDAL = tipoCargaDAL;
    }

    public List<TipoCarga> listarTodos() {
        return tipoCargaDAL.listarTodos();
    }

    public TipoCarga buscarPorId(int id) {
        return tipoCargaDAL.buscarPorId(id);
    }

    public void registar(TipoCarga tipoCarga) throws Exception {
        ValidacaoUtils.exigirTexto(tipoCarga.getDesignacao(), "A designação do tipo de carga");
        tipoCargaDAL.adicionar(tipoCarga);
    }
}
