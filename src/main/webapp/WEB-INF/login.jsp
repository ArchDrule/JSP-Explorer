<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Вход в файловый менеджер</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Вход в систему</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;">${error}</p>
    <% } %>

    <% if (request.getAttribute("message") != null) { %>
        <p style="color:green;">${message}</p>
    <% } %>

    <form method="post">
        <label>Логин: <input type="text" name="login" required/></label><br/><br/>
        <label>Пароль: <input type="password" name="pass" required/></label><br/><br/>
        <input type="submit" value="Войти"/>
    </form>

    <p>Нет аккаунта? <a href="register">Зарегистрироваться</a></p>
</body>
</html>