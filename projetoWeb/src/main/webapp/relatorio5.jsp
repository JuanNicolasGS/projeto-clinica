<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Relatório 5 - Pacientes com ou sem Consulta</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>
<div class="form-container" style="max-width: 1100px;">
  <h1>Relatório 5</h1>
  <p>LEFT JOIN (paciente LEFT consulta)</p>

  <form action="${pageContext.request.contextPath}/relatorio5" method="GET"
        style="display:flex; gap:10px; flex-wrap:wrap; margin-bottom:20px; align-items:center;">
    <input name="nome" placeholder="Nome do paciente (opcional)"
           style="padding:12px;border:2px solid #ddd;border-radius:8px;">
    <label style="display:flex;gap:8px;align-items:center;">
      <input type="checkbox" name="somenteSemConsulta" />
      Somente pacientes sem consulta
    </label>
    <button class="button btn-info" type="submit"
            style="width:auto;margin:0;padding:0 25px;">Filtrar</button>
  </form>

  <table style="width:100%; border-collapse:collapse; background:#fff;">
    <thead>
    <tr style="background:#008cba;color:#fff;">
      <th style="padding:12px;">Paciente</th>
      <th style="padding:12px;">CPF</th>
      <th style="padding:12px;">Nascimento</th>
      <th style="padding:12px;">ID Consulta</th>
      <th style="padding:12px;">Data Consulta</th>
      <th style="padding:12px;">Valor</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="r" items="${dados}">
      <tr>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.nome_paciente}</td>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.cpf_paciente}</td>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.data_nasc}</td>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.id_consulta}</td>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.data_consulta}</td>
        <td style="padding:12px;border-bottom:1px solid #eee;">${r.valor}</td>
      </tr>
    </c:forEach>

    <c:if test="${empty dados}">
      <tr><td colspan="6" style="padding:12px;text-align:center;color:#888;">Nenhum resultado.</td></tr>
    </c:if>
    </tbody>
  </table>

  <a href="${pageContext.request.contextPath}/relatorio5" class="button btn-info" style="margin-top:20px;">Atualizar</a>
  <a href="index.jsp" class="button btn-neutral" style="margin-top:20px;">Voltar ao Menu</a>
</div>
</body>
</html>