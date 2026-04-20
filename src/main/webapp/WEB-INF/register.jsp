<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Регистрация нового пользователя</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;">${error}</p>
    <% } %>

    <form method="post">
        <label>Логин: <input type="text" name="login" required/></label><br/><br/>
        <label>Пароль: <input type="password" name="pass" required/></label><br/><br/>
        <label>Email: <input type="email" name="email" required/></label><br/><br/>
        <input type="submit" value="Зарегистрироваться"/>
    </form>

    <p>Уже есть аккаунт? <a href="login">Войти</a></p>
</body>
</html>