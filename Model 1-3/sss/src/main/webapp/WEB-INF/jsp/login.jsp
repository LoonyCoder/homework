<%--
  Created by IntelliJ IDEA.
  User: loonycoder
  Date: 2020年04月17日
  Time: 10:35:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>
<div align="center">

    <form action="/sss/demo/login" method="post">
        <span>用户名：</span><input name="username"/>
        <br>
        <span>密码：</span><input name="password"/>
        <br>
        <input type="submit" value="登录"/>
    </form>
</div>
</body>
</html>
