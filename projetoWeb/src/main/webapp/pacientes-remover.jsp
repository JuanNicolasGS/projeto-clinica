<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <title>Clínica - Remover Paciente</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/index.css"
    />
  </head>
  <body>
    <div class="form-container">
      <h1>Remover Paciente</h1>

      <div
        style="
          background-color: #fff5f5;
          border-left: 5px solid #f44336;
          padding: 15px;
          margin-bottom: 30px;
          width: 100%;
          box-sizing: border-box;
        "
      >
        <p
          style="
            text-align: center;
            color: #d32f2f;
            font-weight: bold;
            margin: 0;
            font-size: 18px;
          "
        >
          Atenção: Esta ação excluirá todos os dados do paciente
          permanentemente.
        </p>
      </div>

      <form
        action="${pageContext.request.contextPath}/RemoverPaciente"
        method="POST"
        style="width: 100%"
      >
        <div class="form-group">
          <label>Digite o CPF do Paciente:</label>
          <input type="text" name="cpf" required placeholder="000.000.000-00" />
        </div>

        <button type="submit" class="button btn-danger">
          Confirmar Exclusão Definitiva
        </button>

        <a href="index.jsp" class="button btn-neutral">Desistir e Voltar</a>
      </form>
    </div>
  </body>
</html>
