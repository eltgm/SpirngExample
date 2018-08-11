<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<!--FORM-->
<div class="form-style-2">
<form method="post" action="/users">
    <label for="firstName">First name
    <input class="input-field" type="text" id="firstName" name="firstName">
    </label>
    <label for="lastName">Last name
        <input class="input-field" type="text" id="lastName" name="lastName">
    </label>
    <input type="submit" value="Add user">
</form>
</div>
<!--FORM-->

<div class="form-style-2">
<table>
    <tr>
        <th>First name</th>
        <th>Last name</th>
    </tr>
    <c:forEach items="${usersFromServer}" var="user">
        <tr>
            <td>${user.first_name}</td>
            <td>${user.last_name}</td>
        </tr>
    </c:forEach>
</table>
</div>
</body>
</html>