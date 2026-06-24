package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Capitao;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViagemBLLTest {

    @Mock
    ViagemDAL viagemDAL;
    @Mock
    NavioBLL navioBLL;
    @Mock
    TripulanteBLL tripulanteBLL;
    @Mock
    CompatibilidadeBLL compatibilidadeBLL;

    ViagemBLL viagemBLL;

    @BeforeEach
    void setUp() {
        viagemBLL = new ViagemBLL(viagemDAL, navioBLL, tripulanteBLL, compatibilidadeBLL);
    }

    private Porto porto(int id, String nome) {
        Porto p = new Porto();
        p.setId(id);
        p.setNome(nome);
        return p;
    }

    private Navio navioAtivo() {
        return new Navio(1, "Nordic Star", "IMO1234567",
                TipoNavioEnums.PETROLEIRO_CRUDE, 100000.0, 4,
                "Portugal", 2010, EstadoOperacional.ATIVO, null);
    }

    private Viagem viagemPlaneada() {
        Viagem v = new Viagem(1,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                EstadoViagem.PLANEADA,
                porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navioAtivo());
        return v;
    }

    // ---------- criarViagem ----------

    @Test
    void criarViagem_dadosValidos_adicionaAoDAL() throws Exception {
        Navio navio = navioAtivo();
        Viagem viagem = new Viagem(0,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                null, porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navio);
        when(navioBLL.podeIniciarViagem(navio)).thenReturn(true);

        viagemBLL.criarViagem(viagem);

        verify(viagemDAL).adicionar(viagem);
        assertEquals(EstadoViagem.PLANEADA, viagem.getEstado());
    }

    @Test
    void criarViagem_portosIguais_lancaExcecao() {
        Navio navio = navioAtivo();
        Viagem viagem = new Viagem(0,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                null, porto(1, "Lisboa"), porto(1, "Lisboa"),
                null, navio);

        assertThrows(Exception.class, () -> viagemBLL.criarViagem(viagem));
    }

    @Test
    void criarViagem_dataPartidaNoPassado_lancaExcecao() {
        Navio navio = navioAtivo();
        Viagem viagem = new Viagem(0,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                null, porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navio);

        assertThrows(Exception.class, () -> viagemBLL.criarViagem(viagem));
    }

    @Test
    void criarViagem_chegadaAnteriorPartida_lancaExcecao() {
        Navio navio = navioAtivo();
        Viagem viagem = new Viagem(0,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(3),
                null, porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navio);

        assertThrows(Exception.class, () -> viagemBLL.criarViagem(viagem));
    }

    @Test
    void criarViagem_navioIndisponivel_lancaExcecao() {
        Navio navio = navioAtivo();
        Viagem viagem = new Viagem(0,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                null, porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navio);
        when(navioBLL.podeIniciarViagem(navio)).thenReturn(false);

        assertThrows(Exception.class, () -> viagemBLL.criarViagem(viagem));
    }

    // ---------- avancarEstado ----------

    @Test
    void avancarEstado_planeadaParaEmCurso_atualizaEstado() throws Exception {
        Viagem viagem = viagemPlaneada();
        viagem.getCargas().add(new AtribuicaoCarga(0, new Carga(), 100.0, 100.0, null));
        viagem.getTripulacao().add(new TripulacaoViagem(0,
                new Capitao(1, "Ana", "111111111", false, "Portuguesa", null),
                FuncaoTripulante.CAPITAO, LocalDate.now(), null));
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        viagemBLL.avancarEstado(1, null);

        assertEquals(EstadoViagem.EM_CURSO, viagem.getEstado());
        verify(viagemDAL).atualizar(viagem);
    }

    @Test
    void avancarEstado_planeadaSemCargaOuCapitao_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.avancarEstado(1, null));
        assertEquals(EstadoViagem.PLANEADA, viagem.getEstado());
    }

    @Test
    void avancarEstado_emCursoParaConcluida_atualizaEstadoEPorto() throws Exception {
        Viagem viagem = new Viagem(1,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(2),
                EstadoViagem.EM_CURSO,
                porto(1, "Lisboa"), porto(2, "Rotterdam"),
                null, navioAtivo());
        viagem.setTripulacao(new ArrayList<>());
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        viagemBLL.avancarEstado(1, LocalDate.now());

        assertEquals(EstadoViagem.CONCLUIDA, viagem.getEstado());
        assertNotNull(viagem.getDataChegadaReal());
        verify(navioBLL).atualizar(any());
    }

    @Test
    void avancarEstado_viagemConcluida_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        viagem.setEstado(EstadoViagem.CONCLUIDA);
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.avancarEstado(1, null));
    }

    @Test
    void avancarEstado_viagemCancelada_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        viagem.setEstado(EstadoViagem.CANCELADA);
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.avancarEstado(1, null));
    }

    // ---------- cancelarViagem ----------

    @Test
    void cancelarViagem_planeada_cancelaELibertaTripulantes() throws Exception {
        Viagem viagem = viagemPlaneada();
        viagem.setTripulacao(new ArrayList<>());
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        viagemBLL.cancelarViagem(1);

        assertEquals(EstadoViagem.CANCELADA, viagem.getEstado());
        verify(viagemDAL).atualizar(viagem);
    }

    @Test
    void cancelarViagem_jaConcluida_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        viagem.setEstado(EstadoViagem.CONCLUIDA);
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.cancelarViagem(1));
    }

    @Test
    void cancelarViagem_jaCancelada_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        viagem.setEstado(EstadoViagem.CANCELADA);
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.cancelarViagem(1));
    }

    // ---------- editarViagem ----------

    @Test
    void editarViagem_planeada_atualizaDatas() throws Exception {
        Viagem viagem = viagemPlaneada();
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);
        LocalDate novaPartida = LocalDate.now().plusDays(3);
        LocalDate novaChegada = LocalDate.now().plusDays(15);

        viagemBLL.editarViagem(1, novaPartida, novaChegada, "Observação nova");

        assertEquals(novaPartida, viagem.getDataPartida());
        assertEquals(novaChegada, viagem.getDataChegadaPrevista());
        verify(viagemDAL).atualizar(viagem);
    }

    @Test
    void editarViagem_emCurso_lancaExcecao() {
        Viagem viagem = viagemPlaneada();
        viagem.setEstado(EstadoViagem.EM_CURSO);
        when(viagemDAL.buscarPorId(1)).thenReturn(viagem);

        assertThrows(Exception.class, () -> viagemBLL.editarViagem(1,
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(15), null));
    }

    // ---------- listarPorEstado ----------

    @Test
    void listarPorEstado_returnaApenasDoEstadoCorreto() {
        Viagem v1 = viagemPlaneada();
        Viagem v2 = new Viagem(2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
                EstadoViagem.EM_CURSO, porto(1, "Lisboa"), porto(2, "Porto"), null, navioAtivo());
        when(viagemDAL.listarTodos()).thenReturn(List.of(v1, v2));

        List<Viagem> resultado = viagemBLL.listarPorEstado(EstadoViagem.PLANEADA);

        assertEquals(1, resultado.size());
        assertEquals(EstadoViagem.PLANEADA, resultado.get(0).getEstado());
    }
}
