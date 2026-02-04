<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <title>Clínica - Editar Paciente</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/index.css"
    />
  </head>
  <body>
    <div class="form-container">
      <h1>Editar Cadastro</h1>

      <form
        action="${pageContext.request.contextPath}/AtualizarPaciente"
        method="POST"
        style="width: 100%"
      >
        <div class="form-group">
          <label>Nome do Paciente:</label>
          <input
            type="text"
            name="nome"
            value="Nome Atual"
            required
            placeholder="Atualize o nome completo"
          />
        </div>

        <div class="form-group">
          <label>CPF (Identificador único):</label>
          <input
            type="text"
            name="cpf"
            value="000.000.000-00"
            readonly
            style="
              background-color: #eee;
              cursor: not-allowed;
              border-color: #ccc;
            "
          />
        </div>

        <div class="form-group">
          <label>Contatos Telefônicos:</label>
          <div id="telefones-container">
            <div class="telefone-row">
              <input type="text" name="telefones" value="(00) 00000-0000" />
              <button
                type="button"
                class="btn-remove"
                onclick="removerTelefone(this)"
              >
                X
              </button>
            </div>
          </div>
          <button type="button" class="btn-add" onclick="adicionarTelefone()">
            + Adicionar Outro Telefone
          </button>
        </div>

        <button type="submit" class="button btn-success">
          Atualizar Dados
        </button>

        <a href="index.jsp" class="button btn-neutral">Cancelar Alterações</a>
      </form>
    </div>

    <script src="${pageContext.request.contextPath}/js/paciente.js"></script>
  </body>
</html>
