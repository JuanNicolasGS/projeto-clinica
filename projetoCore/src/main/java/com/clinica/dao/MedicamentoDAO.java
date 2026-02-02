package com.clinica.dao;

import java.sql.Connection;
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
    
    
}
