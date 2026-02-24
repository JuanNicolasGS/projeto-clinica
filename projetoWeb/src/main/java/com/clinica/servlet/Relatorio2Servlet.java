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

@WebServlet("/relatorio2")
public class Relatorio2Servlet extends HttpServlet {

    private double parseDouble(String s, double def) {
        if (s == null || s.trim().isEmpty()) return def;
        return Double.parseDouble(s.trim());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String especialidade = req.getParameter("especialidade");
            double valorMin = parseDouble(req.getParameter("valorMin"), 0);

            ConsultaDAO dao = new ConsultaDAO();
            List<Map<String, Object>> dados =
                    dao.relatorio2ConsultasPorEspecialidadeEValorMin(especialidade, valorMin);

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio2.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 2", e);
        }
    }
}