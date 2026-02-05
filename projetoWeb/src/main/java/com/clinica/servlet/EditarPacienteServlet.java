package com.clinica.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/EditarPaciente", "/AtualizarPaciente"})
public class EditarPacienteServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String cpf = req.getParameter("cpf");
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente p = pacienteDAO.buscar(cpf);
            req.setAttribute("paciente", p);
            RequestDispatcher dispatcher = req.getRequestDispatcher("pacientes-editar.jsp");
            dispatcher.forward(req, resp);



        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String cpf = req.getParameter("cpf");
            String nome = req.getParameter("nome");
            String dataStr = req.getParameter("dataNascimento");
            java.sql.Date dataNasc = java.sql.Date.valueOf(dataStr);

            String[] telefones = req.getParameterValues("telefones");

            Paciente paciente = new Paciente();
            paciente.setNome(nome);
            paciente.setCpf(cpf);
            paciente.setDataNasc(dataNasc);
            List<String> telefonesArray = new ArrayList<>();
            if (telefones != null){
                for (int i = 0; i < telefones.length; i++) {
                    telefonesArray.add(telefones[i]);
                }
                paciente.setTelefones(telefonesArray);
            }

            PacienteDAO pacienteDAO = new PacienteDAO();
            pacienteDAO.editar(paciente);

            resp.sendRedirect("pacientes-listar.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar dados.");
        }

        
    }

    
    
}
