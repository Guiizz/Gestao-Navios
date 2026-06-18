package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargaBLLTest {

    @Mock CargaDAL cargaDAL;
    @Mock ViagemDAL viagemDAL;

    CargaBLL cargaBLL;

    @BeforeEach
    void setUp() {
        cargaBLL = new CargaBLL(cargaDAL, viagemDAL);
    }

    private Porto porto(int id) {
        Porto p = new Porto();
        p.setId(id);
        return p;
    }

    private TipoCarga tipoCarga(int id, String nome) {
        return new TipoCarga(id, nome, "PETROLEO", true, false, false);
    }

    private Carga cargaValida() {
        return new Carga(0, "Petróleo Bruto", tipoCarga(1, "PETROLEO_BRUTO"),
                500.0, 450.0, porto(1), porto(2));
    }

    // ---------- registar ----------

    @Test
    void registar_dadosValidos_adicionaAoDAL() throws Exception {
        Carga carga = cargaValida();

        cargaBLL.registar(carga);

        verify(cargaDAL).adicionar(carga);
    }

    @Test
    void registar_designacaoVazia_lancaExcecao() {
        Carga carga = cargaValida();
        carga.setDesignacao("");
        assertThrows(Exception.class, () -> cargaBLL.registar(carga));
    }

    @Test
    void registar_volumeNegativo_lancaExcecao() {
        Carga carga = cargaValida();
        carga.setVolume(-1);
        assertThrows(Exception.class, () -> cargaBLL.registar(carga));
    }

    @Test
    void registar_pesoZero_lancaExcecao() {
        Carga carga = cargaValida();
        carga.setPeso(0);
        assertThrows(Exception.class, () -> cargaBLL.registar(carga));
    }

    @Test
    void registar_portosIguais_lancaExcecao() {
        Carga carga = new Carga(0, "Diesel", tipoCarga(2, "DIESEL_GASOLEO"),
                100.0, 90.0, porto(1), porto(1));
        assertThrows(Exception.class, () -> cargaBLL.registar(carga));
    }

    @Test
    void registar_tipoCargaNulo_lancaExcecao() {
        Carga carga = new Carga(0, "Diesel", null, 100.0, 90.0, porto(1), porto(2));
        assertThrows(Exception.class, () -> cargaBLL.registar(carga));
    }

    // ---------- remover ----------

    @Test
    void remover_semViagemAtiva_remove() throws Exception {
        Carga carga = cargaValida();
        carga.setId(1);
        when(cargaDAL.buscarPorId(1)).thenReturn(carga);
        when(viagemDAL.cargaEmViagemAtiva(1)).thenReturn(false);

        cargaBLL.remover(1);

        verify(cargaDAL).remover(1);
    }

    @Test
    void remover_emViagemAtiva_lancaExcecao() {
        Carga carga = cargaValida();
        carga.setId(1);
        when(cargaDAL.buscarPorId(1)).thenReturn(carga);
        when(viagemDAL.cargaEmViagemAtiva(1)).thenReturn(true);

        assertThrows(Exception.class, () -> cargaBLL.remover(1));
    }

    @Test
    void remover_cargaInexistente_lancaExcecao() {
        when(cargaDAL.buscarPorId(99)).thenReturn(null);
        assertThrows(Exception.class, () -> cargaBLL.remover(99));
    }

    // ---------- listarPorTipo ----------

    @Test
    void listarPorTipo_returnaApenasDoTipoCorreto() {
        TipoCarga tipoPetroleo = tipoCarga(1, "PETROLEO_BRUTO");
        TipoCarga tipoGasolina = tipoCarga(2, "GASOLINA");
        Carga petroleo = new Carga(1, "Petróleo", tipoPetroleo, 100, 90, porto(1), porto(2));
        Carga gasolina = new Carga(2, "Gasolina", tipoGasolina, 50, 40, porto(1), porto(3));
        when(cargaDAL.listarTodos()).thenReturn(List.of(petroleo, gasolina));

        List<Carga> resultado = cargaBLL.listarPorTipo(tipoPetroleo);

        assertEquals(1, resultado.size());
        assertEquals("Petróleo", resultado.get(0).getDesignacao());
    }

    // ---------- pesquisarPorNome ----------

    @Test
    void pesquisarPorNome_encontraSubstring() {
        Carga c1 = new Carga(1, "Petróleo Bruto", tipoCarga(1, "PETROLEO_BRUTO"), 100, 90, porto(1), porto(2));
        Carga c2 = new Carga(2, "Gasolina Premium", tipoCarga(2, "GASOLINA"), 50, 40, porto(1), porto(3));
        when(cargaDAL.listarTodos()).thenReturn(List.of(c1, c2));

        List<Carga> resultado = cargaBLL.pesquisarPorNome("petról");

        assertEquals(1, resultado.size());
    }

    @Test
    void pesquisarPorNome_semResultados_returnaListaVazia() {
        when(cargaDAL.listarTodos()).thenReturn(Collections.emptyList());

        List<Carga> resultado = cargaBLL.pesquisarPorNome("xyz");

        assertTrue(resultado.isEmpty());
    }
}
