package com.gestaonavios.gestaonavios.Controller;

import com.gestaonavios.gestaonavios.BLL.CargaBLL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoCarga;

import java.util.List;

public class CargaController {

    private final CargaBLL cargaBLL;

    public CargaController(CargaBLL cargaBLL) {
        this.cargaBLL = cargaBLL;
    }

    public List<Carga> listarTodos() {
        return cargaBLL.listarTodos();
    }

    public Carga buscarPorId(int id) {
        return cargaBLL.buscarPorId(id);
    }

    public List<Carga> listarPorTipo(TipoCarga tipo) {
        return cargaBLL.listarPorTipo(tipo);
    }

    public List<Carga> pesquisarPorNome(String termo) {
        return cargaBLL.pesquisarPorNome(termo);
    }

    public void registar(String designacao, TipoCarga tipo, double volume, double peso) throws Exception {
        cargaBLL.registar(new Carga(0, designacao, tipo, volume, peso, null, null));
    }

    public void registar(String designacao, TipoCarga tipo, double volume, double peso,
                         Porto portoCarga, Porto portoDescarga) throws Exception {
        cargaBLL.registar(new Carga(0, designacao, tipo, volume, peso, portoCarga, portoDescarga));
    }

    public void atualizar(Carga carga) throws Exception {
        cargaBLL.atualizar(carga);
    }

    public void remover(int id) throws Exception {
        cargaBLL.remover(id);
    }
}
