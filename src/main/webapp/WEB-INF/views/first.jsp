<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
 <form action="/held/add" method="post">


     <c:forEach var="co" items="${companies}">
         <tr>
             <td>${co.id}</td>
             <td>${co.name}</td>
             <td>${co.pricePerStock}</td>
         </tr>
     </c:forEach>

 </form>
</body>
</html>
