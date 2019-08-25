package com.cluster.common.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.beetl.core.Function;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;





/**
 * 配置拦截器
 * @author admin
 *
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

	/**
	 * 注册beetl filter，只负责静态部分的
	 * @return
	 */
	@Bean
	public FilterRegistrationBean registrationBeetlFilter(
			@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		BeetlFilter beetlFilter = new BeetlFilter(beetlGroupUtilConfiguration);
		registrationBean.setFilter(beetlFilter);
		List<String> urlPatterns = new ArrayList<String>(1);
		urlPatterns.add("*.html");
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(2);
		return registrationBean;
	}

	/**
	 * 配置beetl配置工具
	 * @return
	 */
	@Bean(initMethod = "init", name = "beetlConfig")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("static");
		beetlGroupUtilConfiguration.setResourceLoader(resourceLoader);
		// 注册函数
		Map<String, Function> functions = new HashMap<String, Function>();
		beetlGroupUtilConfiguration.setFunctions(functions);
		return beetlGroupUtilConfiguration;
	}
	/**
	 * 配置beetl视图解析器，动态部分的
	 * @return
	 */
	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(
			@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		beetlSpringViewResolver.setPrefix("/");
		beetlSpringViewResolver.setSuffix(".btl");
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(1);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}
	/*@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//单个文件最大 5m 可以使用读取配置
		factory.setMaxFileSize("300MB"); //KB,MB
		/// 设置总上传数据总大小 50m
		factory.setMaxRequestSize("1000MB");
		return factory.createMultipartConfig();
	}*/
	
	/*@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver(){
		return new StandardServletMultipartResolver();
	}*/
	/**  
     * 文件上传配置  
     * @return  
     */  
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //文件最大  
        factory.setMaxFileSize("1000MB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("1000MB");  
        return factory.createMultipartConfig();  
    }  
}
