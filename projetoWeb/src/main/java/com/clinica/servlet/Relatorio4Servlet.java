package com.clinica.servlet;

import com.clinica.dao.ConsultaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/relatorio4")
public class Relatorio4Servlet extends HttpServlet {

    private java.sql.Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return java.sql.Date.valueOf(s.trim());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String especialidade = req.getParameter("especialidade");
            java.sql.Date ini = parseDate(req.getParameter("dataIni"));
            java.sql.Date fim = parseDate(req.getParameter("dataFim"));

            ConsultaDAO dao = new ConsultaDAO();
            var dados = dao.relatorio4ResumoPorProfissional(especialidade, ini, fim);

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio4.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 4", e);
        }
    }
}
