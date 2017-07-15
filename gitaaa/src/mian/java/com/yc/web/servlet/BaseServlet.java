package com.yc.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//适配器
public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 494757538394312548L;
	protected String charset="utf-8";
	protected String op;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		op = req.getParameter("op");
		super.service(req, resp);
	}

	@Override  //doGet方法处理GET请求方式
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		if( config.getInitParameter("charset")!=null){
			charset=config.getInitParameter("charset");
		}
	}
	
	
	protected void outJsonStr(String jsonstr, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.println(jsonstr);
		out.flush();
		out.close();
	}

	
	
}
