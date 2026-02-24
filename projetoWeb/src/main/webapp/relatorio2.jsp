<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <title>Relatório 2 - Por Especialidade e Valor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>
<div class="form-container" style="max-width: 1100px;">
    <h1>Relatório 2</h1>
    <p>INNER JOIN (4 tabelas) + WHERE (especialidade e valor mínimo)</p>

    <form action="${pageContext.request.contextPath}/relatorio2" method="GET"
          style="display:flex; gap:10px; flex-wrap:wrap; margin-bottom:20px;">
        <input name="especialidade" placeholder="Especialidade (ex: cardio)"
               style="padding:12px;border:2px solid #ddd;border-radius:8px;">
        <input name="valorMin" placeholder="Valor mínimo (ex: 100)"
               style="padding:12px;border:2px solid #ddd;border-radius:8px;">
        <button class="button btn-info" type="submit" style="width:auto;margin:0;padding:0 25px;">Buscar</button>
    </form>

    <table style="width:100%; border-collapse:collapse; background:#fff;">
        <thead>
        <tr style="background:#008cba;color:#fff;">
            <th style="padding:12px;">ID</th>
            <th style="padding:12px;">Data</th>
            <th style="padding:12px;">Valor</th>
            <th style="padding:12px;">Paciente</th>
            <th style="padding:12px;">Profissional</th>
            <th style="padding:12px;">Especialidade</th>
            <th style="padding:12px;">Clínica</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${dados}">
            <tr>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.id_consulta}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.data_consulta}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.valor}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_paciente} (${r.cpf_paciente})</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_profissional}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.especialidade}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_clinica}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty dados}">
            <tr><td colspan="7" style="padding:12px;text-align:center;color:#888;">Nenhum resultado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <a href="index.jsp" class="button btn-neutral" style="margin-top:20px;">Voltar ao Menu</a>
</div>
</body>
</html>