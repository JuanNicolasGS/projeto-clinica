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

@WebServlet("/relatorio6")
public class Relatorio6Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            ConsultaDAO dao = new ConsultaDAO();
            List<Map<String, Object>> dados = dao.relatorio6ProfissionaisAcimaDaMedia();

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio6.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 6", e);
        }
    }
}