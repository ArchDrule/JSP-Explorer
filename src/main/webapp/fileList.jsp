<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Comparator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String currentPath = (String) request.getAttribute("currentPath");
    File[] files = (File[]) request.getAttribute("files");
    String parentPath = (String) request.getAttribute("parentPath");
    Date serverTime = (Date) request.getAttribute("serverTime");
    String error = (String) request.getAttribute("error");

    if (files != null && files.length > 0) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1; // f1 папка, f2 файл -> f1 выше
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;  // f1 файл, f2 папка -> f2 выше
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            }
        });
    }
%>
<html>
<head>
    <title>File Explorer</title>
    <meta charset="UTF-8">
</head>
<body>

<h2>Файловый менеджер</h2>

<p><strong>Сгенерировано:</strong> <%= serverTime %></p>

<hr/>

<% if (error != null) { %>
    <p style="color:red;">Ошибка: <%= error %></p>
<% } else { %>

    <p><strong>Текущая директория:</strong> <%= currentPath %></p>

    <% if (parentPath != null) { %>
        <a href="?path=<%= URLEncoder.encode(parentPath, StandardCharsets.UTF_8.name()) %>">⬆ Вверх (на уровень выше)</a>
    <% } else { %>
        <span style="color:gray;">⬆ Вверх (корень)</span>
    <% } %>

    <hr/>

    <ul>
    <% for (File file : files) {
        String encodedPath = URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8.name());
    %>
        <li>
            <% if (file.isDirectory()) { %>
                📁 <a href="?path=<%= encodedPath %>"><%= file.getName() %>/</a>
            <% } else { %>
                📄 <a href="download?file=<%= encodedPath %>"><%= file.getName() %></a>
                (<%= file.length() %> bytes)
            <% } %>
        </li>
    <% } %>
    </ul>

<% } %>

</body>
</html>