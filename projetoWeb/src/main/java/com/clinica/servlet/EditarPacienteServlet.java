package com.clinica.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;
import com.clinica.util.CPFUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EditarPaciente")
public class EditarPacienteServlet extends HttpServlet {

    private final PacienteDAO dao = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String cpf = normalizeCpf(req.getParameter("cpf"));
            if (cpf == null || !CPFUtil.isValido(cpf)) {
                req.setAttribute("erro", "CPF inválido.");
                req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);
                return;
            }
            Paciente p = dao.buscar(cpf);
            if (p == null) {
                req.setAttribute("erro", "Paciente não encontrado.");
                req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("paciente", p);
            req.setAttribute("dataNascISO", p.getDataNasc().toString());
            req.getRequestDispatcher("pacientes-editar.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao carregar paciente: " + e.getMessage());
            req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String cpf = normalizeCpf(req.getParameter("cpf"));
            String nome = safeTrim(req.getParameter("nome"));
            String dataNasc = safeTrim(req.getParameter("dataNascimento"));
            if (cpf == null || !CPFUtil.isValido(cpf)) {
                req.setAttribute("erro", "CPF inválido.");
                forwardEdit(req, resp, cpf);
                return;
            }
            if (nome == null || nome.isBlank()) {
                req.setAttribute("erro", "Nome é obrigatório.");
                forwardEdit(req, resp, cpf);
                return;
            }
            java.sql.Date dt;
            try {
                dt = java.sql.Date.valueOf(dataNasc);
            } catch (Exception ex) {
                req.setAttribute("erro", "Data de nascimento inválida.");
                forwardEdit(req, resp, cpf);
                return;
            }
            List<String> telefones = normalizeTelefones(req.getParameterValues("telefones"));
            Paciente p = new Paciente();
            p.setCpf(cpf);
            p.setNome(nome);
            p.setDataNasc(dt);
            p.setTelefones(telefones);
            boolean ok = dao.editar(p);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/BuscarPaciente?msg=editado");
            } else {
                req.setAttribute("erro", "Não foi possível editar o paciente.");
                forwardEdit(req, resp, cpf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao editar: " + e.getMessage());
            forwardEdit(req, resp, req.getParameter("cpf"));
        }
    }

    private void forwardEdit(HttpServletRequest req, HttpServletResponse resp, String cpf)
            throws ServletException, IOException {
        try {
            String cpfN = normalizeCpf(cpf);
            if (cpfN != null) {
                Paciente p = dao.buscar(cpfN);
                req.setAttribute("paciente", p);
                if (p != null && p.getDataNasc() != null) {
                    req.setAttribute("dataNascISO", p.getDataNasc().toString());
                }
            }
        } catch (Exception ignored) {}
        req.getRequestDispatcher("paciente-editar.jsp").forward(req, resp);
    }

    private static String normalizeCpf(String cpf) {
        if (cpf == null) return null;
        cpf = cpf.replaceAll("\\D", "");
        return cpf.isBlank() ? null : cpf;
    }

    private static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private static List<String> normalizeTelefones(String[] tels) {
        List<String> out = new ArrayList<>();
        if (tels == null) return out;
        for (String t : tels) {
            if (t == null) continue;
            String tel = t.trim().replaceAll("[^0-9+]", "");
            if (tel.isBlank()) continue;
            if (!out.contains(tel)) out.add(tel);
        }
        return out;
    }
}
