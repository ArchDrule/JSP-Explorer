<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session != null && session.getAttribute("user") != null) {
        response.sendRedirect("explorer");
    } else {
        response.sendRedirect("login");
    }
%>