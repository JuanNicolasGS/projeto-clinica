<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Clínica - Menu Principal</title>
    <link rel="stylesheet" href="css/index.css" />
  </head>
  <body>
    <div class="form-container">
      <h1>Menu Principal</h1>

      <main style="width: 100%">
        <div class="buttons-grid">
          <a href="pacientes-lista.jsp" class="button btn-info">
            Listar Todos os Pacientes
          </a>

          <a href="pacientes-editar.jsp" class="button btn-info">
            Editar Cadastro de Paciente
          </a>

          <a href="paciente-form.jsp" class="button btn-success">
            Adicionar Novo Paciente
          </a>

          <a href="pacientes-remover.jsp" class="button btn-danger">
            Remover Paciente do Sistema
          </a>
        </div>
      </main>
    </div>
  </body>
</html>
