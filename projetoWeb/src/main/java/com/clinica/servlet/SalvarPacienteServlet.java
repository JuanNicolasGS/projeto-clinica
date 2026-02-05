package com.clinica.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SalvarPaciente")
public class SalvarPacienteServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String nome = req.getParameter("nome");
            String cpf = req.getParameter("cpf");
            String dataStr = req.getParameter("dataNascimento");
            java.sql.Date dataNasc = java.sql.Date.valueOf(dataStr);

            String[] telefones = req.getParameterValues("telefones");

            Paciente paciente = new Paciente();
            paciente.setNome(nome);
            paciente.setCpf(cpf);
            paciente.setDataNasc(dataNasc);
            List<String> telefonesArray = new ArrayList<>();
            if(telefones != null){
                for (int i = 0; i < telefones.length; i++) {
                    telefonesArray.add(telefones[i]);
                }
                paciente.setTelefones(telefonesArray);
            }


            PacienteDAO pacienteDAO = new PacienteDAO();
            pacienteDAO.salvar(paciente);

            resp.sendRedirect("pacientes-lista.jsp");

        } catch (Exception e){
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao salvar paciente.");
        }
    }
    
}
