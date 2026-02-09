package com.clinica.servlet;

import java.io.IOException;

import com.clinica.dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RemoverPaciente")
public class RemoverPacienteServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cpf = req.getParameter("cpf");
        cpf = cpf.replaceAll("[^0-9]", "");
        try {
            PacienteDAO dao = new PacienteDAO();
            dao.remover(cpf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("listarPacientes");
    }
}
