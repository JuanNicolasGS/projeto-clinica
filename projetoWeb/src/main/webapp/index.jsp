<%@ page import="com.clinica.model.Exame" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Clínica - Web</title>
</head>
<body>
    <h1>Sistema da Clínica (Módulo Web)</h1>
    <%
        Exame e = new Exame("Hemograma Completo");
    %>
    <p>Teste de Integração: O objeto <strong><%= e.getNome() %></strong> foi carregado do projetoCore!</p>
</body>
</html>