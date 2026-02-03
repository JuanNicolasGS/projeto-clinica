package com.clinica.dao;

import com.clinica.model.Exame;
import com.clinica.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExameDAO {

    public boolean salvarExame(Exame exame) {

        String sql = """
            INSERT INTO exame
            (cpf_paciente, id_profissional, tipo_exame, prioridade, observacoes)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, exame.getPaciente().getCpf());
            ps.setInt(2, exame.getProfissional().getId());
            ps.setString(3, exame.getNome()); 
            ps.setString(4, exame.getPrioridade());
            ps.setString(5, exame.getObservacoes());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Exame> listarExames() {

        List<Exame> exames = new ArrayList<>();
        String sql = "SELECT * FROM exame";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Exame exame = new Exame(
                    rs.getInt("id_exame"),     
                    null,                     
                    null,                      
                    rs.getString("tipo_exame"),
                    rs.getString("prioridade"),
                    rs.getString("observacoes")
                );

                exames.add(exame);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exames;
    }

    public Exame buscarExamePorId(int idExame) {

        String sql = "SELECT * FROM exame WHERE id_exame = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idExame);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Exame(
                    rs.getInt("id_exame"),
                    null,
                    null,
                    rs.getString("tipo_exame"),
                    rs.getString("prioridade"),
                    rs.getString("observacoes")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean excluir(int idExame) {

        String sql = "DELETE FROM exame WHERE id_exame = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idExame);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
