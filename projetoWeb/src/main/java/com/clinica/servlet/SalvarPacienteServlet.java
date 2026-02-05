package com.clinica.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;
import com.clinica.util.CPFUtil;
import com.clinica.util.TelefoneUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SalvarPaciente")
public class SalvarPacienteServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try{
            String nome = req.getParameter("nome");

            String cpf = req.getParameter("cpf");
            cpf = cpf.replaceAll("\\D", "");

            if (!CPFUtil.isValido(cpf)) {
                req.setAttribute("erro", "CPF inválido!");
                req.getRequestDispatcher("/paciente/form.jsp").forward(req, resp);
                return;
            }

            String dataStr = req.getParameter("dataNascimento");
            java.sql.Date dataNasc = java.sql.Date.valueOf(dataStr);

            String[] telefones = req.getParameterValues("telefone");

            List<String> telefonesArray = new ArrayList<>();

            if (telefones != null) {
                for (String tel : telefones) {

                    if (!TelefoneUtil.isValido(tel)) {
                        req.setAttribute("erro", "Telefone inválido: " + tel +". Use formato (11) 99999-8888");

                        req.getRequestDispatcher("/paciente/form.jsp").forward(req, resp);
                        return;
                    }

                    telefonesArray.add(tel);
                }
            }

            Paciente paciente = new Paciente();
            paciente.setNome(nome);
            paciente.setCpf(cpf);
            paciente.setDataNasc(dataNasc);
            paciente.setTelefones(telefonesArray);

            PacienteDAO pacienteDAO = new PacienteDAO();
            boolean ok = pacienteDAO.salvar(paciente);

            if (!ok) {
                throw new Exception("Falha ao salvar no banco");
            }

            resp.sendRedirect("listarPacientes");

        } catch (Exception e){
            e.printStackTrace();
            resp.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Erro ao salvar paciente."
            );
        }
    }
}
