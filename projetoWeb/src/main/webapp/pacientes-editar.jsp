<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Clínica - Editar Paciente</title>

  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/css/index.css" />
</head>

<body>
<div class="form-container">
  <h1>Editar Cadastro</h1>

  <!-- Mensagens (pode setar req.setAttribute("erro", "...") / ("sucesso", "...") no servlet -->
  <c:if test="${not empty erro}">
    <div class="alert alert-error">${erro}</div>
  </c:if>

  <c:if test="${not empty sucesso}">
    <div class="alert alert-success">${sucesso}</div>
  </c:if>

  <!-- Se paciente não existir no request -->
  <c:if test="${empty paciente}">
    <div class="alert alert-error">
      Paciente não carregado. Volte e tente novamente.
    </div>
    <a href="${pageContext.request.contextPath}/BuscarPaciente" class="button btn-neutral">
      Voltar
    </a>
  </c:if>

  <c:if test="${not empty paciente}">
    <form action="${pageContext.request.contextPath}/EditarPaciente"
          method="POST"
          style="width: 100%">

      <!-- CPF deve ir SEMPRE no POST como hidden (PK) -->
      <input type="hidden" name="cpf" value="${paciente.cpf}" />

      <div class="form-group">
        <label>Nome do Paciente:</label>
        <input type="text" name="nome" value="${paciente.nome}" required />
      </div>

      <div class="form-group">
        <label>CPF (Não editável):</label>
        <!-- readonly só para visual; o valor "real" vai no hidden -->
        <input type="text"
               value="${paciente.cpf}"
               readonly
               style="background-color: #eee; cursor: not-allowed" />

      </div>

      <div class="form-group">
        <label>Data de Nascimento:</label>

        <!--
            INPUT DATE exige yyyy-MM-dd.
            Se paciente.dataNasc for java.sql.Date, ${paciente.dataNasc} normalmente já vira yyyy-MM-dd.
            Se vier em outro formato, o safe fallback abaixo evita quebrar.
          -->
        <input type="date" name="dataNascimento" value="${dataNascISO}" required />

      </div>

      <div class="form-group">
        <label>Contatos Telefônicos:</label>

        <div id="telefones-container">
          <c:choose>

            <c:when test="${not empty paciente.telefones}">
              <c:forEach var="fone" items="${paciente.telefones}">
                <div class="telefone-row">
                  <input type="text" name="telefones" value="${fone}" placeholder="(00) 00000-0000"/>
                  <button type="button" class="btn-remove" onclick="removerTelefone(this)">X</button>
                </div>
              </c:forEach>
            </c:when>

            <c:otherwise>
              <div class="telefone-row">
                <input type="text" name="telefones" placeholder="(00) 00000-0000"/>
                <button type="button" class="btn-remove" onclick="removerTelefone(this)">X</button>
              </div>
            </c:otherwise>

          </c:choose>
        </div>

        <button type="button" class="btn-add" onclick="adicionarTelefone()">
          + Adicionar Outro Telefone
        </button>
      </div>

      <div class="form-actions">
        <button type="submit" class="button btn-success">Atualizar Dados</button>

        <!-- Melhor voltar pra listagem, não pro index.jsp -->
        <a href="${pageContext.request.contextPath}/BuscarPaciente" class="button btn-neutral">
          Cancelar Alterações
        </a>
      </div>
    </form>
  </c:if>
</div>

<script src="${pageContext.request.contextPath}/js/paciente.js"></script>

<!-- Fallback caso seu paciente.js não tenha essas funções -->
<script>
  if (typeof adicionarTelefone !== "function") {
    function adicionarTelefone() {
      const container = document.getElementById("telefones-container");
      const row = document.createElement("div");
      row.className = "telefone-row";
      row.innerHTML = `
          <input type="text" name="telefones" placeholder="(00) 00000-0000"/>
          <button type="button" class="btn-remove" onclick="removerTelefone(this)">X</button>
        `;
      container.appendChild(row);
    }
  }

  if (typeof removerTelefone !== "function") {
    function removerTelefone(btn) {
      const row = btn.closest(".telefone-row");
      if (!row) return;

      const container = document.getElementById("telefones-container");
      // Se for o último, limpa o campo ao invés de remover tudo
      if (container.querySelectorAll(".telefone-row").length <= 1) {
        const input = row.querySelector("input[name='telefones']");
        if (input) input.value = "";
        return;
      }
      row.remove();
    }
  }
</script>
</body>
</html>
