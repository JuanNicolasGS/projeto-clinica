package com.clinica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.clinica.model.Consulta;
import com.clinica.util.ConnectionFactory;;

public class ConsultaDAO {

    public boolean inserirConsulta(Consulta consulta) {

        String sql = """
            INSERT INTO consulta
            (paciente_id, profissional_id, clinica_id, data, valor, observacoes)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, consulta.getPaciente().getCpf());
            ps.setInt(2, consulta.getProfissional().getId());
            ps.setInt(3, consulta.getClinica().getId());
            ps.setDate(4, new java.sql.Date(consulta.getData().getTime()));
            ps.setDouble(5, consulta.getValor());
            ps.setString(6, consulta.getObservacoes());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
             e.printStackTrace();
        }
        return false;
    }

    public List<Consulta> listarConsulta() {

        List<Consulta> consultas = new ArrayList<>();

        String sql = "SELECT * FROM consulta";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Consulta consulta = new Consulta(
                    rs.getInt("id"),
                    null, // paciente
                    null, // profissional
                    null, // clinica
                    new Date(rs.getDate("data").getTime()),
                    rs.getDouble("valor"),
                    rs.getString("observacoes")
                );

                consultas.add(consulta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
    }

    public boolean excluirConsulta(int id) {

        String sql = "DELETE FROM consulta WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
