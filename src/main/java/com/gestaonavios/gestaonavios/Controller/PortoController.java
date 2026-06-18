package com.gestaonavios.gestaonavios.Controller;

import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.Model.Porto;

import java.util.List;

public class PortoController {

    private final PortoBLL portoBLL;

    public PortoController(PortoBLL portoBLL) {
        this.portoBLL = portoBLL;
    }

    public List<Porto> listarTodos() {
        return portoBLL.listarTodos();
    }

    public Porto buscarPorId(int id) {
        return portoBLL.buscarPorId(id);
    }

    public void registar(String nome, String pais, String locode) throws Exception {
        portoBLL.registar(new Porto(0, nome, pais, locode));
    }

    public void atualizar(Porto porto) throws Exception {
        portoBLL.atualizar(porto);
    }

    public void remover(int id) throws Exception {
        portoBLL.remover(id);
    }
}
