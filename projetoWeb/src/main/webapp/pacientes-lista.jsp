<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <title>Clínica - Lista de Pacientes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/index.css">
    <style>
        /* Estilos específicos para a tabela dentro do padrão de 700px */
        .pacientes-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
            background-color: #fff;
            font-size: 18px;
        }
        .pacientes-table th, .pacientes-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 2px solid #f0f0f0;
        }
        .pacientes-table th {
            background-color: #008cba;
            color: white;
            font-weight: bold;
        }
        .pacientes-table tr:hover {
            background-color: #f9f9f9;
        }
        .action-links {
            display: flex;
            gap: 10px;
        }
        .btn-mini {
            padding: 8px 12px;
            font-size: 14px;
            margin: 0;
        }
    </style>
</head>
<body>
    <div class="form-container" style="max-width: 900px;"> <h1>Pacientes Cadastrados</h1>

        <form action="${pageContext.request.contextPath}/BuscarPaciente" method="GET" style="width: 100%; margin-bottom: 30px; display: flex; gap: 10px;">
            <input type="text" name="cpf" placeholder="Buscar CPF..." style="flex: 1; padding: 12px; border: 2px solid #ddd; border-radius: 8px; font-size: 16px;">
            <button type="submit" class="button btn-info" style="width: auto; margin: 0; padding: 0 25px;">Buscar</button>
        </form>

        <table class="pacientes-table">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${listaPacientes}">
                    <tr>
                        <td>${p.nome}</td>
                        <td>${p.cpf}</td>
                        <td class="action-links">
                            <a href="EditarPaciente?cpf=${p.cpf}" class="button btn-info btn-mini">Editar</a>
                            <a href="pacientes-remover.jsp?cpf=${p.cpf}" class="button btn-danger btn-mini">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty listaPacientes}">
                    <tr>
                        <td colspan="3" style="text-align: center; color: #888;">Nenhum paciente encontrado.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <a href="index.jsp" class="button btn-neutral">Voltar ao Menu Principal</a>
    </div>
</body>
</html>