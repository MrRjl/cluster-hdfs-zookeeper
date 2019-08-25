package com.cluster.common.vo;


public class ResultInfo<T> {
	
	/** 是否成功 */
	private boolean success;
	
	/** 失败代码 */
	private String code;
	
	/** 结果信息 */
	private String msg;
	
	/** 成功时返回的内容 */
	private T body;
	
	public static final String CODE400 = "400";//400 访问参数错误
	
	public static final String CODE404 = "404";//404 没找到等
	
	public static final String CODE200 = "200";//200 访问正常 有数据
	
	public static final String CODE401 = "401";//未授权
	
	public static final String CODE403 = "403";//访问资源不可用
	
	public static final String CODE406 ="406";//406 请求不符合，非法访问
	
	public static final String CODE500 = "500";//500 服务器异常
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	public static <B> ResultInfo<B> success(){
		ResultInfo<B> info=new ResultInfo<>();
		info.setCode(CODE200);
		info.setMsg("成功");
		info.setSuccess(true);
		return info;
	}
	public static <B> ResultInfo<B> success(B body){
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(CODE200);
		info.setMsg("成功");
		info.setSuccess(true);
		return info;
	}
	public static <B> ResultInfo<B> success(B body,String code){
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setMsg("成功");
		info.setSuccess(true);
		return info;
	}
	public static <B> ResultInfo<B> success(B body,String code,String msg){
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setMsg(msg);
		info.setSuccess(true);
		return info;
	}
	public static <B> ResultInfo<B> success(B body,String code,String msg,boolean boo){
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setMsg(msg);
		info.setSuccess(boo);
		return info;
	}
	public static <B> ResultInfo<B> error() {
		ResultInfo<B> info=new ResultInfo<>();
		info.setCode(CODE200);
		info.setSuccess(false);
		info.setMsg("失败");
		return info;
	}
	public static <B> ResultInfo<B> error(B body) {
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(CODE200);
		info.setSuccess(false);
		info.setMsg("失败");
		return info;
	}
	public static <B> ResultInfo<B> error(B body,String code) {
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setSuccess(false);
		info.setMsg("失败");
		return info;
	}
	public static <B> ResultInfo<B> error(B body,String code,boolean bool) {
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setSuccess(bool);
		info.setMsg("失败");
		return info;
	}
	public static <B> ResultInfo<B> error(B body,String code,boolean bool,String msg) {
		ResultInfo<B> info=new ResultInfo<>();
		info.setBody(body);
		info.setCode(code);
		info.setSuccess(bool);
		info.setMsg(msg);
		return info;
	}
	
}
