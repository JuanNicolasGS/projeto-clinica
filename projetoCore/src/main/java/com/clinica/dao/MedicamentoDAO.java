package com.clinica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clinica.model.Medicamento;
import com.clinica.util.ConnectionFactory;

public class MedicamentoDAO {
    public List<Medicamento> listar() throws Exception{
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicamento";
        try(Connection c = ConnectionFactory.getConnection(); Statement st = c.createStatement();ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()){
                lista.add(new Medicamento(
                    rs.getInt("id_medicamento"),
                    rs.getString("nome")
                ));
            }
        }
        return lista;
    }   
    
    public boolean salvar(Medicamento medicamento) throws Exception{
        String sql = "INSERT INTO medicamento (nome) VALUES (?)";
        try(Connection c = ConnectionFactory.getConnection();PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, medicamento.getNome());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Medicamento buscar(Integer id) throws Exception{
        String sql = "SELECT * FROM medicamento WHERE id_medicamento=?";
        try(Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Medicamento(rs.getInt("id_medicamento"), rs.getString("nome"));
                }
            }
        }
        return null;
    }

    public boolean remover(Integer id) throws Exception{
        String sql = "DELETE FROM medicamento WHERE id_medicamento =?";
        try(Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean editar(Medicamento medicamento) throws Exception{
        String sql = "UPDATE medicamento SET nome = ? WHERE id_medicamento =?";
        try(Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, medicamento.getNome());
            ps.setInt(2, medicamento.getId());
            ps.execute();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
