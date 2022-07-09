<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 05.07.2022
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form:form method="post" modelAttribute="user">
    <form:input path="firstName" id="firstName"/><label for="firstName">First Name</label><br/>
    <form:errors path="firstName"/>
    <form:input path="lastName" id="LastName"/><label for="lastName">Last Name</label><br/>
    <form:errors path="lastName"/>
    <form:input id="nick" path="nick"/><label for="nick">Nick</label><br/>
    <form:errors  path="nick"/>
    <form:input type="number" path="age" id="age"/><label for="age">Age</label><br/>
    <form:errors path="age"/>
    <form:input type="password" path="password" id="password"/><label for="password">Password</label><br/>
    <form:errors path="password"/>
    <form:input type="number" path="moneyUsd" id="usd"/><label for="usd">Money for start</label><br/>
    <form:errors path="moneyUsd"/>
    <input class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm" type="submit" value="Register">
</form:form>
</body>
</html>
