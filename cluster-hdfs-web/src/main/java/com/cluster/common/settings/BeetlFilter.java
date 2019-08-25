package com.cluster.common.settings;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.beetl.core.Template;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

/**
 * Beetl模板过滤器
 * @author admin
 *
 */
public class BeetlFilter implements Filter {
	private BeetlGroupUtilConfiguration coinfig;

	public BeetlFilter(BeetlGroupUtilConfiguration coinfig) {
		this.coinfig = coinfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// 不处理inc 及 error下的html文件
		String path = req.getRequestURI();
		// 文件不存在，跳过
		if (!coinfig.getGroupTemplate().getResourceLoader().exist(path)) {
			chain.doFilter(request, response);
			return;
		}
		response.setContentType("text/html; charset=UTF-8");
		Template t = coinfig.getGroupTemplate().getTemplate(path);
		// 绑定request
		t.binding("request", req);
		t.renderTo(response.getWriter());
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {

	}

}
