package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.Capitao;
import com.gestaonavios.gestaonavios.Model.Engenheiro;
import com.gestaonavios.gestaonavios.Model.Oficial;
import com.gestaonavios.gestaonavios.Model.Operador;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

import java.util.List;

public class TripulanteDAL {

    private static final RowMapper<Tripulante> MAPPER = rs -> {
        int id = rs.getInt("id_tripulante");
        String nome = rs.getString("nome");
        String nif = rs.getString("nif");
        // funcao guardada como nome do enum (ex: CAPITAO)
        FuncaoTripulante funcao = FuncaoTripulante.valueOf(rs.getString("funcao"));
        boolean disponivel = rs.getBoolean("disponivel");
        String nacionalidade = rs.getString("nacionalidade");
        String certificacoes = rs.getString("certificacoes");
        switch (funcao) {
            case CAPITAO:
                return new Capitao(id, nome, nif, disponivel, nacionalidade, certificacoes);
            case OFICIAL:
                return new Oficial(id, nome, nif, disponivel, nacionalidade, certificacoes);
            case ENGENHEIRO:
                return new Engenheiro(id, nome, nif, disponivel, nacionalidade, certificacoes);
            case OPERADOR:
                return new Operador(id, nome, nif, disponivel, nacionalidade, certificacoes);
            default:
                throw new IllegalArgumentException("Função desconhecida: " + funcao);
        }
    };

    public List<Tripulante> listarTodos() {
        return ConnectionManager.select("SELECT * FROM TRIPULANTE", MAPPER);
    }

    public List<Tripulante> listarDisponiveis() {
        return ConnectionManager.select("SELECT * FROM TRIPULANTE WHERE disponivel = 1", MAPPER);
    }

    public Tripulante buscarPorId(int id) {
        List<Tripulante> result = ConnectionManager.select(
                "SELECT * FROM TRIPULANTE WHERE id_tripulante = ?", MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(Tripulante tripulante) {
        ConnectionManager.create(
                "INSERT INTO TRIPULANTE (nome, nif, funcao, disponivel, nacionalidade, certificacoes) VALUES (?, ?, ?, ?, ?, ?)",
                tripulante.getNome(), tripulante.getNif(), tripulante.getFuncaoEnum().name(),
                tripulante.isDisponivel(), tripulante.getNacionalidade(), tripulante.getCertificacoes());
    }

    public boolean atualizar(Tripulante tripulante) {
        ConnectionManager.create(
                "UPDATE TRIPULANTE SET nome=?, nif=?, funcao=?, disponivel=?, nacionalidade=?, certificacoes=? WHERE id_tripulante=?",
                tripulante.getNome(), tripulante.getNif(), tripulante.getFuncaoEnum().name(),
                tripulante.isDisponivel(), tripulante.getNacionalidade(),
                tripulante.getCertificacoes(), tripulante.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM TRIPULANTE WHERE id_tripulante=?", id);
        return true;
    }
}
