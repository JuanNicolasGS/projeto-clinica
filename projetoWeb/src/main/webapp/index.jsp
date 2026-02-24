<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Clínica - Menu</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>

  <!-- MENU LATERAL FIXO (DIREITA) -->
  <aside class="sidebar-fixed">
    <h2>Relatórios</h2>
    <p class="sidebar-subtitle">Consultas SQL (Parte 2)</p>

    <nav>
      <a href="${pageContext.request.contextPath}/relatorio1">Relatório 1 — JOIN + WHERE</a>
      <a href="${pageContext.request.contextPath}/relatorio2">Relatório 2 — JOIN + WHERE</a>
      <a href="${pageContext.request.contextPath}/relatorio3">Relatório 3 — GROUP BY</a>
      <a href="${pageContext.request.contextPath}/relatorio4">Relatório 4 — GROUP BY</a>
      <a href="${pageContext.request.contextPath}/relatorio5">Relatório 5 — LEFT JOIN</a>
      <a href="${pageContext.request.contextPath}/relatorio6">Relatório 6 — SUBCONSULTA</a>
    </nav>
  </aside>

  <!-- CONTEÚDO CENTRAL -->
  <main class="main-center">
    <div class="form-container">
      <h1>Menu Principal</h1>

      <a href="pacientes-lista.jsp" class="button btn-info">Listar Todos os Pacientes</a>
      <a href="paciente-editar.jsp" class="button btn-info">Editar Cadastro de Paciente</a>
      <a href="paciente-form.jsp" class="button btn-success">Adicionar Novo Paciente</a>
      <a href="paciente-remover.jsp" class="button btn-danger">Remover Paciente do Sistema</a>
    </div>
  </main>

</body>
</html>