package cn.zhang.crud.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于返回更加一般化的Json格式的数据  而不是使用PageHelper等只有业务数据作为Json的返回
 * @author zhang
 *
 */
public class JsonMsg {

	// 用于进行自定义响应码的使用  100-Success  200-Fail
	private int code;
	// 用于记录响应的信息
	private String msg;
	// 用于记录业务逻辑数据
	private Map<String, Object> extendMap = new HashMap<String, Object>();
	
	public static JsonMsg success() {
		JsonMsg msg = new JsonMsg();
		msg.code = 100;
		msg.msg = "Success";
		return msg;
	}
	
	public static JsonMsg fail() {
		JsonMsg msg = new JsonMsg();
		msg.code = 200;
		msg.msg = "Fail";
		return msg;
	}
	
	/**
	 * 向外界提供 用于链式 增加业务数据的方法 
	 * @param key 放到Json格式的字符串的key
	 * @param value 对应key的业务数据
	 * @return
	 */
	public JsonMsg add(String key, Object value) {
		this.getExtendMap().put(key, value);
		return this;
	}
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map<String, Object> getExtendMap() {
		return extendMap;
	}
	public void setExtendMap(Map<String, Object> extendMap) {
		this.extendMap = extendMap;
	}
	
}
