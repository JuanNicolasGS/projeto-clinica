package com.clinica.dao;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.clinica.model.Paciente;
import com.clinica.util.CPFUtil;
import com.clinica.util.ConnectionFactory;
import com.clinica.util.TelefoneUtil;

public class PacienteDAO {
    public boolean salvar(Paciente paciente) throws Exception{
        String sqlP = "INSERT INTO paciente (cpf, nome, data_nasc) VALUES (?, ?, ?)";
        String sqlT = "INSERT INTO paciente_telefone (cpf, telefone) VALUES (?, ?)";

        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);
            
            try(PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setString(1, paciente.getCpf());
                psP.setString(2, paciente.getNome());
                psP.setDate(3, new java.sql.Date(paciente.getDataNasc().getTime()));
                psP.execute();
            }

            if(paciente.getTelefones() != null) {
                try(PreparedStatement psT = c.prepareStatement(sqlT)) {
                    for(String telefone : paciente.getTelefones()) {
                        psT.setString(1, paciente.getCpf());
                        psT.setString(2, telefone);
                        psT.execute();
                    }
                }
            }

            c.commit();
            return true;
        } catch (Exception e){
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (c != null) c.close();
        }
    }

    public List<Paciente> listar() throws Exception {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.cpf, p.nome, p.data_nasc, " + "(SELECT telefone FROM paciente_telefone WHERE cpf = p.cpf LIMIT 1) as telefone " + "FROM paciente p ORDER BY p.nome";
        try(Connection c = ConnectionFactory.getConnection();PreparedStatement ps = c.prepareStatement(sql);ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setCpf(rs.getString("cpf"));
                p.setNome(rs.getString("nome"));
                p.setDataNasc(rs.getDate("data_nasc"));
                
                String tel = rs.getString("telefone");
                if(tel != null){
                    p.setTelefones(new ArrayList<>());
                    p.getTelefones().add(tel);
                }
                lista.add(p);
            }
        }
        return lista;
    }

    public boolean remover(String cpf) throws Exception {
        String sqlT = "DELETE FROM paciente_telefone WHERE cpf = ?";
        String sqlP = "DELETE FROM paciente WHERE cpf = ?";
        
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement psT = c.prepareStatement(sqlT)) {
                psT.setString(1, cpf);
                psT.execute();
            }

            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setString(1, cpf);
                psP.execute();
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

    public boolean editar(Paciente p) throws Exception {
        String sqlP = "UPDATE paciente SET nome = ?, data_nasc = ? WHERE cpf = ?";
        String sqlDelT = "DELETE FROM paciente_telefone WHERE cpf = ?";
        String sqlInsT = "INSERT INTO paciente_telefone (cpf, telefone) VALUES (?, ?)";

        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setString(1, p.getNome());
                psP.setDate(2, new java.sql.Date(p.getDataNasc().getTime()));
                psP.setString(3, p.getCpf());
                psP.execute();
            }

            try (PreparedStatement psDelT = c.prepareStatement(sqlDelT)) {
                psDelT.setString(1, p.getCpf());
                psDelT.execute();
            }

            if (p.getTelefones() != null) {
                try (PreparedStatement psInsT = c.prepareStatement(sqlInsT)) {
                    for (String tel : p.getTelefones()) {
                        psInsT.setString(1, p.getCpf());
                        psInsT.setString(2, tel);
                        psInsT.execute();
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

    public Paciente buscar(String cpf) throws Exception {
        String sqlP = "SELECT * FROM paciente WHERE cpf = ?";
        String sqlT = "SELECT telefone FROM paciente_telefone WHERE cpf = ?";
        
        try (Connection c = ConnectionFactory.getConnection()) {
            Paciente p = null;

            try (PreparedStatement psP = c.prepareStatement(sqlP)) {
                psP.setString(1, cpf);
                try (ResultSet rsP = psP.executeQuery()) {
                    if (rsP.next()) {
                        p = new Paciente();
                        p.setCpf(rsP.getString("cpf"));
                        p.setNome(rsP.getString("nome"));
                        p.setDataNasc(rsP.getDate("data_nasc"));
                        p.setTelefones(new ArrayList<>());
                    }
                }
            }

            if (p != null) {
                try (PreparedStatement psT = c.prepareStatement(sqlT)) {
                    psT.setString(1, cpf);
                    try (ResultSet rsT = psT.executeQuery()) {
                        while (rsT.next()) {
                            p.getTelefones().add(rsT.getString("telefone"));
                        }
                    }
                }
            }
            return p;
        }
    }

    public int importarCsv(Path arquivo) {
        String sqlP = "INSERT INTO paciente (cpf, nome, data_nasc) VALUES (?, ?, ?) ON CONFLICT (cpf) DO NOTHING";
        String sqlT = "INSERT INTO paciente_telefone (cpf, telefone) VALUES (?, ?)";

        int inseridos = 0;

        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
            c.setAutoCommit(false);

            try (BufferedReader br = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8);
                 PreparedStatement psP = c.prepareStatement(sqlP);
                 PreparedStatement psT = c.prepareStatement(sqlT)) {

                String header = br.readLine();
                if (header == null) return 0;

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.isBlank()) continue;

                    String[] col = linha.split(";", -1);

                    String cpf = col.length > 0 ? col[0].trim().replaceAll("\\D", "") : "";
                    String nome = col.length > 1 ? col[1].trim() : "";
                    String dataTxt = col.length > 2 ? col[2].trim() : "";
                    String telsTxt = col.length > 3 ? col[3].trim() : "";

                    if (!CPFUtil.isValido(cpf)) continue;
                    if (nome.isBlank()) continue;

                    java.sql.Date data;
                    try {
                        data = java.sql.Date.valueOf(LocalDate.parse(dataTxt, fmt));
                    } catch (Exception e) {
                        continue;
                    }

                    psP.setString(1, cpf);
                    psP.setString(2, nome);
                    psP.setDate(3, data);
                    psP.addBatch();

                    if (!telsTxt.isBlank()) {
                        String[] tels = telsTxt.split("\\|");
                        for (String t : tels) {
                            String telRaw = t == null ? "" : t.trim();
                            if (!TelefoneUtil.isValido(telRaw)) continue;
                            String tel = telRaw.replaceAll("\\D", "");

                            psT.setString(1, cpf);
                            psT.setString(2, tel);
                            psT.addBatch();
                        }
                    }

                    inseridos++;

                    if (inseridos % 500 == 0) {
                        psP.executeBatch();
                        psT.executeBatch();
                    }
                }

                psP.executeBatch();
                psT.executeBatch();
                c.commit();
                return inseridos;
            }
        } catch (Exception e) {
            if (c != null) {
                try { c.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try { c.close(); } catch (SQLException ignored) {}
            }
        }
    }


}
