<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <title>Relatório 4 - Resumo por Profissional</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>
<div class="form-container" style="max-width: 1100px;">
    <h1>Relatório 4</h1>
    <p>GROUP BY + ORDER BY (consulta + profissional)</p>

    <form action="${pageContext.request.contextPath}/relatorio4" method="GET"
          style="display:flex; gap:10px; flex-wrap:wrap; margin-bottom:20px;">
        <input name="especialidade" placeholder="Especialidade (opcional)"
               style="padding:12px;border:2px solid #ddd;border-radius:8px;">
        <input type="date" name="dataIni"
               style="padding:12px;border:2px solid #ddd;border-radius:8px;">
        <input type="date" name="dataFim"
               style="padding:12px;border:2px solid #ddd;border-radius:8px;">
        <button class="button btn-info" type="submit"
                style="width:auto;margin:0;padding:0 25px;">Filtrar</button>
    </form>

    <table style="width:100%; border-collapse:collapse; background:#fff;">
        <thead>
        <tr style="background:#008cba;color:#fff;">
            <th style="padding:12px;">Profissional</th>
            <th style="padding:12px;">Especialidade</th>
            <th style="padding:12px;">Qtd Consultas</th>
            <th style="padding:12px;">Total Gerado</th>
            <th style="padding:12px;">Média Valor</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${dados}">
            <tr>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_profissional}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.especialidade}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.qtd_consultas}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.total_gerado}</td>
                <td style="padding:12px;border-bottom:1px solid #eee;">${r.media_valor}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty dados}">
            <tr><td colspan="5" style="padding:12px;text-align:center;color:#888;">Nenhum resultado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/relatorio4" class="button btn-info" style="margin-top:20px;">Atualizar</a>
    <a href="index.jsp" class="button btn-neutral" style="margin-top:20px;">Voltar ao Menu</a>
</div>
</body>
</html>