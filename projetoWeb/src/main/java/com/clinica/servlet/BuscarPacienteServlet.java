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

@WebServlet("/BuscarPaciente")
public class BuscarPacienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            String cpf = req.getParameter("cpf");
            PacienteDAO dao = new PacienteDAO();

            if (cpf == null || cpf.trim().isEmpty()) {

                List<Paciente> todos = dao.listar();

                req.setAttribute("listaPacientes", todos);

                req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);

                return;
            }

            
            cpf = cpf.replaceAll("\\D", "");

            if (!CPFUtil.isValido(cpf)) {

                req.setAttribute("erro", "CPF informado é inválido!");

                req.setAttribute("listaPacientes", dao.listar());

                req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);

                return;
            }

            Paciente paciente = dao.buscar(cpf);

            List<Paciente> resultado = new ArrayList<>();

            if (paciente != null) {

                resultado.add(paciente);

            } else {

                req.setAttribute("erro",
                    "Nenhum paciente encontrado com o CPF informado.");
            }

            req.setAttribute("listaPacientes", resultado);

            req.getRequestDispatcher("pacientes-lista.jsp").forward(req, resp);

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                "Erro ao buscar paciente: " + e.getMessage(), e);
        }
    }
}
