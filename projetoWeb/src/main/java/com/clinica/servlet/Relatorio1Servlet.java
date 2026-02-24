package com.clinica.servlet;

import com.clinica.dao.ConsultaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/relatorio1")
public class Relatorio1Servlet extends HttpServlet {

    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return Date.valueOf(LocalDate.parse(s.trim()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idClinicaStr = req.getParameter("idClinica");
            Date ini = parseDate(req.getParameter("dataIni"));
            Date fim = parseDate(req.getParameter("dataFim"));

            if (idClinicaStr == null || idClinicaStr.isBlank() || ini == null || fim == null) {
                req.setAttribute("erro", "Informe idClinica, dataIni e dataFim.");
                req.getRequestDispatcher("relatorio1.jsp").forward(req, resp);
                return;
            }

            int idClinica = Integer.parseInt(idClinicaStr);
            ConsultaDAO dao = new ConsultaDAO();

            List<Map<String, Object>> dados =
                    dao.relatorio1ConsultasPorClinicaEPeriodo(idClinica, ini, fim);

            req.setAttribute("dados", dados);
            req.getRequestDispatcher("relatorio1.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro no Relatório 1", e);
        }
    }
}