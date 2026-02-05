function adicionarTelefone() {
  const container = document.getElementById("telefones-container");
  const row = document.createElement("div");
  row.className = "telefone-row";

  row.innerHTML = `
        <input type="text" name="telefones" placeholder="(00) 00000-0000">
        <button type="button" class="btn-remove" onclick="removerTelefone(this)">X</button>
    `;

  container.appendChild(row);
}

function removerTelefone(botao) {
  const container = document.getElementById("telefones-container");
  if (container.children.length > 1) {
    botao.parentElement.remove();
  }
}
