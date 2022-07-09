<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 30.06.2022
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/company/add">
    <input type="submit" value="Add" />
</form>
<table>
    <thead>
    <tr>
        <td><strong>Id</strong></td>
        <td><strong>name</strong><td/>
        <td><strong>pricePerStock(K$)</strong><td/>
        <td><strong>Date and Time</strong><td/>
        <td><strong>netProfit(K$)</strong><td/>
        <td><strong>equityCapital(K$)</strong><td/>
        <td><strong>longTermLiabilities(K$)</strong><td/>
        <td><strong>shortTermLiabilities(K$)</strong><td/>
        <td><strong>assetsAll(K$)</strong><td/>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="cpn" items="${companies}">
        <tr>
            <td>${cpn.id}</td>
            <td>${cpn.name}</td>
            <td>${cpn.pricePerStock}</td>
            <td>${cpn.dateAndTime}</td>
            <td>${cpn.netProfit}</td>
            <td>${cpn.equityCapital}</td>
            <td>${cpn.longTermLiabilities}</td>
            <td>${cpn.shortTermLiabilities}</td>
            <td>${cpn.assetsAll}</td>
            <td>
                <form action="http://localhost:8080/company/del/${cpn.id}">
                    <input class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm" type="submit" value="Remove" />
                </form>
            </td>
            <td>
                <form action="http://localhost:8080/company/edit/${cpn.id}">
                    <input type="submit" value="Edit" />
                </form>
            </td>
            <td>
                <form action="">
                    <input type="submit" value="Show more" />
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
