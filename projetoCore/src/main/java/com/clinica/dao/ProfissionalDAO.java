package com.clinica.dao;

import com.clinica.model.Clinica;
import com.clinica.model.Profissional;
import com.clinica.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalDAO {

    public boolean salvar(Profissional profissional) throws SQLException {
        String sql = "INSERT INTO profissional (nome, especialidade, registro, id_clinica) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, profissional.getNome());
            stmt.setString(2, profissional.getEspecialidade());
            stmt.setString(3, profissional.getRegistro());
            stmt.setInt(4, profissional.getClinica().getId());
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        profissional.setId(rs.getInt(1));
                    }
                }
                salvarTelefones(profissional, conn);
                return true;
            }
            return false;
        }
    }

    public List<Profissional> listar() throws SQLException {
        List<Profissional> lista = new ArrayList<>();
        String sql = """
            SELECT p.id_profissional, p.nome, p.especialidade, p.registro, p.id_clinica,
                   c.nome AS nome_clinica
              FROM profissional p
              JOIN clinica c ON c.id_clinica = p.id_clinica
             ORDER BY p.nome
        """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clinica clinica = new Clinica(rs.getInt("id_clinica"), rs.getString("nome_clinica"));
                Profissional p = new Profissional();
                p.setId(rs.getInt("id_profissional"));
                p.setNome(rs.getString("nome"));
                p.setEspecialidade(rs.getString("especialidade"));
                p.setRegistro(rs.getString("registro"));
                p.setClinica(clinica);
                p.setTelefones(buscarTelefones(p.getId(), conn));
                lista.add(p);
            }
        }
        return lista;
    }

    public Profissional buscar(int idProfissional) throws SQLException {
        String sql = """
            SELECT p.id_profissional, p.nome, p.especialidade, p.registro, p.id_clinica,
                   c.nome AS nome_clinica
              FROM profissional p
              JOIN clinica c ON c.id_clinica = p.id_clinica
             WHERE p.id_profissional = ?
        """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfissional);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;
                Clinica clinica = new Clinica(rs.getInt("id_clinica"), rs.getString("nome_clinica"));
                Profissional p = new Profissional();
                p.setId(rs.getInt("id_profissional"));
                p.setNome(rs.getString("nome"));
                p.setEspecialidade(rs.getString("especialidade"));
                p.setRegistro(rs.getString("registro"));
                p.setClinica(clinica);
                p.setTelefones(buscarTelefones(idProfissional, conn));
                return p;
            }
        }
    }

    public boolean editar(Profissional profissional) throws SQLException {
        String sql = "UPDATE profissional SET nome = ?, especialidade = ?, registro = ?, id_clinica = ? WHERE id_profissional = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profissional.getNome());
            stmt.setString(2, profissional.getEspecialidade());
            stmt.setString(3, profissional.getRegistro());
            stmt.setInt(4, profissional.getClinica().getId());
            stmt.setInt(5, profissional.getId());
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                removerTelefones(profissional.getId(), conn);
                salvarTelefones(profissional, conn);
                return true;
            }
            return false;
        }
    }

    public boolean remover(int idProfissional) throws SQLException {
        String sql = "DELETE FROM profissional WHERE id_profissional = ?";
        try (Connection conn = ConnectionFactory.getConnection()) {
            removerTelefones(idProfissional, conn);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idProfissional);
                return stmt.executeUpdate() > 0;
            }
        }
    }

    private void salvarTelefones(Profissional profissional, Connection conn) throws SQLException {
        if (profissional.getTelefones() == null) return;
        String sqlTelefone = "INSERT INTO profissional_telefone (id_profissional, telefone) VALUES (?, ?)";
        try (PreparedStatement stmtTelefone = conn.prepareStatement(sqlTelefone)) {
            for (String tel : profissional.getTelefones()) {
                if (tel == null || tel.isBlank()) continue;
                stmtTelefone.setInt(1, profissional.getId());
                stmtTelefone.setString(2, tel);
                stmtTelefone.addBatch();
            }
            stmtTelefone.executeBatch();
        }
    }

    private List<String> buscarTelefones(int idProfissional, Connection conn) throws SQLException {
        List<String> telefones = new ArrayList<>();
        String sql = "SELECT telefone FROM profissional_telefone WHERE id_profissional = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfissional);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    telefones.add(rs.getString("telefone"));
                }
            }
        }
        return telefones;
    }

    private void removerTelefones(int idProfissional, Connection conn) throws SQLException {
        String sql = "DELETE FROM profissional_telefone WHERE id_profissional = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfissional);
            stmt.executeUpdate();
        }
    }
}
