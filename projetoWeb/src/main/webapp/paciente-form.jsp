<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <title>Clínica - Cadastro de Paciente</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/index.css"
    />
  </head>
  <body>
    <div class="form-container">
      <h1>Cadastro de Paciente</h1>

      <form
        action="${pageContext.request.contextPath}/SalvarPaciente"
        method="POST"
        style="width: 100%"
      >
        <div class="form-group">
          <label>Nome Completo:</label>
          <input
            type="text"
            name="nome"
            required
            placeholder="Digite o nome completo"
          />
        </div>

        <div class="form-group">
          <label>CPF:</label>
          <input type="text" name="cpf" required placeholder="000.000.000-00" />
        </div>

        <div class="form-group">
          <label>Data de Nascimento:</label>
          <input
            type="date"
            name="dataNascimento"
            required
            placeholder="dd/mm/yyyy"
          />
        </div>

        <div class="form-group">
          <label>Contatos Telefônicos:</label>
          <div id="telefones-container">
            <div class="telefone-row">
              <input
                type="text"
                name="telefones"
                placeholder="(11) 99999-8888"
                pattern="\(\d{2}\) \d{4,5}-\d{4}"
                required
              />
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
            + Adicionar Novo Telefone
          </button>
        </div>

        <button type="submit" class="button btn-success">
          Salvar Cadastro
        </button>

        <a href="index.jsp" class="button btn-neutral"
          >Voltar ao Menu Principal</a
        >
      </form>
    </div>

    <script src="${pageContext.request.contextPath}/js/paciente.js"></script>
  </body>
</html>
