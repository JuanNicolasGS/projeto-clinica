package com.clinica.dao;

import com.clinica.model.Clinica;
import com.clinica.model.Consulta;
import com.clinica.model.Paciente;
import com.clinica.model.Profissional;
import com.clinica.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultaDAO {

    public boolean inserirConsulta(Consulta consulta) {
        try {
            return inserirConsultaRetornandoId(consulta) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer inserirConsultaRetornandoId(Consulta consulta) throws SQLException {
        String sql = """
            INSERT INTO consulta
            (cpf_paciente, id_profissional, id_clinica, data, valor, observacoes)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, consulta.getPaciente().getCpf());
            ps.setInt(2, consulta.getProfissional().getId());
            ps.setInt(3, consulta.getClinica().getId());
            ps.setDate(4, new java.sql.Date(consulta.getData().getTime()));
            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(consulta.getValor()));
            ps.setString(6, consulta.getObservacoes());
            int rows = ps.executeUpdate();
            if (rows <= 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    public boolean editarConsulta(Consulta consulta) throws SQLException {
        String sql = """
            UPDATE consulta
               SET cpf_paciente = ?,
                   id_profissional = ?,
                   id_clinica = ?,
                   data = ?,
                   valor = ?,
                   observacoes = ?
             WHERE id_consulta = ?
        """;
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, consulta.getPaciente().getCpf());
            ps.setInt(2, consulta.getProfissional().getId());
            ps.setInt(3, consulta.getClinica().getId());
            ps.setDate(4, new java.sql.Date(consulta.getData().getTime()));
            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(consulta.getValor()));
            ps.setString(6, consulta.getObservacoes());
            ps.setInt(7, consulta.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public Consulta buscarPorId(int idConsulta) throws SQLException {
        String sql = """
            SELECT c.id_consulta, c.data, c.valor, c.observacoes,
                   p.cpf AS cpf_paciente, p.nome AS nome_paciente,
                   pr.id_profissional, pr.nome AS nome_profissional, pr.especialidade, pr.registro,
                   cl.id_clinica, cl.nome AS nome_clinica
              FROM consulta c
              JOIN paciente p ON p.cpf = c.cpf_paciente
              JOIN profissional pr ON pr.id_profissional = c.id_profissional
              JOIN clinica cl ON cl.id_clinica = c.id_clinica
             WHERE c.id_consulta = ?
        """;
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapConsultaDetalhada(rs);
            }
        }
    }

    public List<Consulta> listarDetalhada() throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        String sql = """
            SELECT c.id_consulta, c.data, c.valor, c.observacoes,
                   p.cpf AS cpf_paciente, p.nome AS nome_paciente,
                   pr.id_profissional, pr.nome AS nome_profissional, pr.especialidade, pr.registro,
                   cl.id_clinica, cl.nome AS nome_clinica
              FROM consulta c
              JOIN paciente p ON p.cpf = c.cpf_paciente
              JOIN profissional pr ON pr.id_profissional = c.id_profissional
              JOIN clinica cl ON cl.id_clinica = c.id_clinica
             ORDER BY c.data DESC, c.id_consulta DESC
        """;
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                consultas.add(mapConsultaDetalhada(rs));
            }
        }
        return consultas;
    }

    private Consulta mapConsultaDetalhada(ResultSet rs) throws SQLException {
        Paciente pac = new Paciente();
        pac.setCpf(rs.getString("cpf_paciente"));
        pac.setNome(rs.getString("nome_paciente"));
        Profissional prof = new Profissional();
        prof.setId(rs.getInt("id_profissional"));
        prof.setNome(rs.getString("nome_profissional"));
        prof.setEspecialidade(rs.getString("especialidade"));
        prof.setRegistro(rs.getString("registro"));
        Clinica clinica = new Clinica(rs.getInt("id_clinica"), rs.getString("nome_clinica"));
        Date data = rs.getDate("data") == null ? null : new Date(rs.getDate("data").getTime());
        double valor = rs.getBigDecimal("valor") == null ? 0.0 : rs.getBigDecimal("valor").doubleValue();
        String obs = rs.getString("observacoes");
        return new Consulta(rs.getInt("id_consulta"), pac, prof, clinica, data, valor, obs);
    }

    public boolean excluirConsulta(int idConsulta) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            return ps.executeUpdate() > 0;
        }
    }
}
