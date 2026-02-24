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

    // ======= CONSULTAS (PARTE 2 - RELATÓRIOS WEB) =======

    private java.util.Map<String, Object> mapRow(ResultSet rs) throws SQLException {
        java.util.Map<String, Object> row = new java.util.LinkedHashMap<>();
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            String key = md.getColumnLabel(i);
            Object val = rs.getObject(i);
            row.put(key, val);
        }
        return row;
    }

    private java.util.List<java.util.Map<String, Object>> toList(ResultSet rs) throws SQLException {
        java.util.List<java.util.Map<String, Object>> out = new java.util.ArrayList<>();
        while (rs.next()) out.add(mapRow(rs));
        return out;
    }


//     1) INNER JOIN

    public List<java.util.Map<String, Object>> relatorio1ConsultasPorClinicaEPeriodo(
            int idClinica, java.sql.Date dataIni, java.sql.Date dataFim
    ) throws SQLException {

        String sql = """
        SELECT
            c.id_consulta              AS id_consulta,
            c.data                     AS data_consulta,
            c.valor                    AS valor,
            p.cpf                      AS cpf_paciente,
            p.nome                     AS nome_paciente,
            pr.id_profissional         AS id_profissional,
            pr.nome                    AS nome_profissional,
            pr.especialidade           AS especialidade,
            cl.id_clinica              AS id_clinica,
            cl.nome                    AS nome_clinica
        FROM consulta c
        INNER JOIN paciente p    ON p.cpf = c.cpf_paciente
        INNER JOIN profissional pr ON pr.id_profissional = c.id_profissional
        INNER JOIN clinica cl    ON cl.id_clinica = c.id_clinica
        WHERE c.id_clinica = ?
          AND c.data BETWEEN ? AND ?
        ORDER BY c.data DESC, c.id_consulta DESC
    """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClinica);
            ps.setDate(2, dataIni);
            ps.setDate(3, dataFim);
            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

// 2) INNER JOIN (4 tabelas)

    public List<java.util.Map<String, Object>> relatorio2ConsultasPorEspecialidadeEValorMin(
            String especialidade, double valorMin
    ) throws SQLException {

        String sql = """
        SELECT
            c.id_consulta              AS id_consulta,
            c.data                     AS data_consulta,
            c.valor                    AS valor,
            p.cpf                      AS cpf_paciente,
            p.nome                     AS nome_paciente,
            pr.id_profissional         AS id_profissional,
            pr.nome                    AS nome_profissional,
            pr.especialidade           AS especialidade,
            cl.id_clinica              AS id_clinica,
            cl.nome                    AS nome_clinica
        FROM consulta c
        INNER JOIN paciente p      ON p.cpf = c.cpf_paciente
        INNER JOIN profissional pr ON pr.id_profissional = c.id_profissional
        INNER JOIN clinica cl      ON cl.id_clinica = c.id_clinica
        WHERE pr.especialidade ILIKE ?
          AND COALESCE(c.valor, 0) >= ?
        ORDER BY c.data DESC, c.id_consulta DESC
    """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (especialidade == null ? "" : especialidade.trim()) + "%");
            ps.setBigDecimal(2, java.math.BigDecimal.valueOf(valorMin));
            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

//    3) GROUP BY + ORDER BY (2 tabelas)
    public List<java.util.Map<String, Object>> relatorio3ResumoPorClinica(java.sql.Date dataIni, java.sql.Date dataFim, Integer idClinica) throws SQLException {

        String sql = """
            SELECT
                cl.id_clinica AS id_clinica,
                cl.nome      AS nome_clinica,
                COUNT(c.id_consulta) AS qtd_consultas,
                ROUND(COALESCE(SUM(c.valor), 0)::numeric, 2) AS total_faturado,
                ROUND(COALESCE(AVG(c.valor), 0)::numeric, 2) AS ticket_medio
            FROM clinica cl
            INNER JOIN consulta c ON c.id_clinica = cl.id_clinica
            WHERE (? IS NULL OR c.data >= ?)
              AND (? IS NULL OR c.data <= ?)
              AND (? IS NULL OR c.id_clinica = ?)
            GROUP BY cl.id_clinica, cl.nome
            ORDER BY total_faturado DESC, qtd_consultas DESC
        """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, dataIni);
            ps.setDate(2, dataIni);

            ps.setDate(3, dataFim);
            ps.setDate(4, dataFim);

            if (idClinica == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, idClinica);
                ps.setInt(6, idClinica);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

//      4) GROUP BY + ORDER BY (2 tabelas)
    public List<java.util.Map<String, Object>> relatorio4ResumoPorProfissional(
            String especialidade, java.sql.Date dataIni, java.sql.Date dataFim
    ) throws SQLException {

        String sql = """
            SELECT
                pr.id_profissional AS id_profissional,
                pr.nome           AS nome_profissional,
                pr.especialidade  AS especialidade,
                COUNT(c.id_consulta) AS qtd_consultas,
                ROUND(COALESCE(SUM(c.valor), 0)::numeric, 2) AS total_gerado,
                ROUND(COALESCE(AVG(c.valor), 0)::numeric, 2) AS media_valor
            FROM profissional pr
            INNER JOIN consulta c ON c.id_profissional = pr.id_profissional
            WHERE (? IS NULL OR pr.especialidade ILIKE ?)
              AND (? IS NULL OR c.data >= ?)
              AND (? IS NULL OR c.data <= ?)
            GROUP BY pr.id_profissional, pr.nome, pr.especialidade
            ORDER BY qtd_consultas DESC, total_gerado DESC
        """;

        String esp = (especialidade == null || especialidade.isBlank())
                ? null
                : "%" + especialidade.trim() + "%";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (esp == null) {
                ps.setNull(1, java.sql.Types.VARCHAR);
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, esp);
                ps.setString(2, esp);
            }

            ps.setDate(3, dataIni);
            ps.setDate(4, dataIni);

            ps.setDate(5, dataFim);
            ps.setDate(6, dataFim);

            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

//    5) LEFT JOIN (2 tabelas)
    public List<java.util.Map<String, Object>> relatorio5PacientesComOuSemConsulta(String nomeLike, boolean somenteSemConsulta) throws SQLException {

        String sql = """
            SELECT
                p.cpf       AS cpf_paciente,
                p.nome      AS nome_paciente,
                p.data_nasc AS data_nasc,
                c.id_consulta   AS id_consulta,
                c.data          AS data_consulta,
                c.valor         AS valor
            FROM paciente p
            LEFT JOIN consulta c ON c.cpf_paciente = p.cpf
            WHERE (? IS NULL OR p.nome ILIKE ?)
              AND (? = FALSE OR c.id_consulta IS NULL)
            ORDER BY p.nome ASC, c.data DESC NULLS LAST
        """;

        String nome = (nomeLike == null || nomeLike.isBlank())
                ? null
                : "%" + nomeLike.trim() + "%";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nome == null) {
                ps.setNull(1, java.sql.Types.VARCHAR);
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, nome);
                ps.setString(2, nome);
            }

            ps.setBoolean(3, somenteSemConsulta);

            try (ResultSet rs = ps.executeQuery()) {
                return toList(rs);
            }
        }
    }

//    6) SUBCONSULTA

    public List<java.util.Map<String, Object>> relatorio6ProfissionaisAcimaDaMedia() throws SQLException {

        String sql = """
        SELECT
            pr.id_profissional           AS id_profissional,
            pr.nome                      AS nome_profissional,
            pr.especialidade             AS especialidade,
            COUNT(c.id_consulta)         AS qtd_consultas
        FROM profissional pr
        JOIN consulta c ON c.id_profissional = pr.id_profissional
        GROUP BY pr.id_profissional, pr.nome, pr.especialidade
        HAVING COUNT(c.id_consulta) >
            (
                SELECT AVG(cnt)
                FROM (
                    SELECT COUNT(*) AS cnt
                    FROM consulta
                    GROUP BY id_profissional
                ) t
            )
        ORDER BY qtd_consultas DESC
    """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return toList(rs);
        }
    }
}
