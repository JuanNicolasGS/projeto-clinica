package com.clinica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.clinica.model.Profissional;
import com.clinica.util.ConnectionFactory;

public class ProfissionalDAO {

    public boolean salvar(Profissional p) throws Exception {
        String sqlP = "INSERT INTO profissional (nome, especialidade, registro, id_clinica) VALUES (?, ?, ?, ?)";
        String sqlT = "INSERT INTO profissional_telefone (id_profissional, telefone) VALUES (?, ?)";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            int idGerado = -1;
            try (PreparedStatement psP = c.prepareStatement(sqlP, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psP.setString(1, p.getNome());
                psP.setString(2, p.getEspecialidade());
                psP.setString(3, p.getRegistro());
                psP.setInt(4, p.getClinica().getId());
                psP.execute();
                try (ResultSet rs = psP.getGeneratedKeys()) {
                    if (rs.next()) idGerado = rs.getInt(1);
                }
            }
            if (p.getTelefones() != null && idGerado != -1) {
                try (PreparedStatement psT = c.prepareStatement(sqlT)) {
                    for (String tel : p.getTelefones()) {
                        psT.setInt(1, idGerado);
                        psT.setString(2, tel);
                        psT.execute();
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

    public List<Profissional> listar() throws Exception {
        List<Profissional> lista = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT telefone FROM profissional_telefone WHERE id_profissional = p.id_profissional LIMIT 1) as telefone FROM profissional p ORDER BY p.nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Profissional p = new Profissional();
                p.setId(rs.getInt("id_profissional"));
                p.setNome(rs.getString("nome"));
                p.setEspecialidade(rs.getString("especialidade"));
                p.setRegistro(rs.getString("registro"));
                String tel = rs.getString("telefone");
                if (tel != null) {
                    List<String> tels = new ArrayList<>();
                    tels.add(tel);
                    p.setTelefones(tels);
                }
                lista.add(p);
            }
        }
        return lista;
    }

    public Profissional buscar(Integer id) throws Exception {
        String sqlP = "SELECT * FROM profissional WHERE id_profissional = ?";
        String sqlT = "SELECT telefone FROM profissional_telefone WHERE id_profissional = ?";
        try (Connection c = ConnectionFactory.getConnection()) {
            Profissional p = null;
            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setInt(1, id);
                try (ResultSet rsP = psP.executeQuery()) {
                    if (rsP.next()) {
                        p = new Profissional();
                        p.setId(rsP.getInt("id_profissional"));
                        p.setNome(rsP.getString("nome"));
                        p.setEspecialidade(rsP.getString("especialidade"));
                        p.setRegistro(rsP.getString("registro"));
                    }
                }
            }
            if (p != null) {
                List<String> tels = new ArrayList<>();
                try (PreparedStatement psT = c.prepareStatement(sqlT)) {
                    psT.setInt(1, id);
                    try (ResultSet rsT = psT.executeQuery()) {
                        while (rsT.next()) {
                            tels.add(rsT.getString("telefone"));
                        }
                    }
                }
                p.setTelefones(tels);
            }
            return p;
        }
    }

    public boolean editar(Profissional p) throws Exception {
        String sqlP = "UPDATE profissional SET nome = ?, especialidade = ?, registro = ?, id_clinica = ? WHERE id_profissional = ?";
        String sqlDelT = "DELETE FROM profissional_telefone WHERE id_profissional = ?";
        String sqlInsT = "INSERT INTO profissional_telefone (id_profissional, telefone) VALUES (?, ?)";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setString(1, p.getNome());
                psP.setString(2, p.getEspecialidade());
                psP.setString(3, p.getRegistro());
                psP.setInt(4, p.getClinica().getId());
                psP.setInt(5, p.getId());
                psP.execute();
            }
            try (PreparedStatement psDel = c.prepareStatement(sqlDelT)) {
                psDel.setInt(1, p.getId());
                psDel.execute();
            }
            if (p.getTelefones() != null) {
                try (PreparedStatement psIns = c.prepareStatement(sqlInsT)) {
                    for (String tel : p.getTelefones()) {
                        psIns.setInt(1, p.getId());
                        psIns.setString(2, tel);
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
        String sqlT = "DELETE FROM profissional_telefone WHERE id_profissional = ?";
        String sqlP = "DELETE FROM profissional WHERE id_profissional = ?";
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            try (PreparedStatement psT = c.prepareStatement(sqlT)) {
                psT.setInt(1, id);
                psT.execute();
            }
            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setInt(1, id);
                psP.execute();
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