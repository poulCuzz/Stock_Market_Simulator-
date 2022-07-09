<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
<form:form method="post" modelAttribute="company">
    <form:input path="name" id="name"/><label for="name">name</label><br/>
    <form:errors path="name"/>
    <form:input path="pricePerStock" id="pricePerStock" placeholder="e.g. 20.75"/><label for="pricePerStock">pricePerStock</label><br/>
    <form:errors path="pricePerStock"/>
    <form:input type="datetime-local" id="datetime" path="dateAndTime"/><label for="datetime">date and time</label><br/>
    <form:errors  path="dateAndTime"/>
    <form:input path="netProfit" id="netProfit"/><label for="netProfit">netProfit</label><br/>
    <form:errors path="netProfit"/>
    <form:input path="equityCapital" id="equityCapital"/><label for="equityCapital">equityCapital</label><br/>
    <form:errors path="equityCapital"/>
    <form:input path="longTermLiabilities" id="longTermLiabilities"/><label for="longTermLiabilities">longTermLiabilities</label><br/>
    <form:errors path="longTermLiabilities"/>
    <form:input path="shortTermLiabilities" id="shortTermLiabilities"/><label for="shortTermLiabilities">shortTermLiabilities</label><br/>
    <form:errors path="shortTermLiabilities"/>
    <form:input path="assetsAll" id="assetsAll"/><label for="assetsAll">assetsAll</label><br/>
    <form:errors path="assetsAll"/>
    <form:checkbox path="own" id="own"/><label for="own">are you owner?</label><br/>
    <form:input path="numberOfStocks" type="number" id="numberOfStocks" value="0" min="0" step="1" /><label for="numberOfStocks">How many stocks?</label>
    <input type="submit" value="Save">
</form:form>
</body>
</html>
