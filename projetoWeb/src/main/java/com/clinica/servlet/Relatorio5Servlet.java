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

@WebServlet("/relatorio5")
public class Relatorio5Servlet extends HttpServlet {

    private boolean parseBool(String s) {
        return s != null && (s.equalsIgnoreCase("true") || s.equals("1") || s.equalsIgnoreCase("on"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String nome = req.getParameter("nome");
            boolean somenteSem = parseBool(req.getParameter("somenteSemConsulta"));

            ConsultaDAO dao = new ConsultaDAO();
            var dados = dao.relatorio5PacientesComOuSemConsulta(nome, somenteSem);

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio5.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 5", e);
        }
    }
}