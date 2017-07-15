package com.yc.web.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yc.bean.Student;
import com.yc.biz.StudentBiz;
import com.yc.biz.impl.StudentBizImpl;
import com.yc.util.RequestUtil;

@WebServlet("/login.action")
public class LoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	StudentBiz sb = new StudentBizImpl();
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try {
			if("login".equals(op)){
				login(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Student student = RequestUtil.getParemeter(request, Student.class);
		HttpSession session = request.getSession();
		student = sb.login(student);
		if(student!=null){
			response.sendRedirect("success.jsp");
		}
	}
}
