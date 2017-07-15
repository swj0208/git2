<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<form id="loginForm" action="login.action" method="post">
	<input type="hidden" name="op" value="login"/>
	用户名：<input type="text" name="name" id="name"/>a<br/>
	密码：<input type="password" name="pwd" id="pwd"/>a<br/>
	<input type="submit" value="登陆"/>
</form>
</body>
</html>
