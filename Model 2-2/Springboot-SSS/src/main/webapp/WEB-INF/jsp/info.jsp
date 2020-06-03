<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改</title>
</head>
<body>
<p>
<p>
<form id="resumeForm"	action="${pageContext.request.contextPath }/demo/save" method="post">
    <input type="hidden" name="id" value="${resume.id }" /> 修改resume信息：
    <table width="100%" border=1>
        <tr>
            <td>name</td>
            <td><input type="text" name="name" value="${resume.name }" /></td>
        </tr>
        <tr>
            <td>address</td>
            <td><input type="text" name="address" value="${resume.address }" /></td>
        </tr>

        <tr>
            <td>phone</td>
            <td><input type="text" name="phone" value="${resume.phone }" /></td>
        </tr>

        <tr>
            <td colspan="2" align="center"><input type="submit" value="提交" />
            </td>
        </tr>
    </table>

</form>
</body>
</html>
