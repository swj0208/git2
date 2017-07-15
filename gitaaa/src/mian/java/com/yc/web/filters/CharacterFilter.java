package com.yc.web.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
/**
 * tomcat 7 以上才支持的功能
 * @author lenovi
 *
 */
@WebFilter("*.action")
public class CharacterFilter implements Filter {
	private String charset="utf-8";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		chain.doFilter(request, response);   //一定要调用过滤链
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
