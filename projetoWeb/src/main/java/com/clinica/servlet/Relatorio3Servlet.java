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

@WebServlet("/relatorio3")
public class Relatorio3Servlet extends HttpServlet {

    private java.sql.Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return java.sql.Date.valueOf(s.trim());
    }

    private Integer parseIntOrNull(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return Integer.parseInt(s.trim());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            java.sql.Date ini = parseDate(req.getParameter("dataIni"));
            java.sql.Date fim = parseDate(req.getParameter("dataFim"));
            Integer idClinica = parseIntOrNull(req.getParameter("idClinica"));

            ConsultaDAO dao = new ConsultaDAO();
            var dados = dao.relatorio3ResumoPorClinica(ini, fim, idClinica);

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio3.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 3", e);
        }
    }
}
