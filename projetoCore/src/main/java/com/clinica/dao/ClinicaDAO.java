package com.clinica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clinica.model.Clinica;
import com.clinica.util.ConnectionFactory;

public class ClinicaDAO {

    public boolean inserirClinica(Clinica clinica) {
        String sql = "INSERT INTO clinica (nome) VALUES (?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, clinica.getNome());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Clinica> listar() {
        List<Clinica> clinicas = new ArrayList<>();
        String sql = "SELECT id_clinica, nome FROM clinica";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clinica clinica = new Clinica(
                    rs.getInt("id_clinica"),
                    rs.getString("nome")
                );
                clinicas.add(clinica);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clinicas;
    }

    public boolean deletarClinica(Integer idClinica) {
        String sql = "DELETE FROM clinica WHERE id_clinica = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idClinica);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
