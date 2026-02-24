<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <title>Relatório 6 - Profissionais acima da Média</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>
<div class="form-container" style="max-width: 1100px;">
    <h1>Relatório 6</h1>
    <p>Subconsulta: profissionais com consultas acima da média</p>

    <table style="width:100%; border-collapse:collapse; background:#fff;">
        <thead>
        <tr style="background:#008cba;color:#fff;">
            <th style="padding:12px;">Profissional</th>
            <th style="padding:12px;">Especialidade</th>
            <th style="padding:12px;">Qtd Consultas</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${dados}">
            <tr>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_profissional}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.especialidade}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.qtd_consultas}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty dados}">
            <tr><td colspan="3" style="padding:12px;text-align:center;color:#888;">Nenhum resultado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/relatorio6" class="button btn-info" style="margin-top:20px;">Atualizar</a>
    <a href="index.jsp" class="button btn-neutral" style="margin-top:20px;">Voltar ao Menu</a>
</div>
</body>
</html>