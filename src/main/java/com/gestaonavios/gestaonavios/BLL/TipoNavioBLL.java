package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Model.enums.TipoCargaEnums;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipoNavioBLL {

    private final TipoNavioDAL tipoNavioDAL;

    private static final List<TipoCargaEnums> CARGAS_CRUDE        = Arrays.asList(TipoCargaEnums.PETROLEO_BRUTO);
    private static final List<TipoCargaEnums> CARGAS_REFINADOS    = Arrays.asList(TipoCargaEnums.GASOLINA, TipoCargaEnums.DIESEL_GASOLEO, TipoCargaEnums.JET_FUEL_QUEROSENE, TipoCargaEnums.FUELOLEO_BETUME);
    private static final List<TipoCargaEnums> CARGAS_QUIMICO      = Arrays.asList(TipoCargaEnums.PRODUTOS_QUIMICOS_LIQUIDOS);
    private static final List<TipoCargaEnums> CARGAS_QUIMICO_PROD = Arrays.asList(TipoCargaEnums.PRODUTOS_QUIMICOS_LIQUIDOS, TipoCargaEnums.GASOLINA, TipoCargaEnums.DIESEL_GASOLEO, TipoCargaEnums.JET_FUEL_QUEROSENE);

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

    public boolean cargaCompativel(TipoNavioEnums tipoNavio, TipoCargaEnums tipoCarga) {
        return getCargasCompativeis(tipoNavio).contains(tipoCarga);
    }

    public List<TipoCargaEnums> getCargasCompativeis(TipoNavioEnums tipoNavio) {
        switch (tipoNavio) {
            case PETROLEIRO_CRUDE:   return CARGAS_CRUDE;
            case PRODUTOS_REFINADOS: return CARGAS_REFINADOS;
            case QUIMICO:            return CARGAS_QUIMICO;
            case QUIMICO_PRODUTOS:   return CARGAS_QUIMICO_PROD;
            default:                 return new ArrayList<>();
        }
    }
}
