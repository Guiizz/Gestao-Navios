package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;
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
class NavioBLLTest {

    @Mock
    NavioDAL navioDAL;
    @Mock
    ViagemDAL viagemDAL;

    NavioBLL navioBLL;

    @BeforeEach
    void setUp() {
        navioBLL = new NavioBLL(navioDAL, viagemDAL);
    }

    private Navio navioValido() {
        return new Navio(0, "Nordic Star", "IMO1234567",
                TipoNavioEnums.PETROLEIRO_CRUDE, 100000.0, 4,
                "Portugal", 2010, EstadoOperacional.ATIVO, null);
    }

    // ---------- registar ----------

    @Test
    void registar_dadosValidos_adicionaAoDAL() throws Exception {
        Navio navio = navioValido();
        when(navioDAL.listarTodos()).thenReturn(Collections.emptyList());

        navioBLL.registar(navio);

        verify(navioDAL).adicionar(navio);
    }

    @Test
    void registar_nomeVazio_lancaExcecao() {
        Navio navio = navioValido();
        navio.setNome("");
        assertThrows(Exception.class, () -> navioBLL.registar(navio));
    }

    @Test
    void registar_imoFormatoInvalido_lancaExcecao() {
        Navio navio = navioValido();
        navio.setCodigoIMO("INVALIDO");
        assertThrows(Exception.class, () -> navioBLL.registar(navio));
    }

    @Test
    void registar_imoDuplicado_lancaExcecao() {
        Navio navio = navioValido();
        Navio existente = new Navio(1, "Outro", "IMO1234567",
                TipoNavioEnums.PETROLEIRO_CRUDE, 50000.0, 2,
                "Espanha", 2005, EstadoOperacional.ATIVO, null);
        when(navioDAL.listarTodos()).thenReturn(List.of(existente));

        assertThrows(Exception.class, () -> navioBLL.registar(navio));
    }

    @Test
    void registar_capacidadeZero_lancaExcecao() {
        Navio navio = navioValido();
        navio.setCapacidadeMaxima(0);
        assertThrows(Exception.class, () -> navioBLL.registar(navio));
    }

    @Test
    void registar_anoFabricoInvalido_lancaExcecao() {
        Navio navio = navioValido();
        navio.setAnoFabrico(1800);
        assertThrows(Exception.class, () -> navioBLL.registar(navio));
    }

    // ---------- remover ----------

    @Test
    void remover_semViagemAtiva_remove() throws Exception {
        Navio navio = navioValido();
        navio.setId(1);
        when(navioDAL.buscarPorId(1)).thenReturn(navio);
        when(viagemDAL.listarPorNavio(1)).thenReturn(Collections.emptyList());

        navioBLL.remover(1);

        verify(navioDAL).remover(1);
    }

    @Test
    void remover_comViagemPlaneada_lancaExcecao() {
        Navio navio = navioValido();
        navio.setId(1);
        Viagem viagem = new Viagem();
        viagem.setEstado(EstadoViagem.PLANEADA);
        when(navioDAL.buscarPorId(1)).thenReturn(navio);
        when(viagemDAL.listarPorNavio(1)).thenReturn(List.of(viagem));

        assertThrows(Exception.class, () -> navioBLL.remover(1));
    }

    @Test
    void remover_navioInexistente_lancaExcecao() {
        when(navioDAL.buscarPorId(99)).thenReturn(null);
        assertThrows(Exception.class, () -> navioBLL.remover(99));
    }

    // ---------- podeIniciarViagem ----------

    @Test
    void podeIniciarViagem_ativoSemViagens_returnaTrue() {
        Navio navio = navioValido();
        navio.setId(1);
        when(viagemDAL.listarPorNavio(1)).thenReturn(Collections.emptyList());

        assertTrue(navioBLL.podeIniciarViagem(navio));
    }

    @Test
    void podeIniciarViagem_inativo_returnaFalse() {
        Navio navio = navioValido();
        navio.setEstadoOperacional(EstadoOperacional.INATIVO);

        assertFalse(navioBLL.podeIniciarViagem(navio));
    }

    @Test
    void podeIniciarViagem_comViagemEmCurso_returnaFalse() {
        Navio navio = navioValido();
        navio.setId(1);
        Viagem viagem = new Viagem();
        viagem.setEstado(EstadoViagem.EM_CURSO);
        when(viagemDAL.listarPorNavio(1)).thenReturn(List.of(viagem));

        assertFalse(navioBLL.podeIniciarViagem(navio));
    }
}
