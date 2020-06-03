<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>列表</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
</head>
<body>
<hr/>
<br>

    <br>
    <a  style="float: right" href="${pageContext.request.contextPath }/demo/toAdd">增加</a>
    <table width="100%" border=1>
        <tr>
            <td></td>
            <td>id</td>
            <td>address</td>
            <td>name</td>
            <td>phone</td>
        </tr>
        <c:forEach items="${resumeList}" var="item" varStatus="status">
            <tr>
                <td><input type="checkbox" name="ids" value="${item.id }"/></td>
                <td><input type="text" readonly="readonly" name="itemList[${status.index }].address" value="${item.address }"/></td>
                <td><input type="text" readonly="readonly" name="itemList[${status.index }].name" value="${item.name }"/></td>
                <td><input type="text" readonly="readonly" name="itemList[${status.index }].phone" value="${item.phone }"/></td>
                <td><a href="${pageContext.request.contextPath }/demo/toEdit?id=${item.id}">修改</a></td>
                <td><a href="${pageContext.request.contextPath }/demo/delete?id=${item.id}">删除</a></td>
            </tr>
        </c:forEach>

    </table>


</body>

</html>
