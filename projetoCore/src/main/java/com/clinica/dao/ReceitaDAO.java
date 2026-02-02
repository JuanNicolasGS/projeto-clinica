package com.clinica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.clinica.model.Receita;
import com.clinica.model.Medicamento;
import com.clinica.util.ConnectionFactory;

public class ReceitaDAO {

    public boolean salvar(Receita r) throws Exception {
        String sqlR = "INSERT INTO receita (data, validade, observacoes, id_consulta) VALUES (?, ?, ?, ?)";
        String sqlRM = "INSERT INTO receita_medicamento (id_receita, id_medicamento) VALUES (?, ?)";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            int idReceita = -1;
            try (PreparedStatement psR = c.prepareStatement(sqlR, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psR.setDate(1, new java.sql.Date(r.getData().getTime()));
                psR.setDate(2, new java.sql.Date(r.getValidade().getTime()));
                psR.setString(3, r.getObservacoes());
                psR.setInt(4, r.getConsulta().getId());
                psR.execute();
                try (ResultSet rs = psR.getGeneratedKeys()) {
                    if (rs.next()) idReceita = rs.getInt(1);
                }
            }
            if (r.getMedicamentos() != null && idReceita != -1) {
                try (PreparedStatement psRM = c.prepareStatement(sqlRM)) {
                    for (Medicamento m : r.getMedicamentos()) {
                        psRM.setInt(1, idReceita);
                        psRM.setInt(2, m.getId());
                        psRM.execute();
                    }
                }
            }
            c.commit();
            return true;
        } catch (Exception e) {
            if (c != null) c.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (c != null) c.close();
        }
    }

    public List<Receita> listar() throws Exception {
        List<Receita> lista = new ArrayList<>();
        String sql = "SELECT * FROM receita ORDER BY data DESC";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receita r = new Receita();
                r.setId(rs.getInt("id_receita"));
                r.setData(rs.getDate("data"));
                r.setValidade(rs.getDate("validade"));
                r.setObservacoes(rs.getString("observacoes"));
                lista.add(r);
            }
        }
        return lista;
    }

    public Receita buscar(Integer id) throws Exception {
        String sqlR = "SELECT * FROM receita WHERE id_receita = ?";
        String sqlM = "SELECT m.* FROM medicamento m JOIN receita_medicamento rm ON m.id_medicamento = rm.id_medicamento WHERE rm.id_receita = ?";
        try (Connection c = ConnectionFactory.getConnection()) {
            Receita r = null;
            try (PreparedStatement psR = c.prepareStatement(sqlR)) {
                psR.setInt(1, id);
                try (ResultSet rsR = psR.executeQuery()) {
                    if (rsR.next()) {
                        r = new Receita();
                        r.setId(rsR.getInt("id_receita"));
                        r.setData(rsR.getDate("data"));
                        r.setValidade(rsR.getDate("validade"));
                        r.setObservacoes(rsR.getString("observacoes"));
                        r.setMedicamentos(new ArrayList<>());
                    }
                }
            }
            if (r != null) {
                try (PreparedStatement psM = c.prepareStatement(sqlM)) {
                    psM.setInt(1, id);
                    try (ResultSet rsM = psM.executeQuery()) {
                        while (rsM.next()) {
                            r.getMedicamentos().add(new Medicamento(rsM.getInt("id_medicamento"), rsM.getString("nome")));
                        }
                    }
                }
            }
            return r;
        }
    }

    public boolean editar(Receita r) throws Exception {
        String sqlR = "UPDATE receita SET data = ?, validade = ?, observacoes = ? WHERE id_receita = ?";
        String sqlDel = "DELETE FROM receita_medicamento WHERE id_receita = ?";
        String sqlIns = "INSERT INTO receita_medicamento (id_receita, id_medicamento) VALUES (?, ?)";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            try (PreparedStatement psR = c.prepareStatement(sqlR)) {
                psR.setDate(1, new java.sql.Date(r.getData().getTime()));
                psR.setDate(2, new java.sql.Date(r.getValidade().getTime()));
                psR.setString(3, r.getObservacoes());
                psR.setInt(4, r.getId());
                psR.execute();
            }
            try (PreparedStatement psDel = c.prepareStatement(sqlDel)) {
                psDel.setInt(1, r.getId());
                psDel.execute();
            }
            if (r.getMedicamentos() != null) {
                try (PreparedStatement psIns = c.prepareStatement(sqlIns)) {
                    for (Medicamento m : r.getMedicamentos()) {
                        psIns.setInt(1, r.getId());
                        psIns.setInt(2, m.getId());
                        psIns.execute();
                    }
                }
            }
            c.commit();
            return true;
        } catch (Exception e) {
            if (c != null) c.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (c != null) c.close();
        }
    }

    public boolean remover(Integer id) throws Exception {
        String sqlRM = "DELETE FROM receita_medicamento WHERE id_receita = ?";
        String sqlR = "DELETE FROM receita WHERE id_receita = ?";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            try (PreparedStatement psRM = c.prepareStatement(sqlRM)) {
                psRM.setInt(1, id);
                psRM.execute();
            }
            try (PreparedStatement psR = c.prepareStatement(sqlR)) {
                psR.setInt(1, id);
                psR.execute();
            }
            c.commit();
            return true;
        } catch (Exception e) {
            if (c != null) c.rollback();
            return false;
        } finally {
            if (c != null) c.close();
        }
    }
}