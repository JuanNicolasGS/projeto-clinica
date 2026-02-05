package com.clinica.servlet;

import java.io.IOException;
import java.util.List;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/listarPacientes")
public class ListarPacientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            PacienteDAO dao = new PacienteDAO();
            List<Paciente> pacientes = dao.listar();

            req.setAttribute("listaPacientes", pacientes);

            req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Erro ao listar pacientes", e);
        }
    }
    
}
