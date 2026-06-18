package com.gestaonavios.gestaonavios.Controller;

import com.gestaonavios.gestaonavios.BLL.NavioBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TipoNavioBLL;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;

import java.util.List;

public class NavioController {

    private final NavioBLL    navioBLL;
    private final PortoBLL    portoBLL;
    private final TipoNavioBLL tipoNavioBLL;

    public NavioController(NavioBLL navioBLL, PortoBLL portoBLL, TipoNavioBLL tipoNavioBLL) {
        this.navioBLL    = navioBLL;
        this.portoBLL    = portoBLL;
        this.tipoNavioBLL = tipoNavioBLL;
    }

    public List<Navio> listarTodos() {
        return navioBLL.listarTodos();
    }

    public List<Porto> listarPortos() {
        return portoBLL.listarTodos();
    }

    public Navio buscarPorId(int id) {
        return navioBLL.buscarPorId(id);
    }

    public List<Navio> listarPorEstado(EstadoOperacional estado) {
        return navioBLL.listarPorEstado(estado);
    }

    public List<Navio> pesquisarPorNome(String termo) {
        return navioBLL.pesquisarPorNome(termo);
    }

    public void registar(String nome, String imo, TipoNavioEnums tipo,
                         double capacidade, int tanques, String bandeira,
                         int ano, EstadoOperacional estado, Porto porto) throws Exception {
        navioBLL.registar(new Navio(0, nome, imo, tipo, capacidade, tanques, bandeira, ano, estado, porto));
    }

    public void atualizar(Navio navio) throws Exception {
        navioBLL.atualizar(navio);
    }

    public void remover(int id) throws Exception {
        navioBLL.remover(id);
    }
}
