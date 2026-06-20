package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Capitao;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;
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
class TripulanteBLLTest {

    @Mock
    TripulanteDAL tripulanteDAL;
    @Mock
    ViagemDAL viagemDAL;

    TripulanteBLL tripulanteBLL;

    @BeforeEach
    void setUp() {
        tripulanteBLL = new TripulanteBLL(tripulanteDAL, viagemDAL);
    }

    private Capitao capitaoValido() {
        return new Capitao(0, "João Silva", "123456789", true, "Portuguesa", "STCW");
    }

    // ---------- registar ----------

    @Test
    void registar_dadosValidos_adicionaAoDAL() throws Exception {
        Capitao c = capitaoValido();
        when(tripulanteDAL.listarTodos()).thenReturn(Collections.emptyList());

        tripulanteBLL.registar(c);

        verify(tripulanteDAL).adicionar(c);
    }

    @Test
    void registar_nomeVazio_lancaExcecao() {
        Capitao c = capitaoValido();
        c.setNome("");
        assertThrows(Exception.class, () -> tripulanteBLL.registar(c));
    }

    @Test
    void registar_nifCurto_lancaExcecao() {
        Capitao c = capitaoValido();
        c.setNif("12345");
        assertThrows(Exception.class, () -> tripulanteBLL.registar(c));
    }

    @Test
    void registar_nifComLetras_lancaExcecao() {
        Capitao c = capitaoValido();
        c.setNif("12345678A");
        assertThrows(Exception.class, () -> tripulanteBLL.registar(c));
    }

    @Test
    void registar_nifDuplicado_lancaExcecao() {
        Capitao c = capitaoValido();
        Capitao existente = new Capitao(1, "Outro", "123456789", true, "Portuguesa", "");
        when(tripulanteDAL.listarTodos()).thenReturn(List.of(existente));

        assertThrows(Exception.class, () -> tripulanteBLL.registar(c));
    }

    @Test
    void registar_nacionalidadeVazia_lancaExcecao() {
        Capitao c = capitaoValido();
        c.setNacionalidade("");
        assertThrows(Exception.class, () -> tripulanteBLL.registar(c));
    }

    // ---------- remover ----------

    @Test
    void remover_semViagemAtiva_remove() throws Exception {
        Capitao c = capitaoValido();
        c.setId(1);
        when(tripulanteDAL.buscarPorId(1)).thenReturn(c);
        when(viagemDAL.tripulanteEmViagemAtiva(1)).thenReturn(false);

        tripulanteBLL.remover(1);

        verify(tripulanteDAL).remover(1);
    }

    @Test
    void remover_emViagemAtiva_lancaExcecao() {
        Capitao c = capitaoValido();
        c.setId(1);
        when(tripulanteDAL.buscarPorId(1)).thenReturn(c);
        when(viagemDAL.tripulanteEmViagemAtiva(1)).thenReturn(true);

        assertThrows(Exception.class, () -> tripulanteBLL.remover(1));
    }

    @Test
    void remover_tripulanteInexistente_lancaExcecao() {
        when(tripulanteDAL.buscarPorId(99)).thenReturn(null);
        assertThrows(Exception.class, () -> tripulanteBLL.remover(99));
    }

    // ---------- listarPorFuncao ----------

    @Test
    void listarPorFuncao_returnaApenasCorretos() {
        Capitao cap = new Capitao(1, "Capitão A", "111111111", true, "Portuguesa", "");
        Capitao cap2 = new Capitao(2, "Capitão B", "222222222", true, "Espanhola", "");
        when(tripulanteDAL.listarTodos()).thenReturn(List.of(cap, cap2));

        List<Tripulante> resultado = tripulanteBLL.listarPorFuncao(FuncaoTripulante.CAPITAO);

        assertEquals(2, resultado.size());
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_tripulanteExistente_atualizaNoDAL() throws Exception {
        Capitao c = capitaoValido();
        c.setId(1);
        when(tripulanteDAL.buscarPorId(1)).thenReturn(c);
        when(tripulanteDAL.listarTodos()).thenReturn(Collections.emptyList());

        tripulanteBLL.atualizar(c);

        verify(tripulanteDAL).atualizar(c);
    }
}
