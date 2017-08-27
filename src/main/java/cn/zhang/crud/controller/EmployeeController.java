package cn.zhang.crud.controller;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zhang.crud.bean.Employee;
import cn.zhang.crud.bean.JsonMsg;
import cn.zhang.crud.service.EmployeeService;

@Controller
@SuppressWarnings("unused")
public class EmployeeController {

	private static final Logger logger = LogManager.getLogger(EmployeeController.class.getName());
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping("/login")
	public String login() {
		return "list";
	}
	
	/**
	 * 此处将单删与批量合并到一起
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public JsonMsg delEmpWithId(@PathVariable("id") String ids) {
		int count = 0;
		if(ids.contains(",")) {
			count = employeeService.delPatchWithId(ids);
		} else {
			Integer id = Integer.valueOf(ids);
			count = employeeService.delWithId(id);
		}
		return count == 0 ? JsonMsg.fail() : JsonMsg.success();
	}
	
	/**
	 * ajax请求可以直接发送PUT请求
	 * Tomcat会对这样的请求进行特殊处理: 不会将上送的请求域中的数据封装到Map集合中来供SpringMVC进行解析
	 *   所以直接使用PUT请求进行数据的发送会有问题
	 * 解决办法:
	 * 1、使用POST请求去封装数据
	 * 2、配置一个HttpPutFormContentFilter过滤器
	 * 		原理:SpringMVC提供的HttpPutFormContentFilter 会将PUT或者PATCH的请求
	 * 		进行特殊的处理  它会将PUT发送过来的数据进行Map结构的封装  并且会将原生的request对象
	 * 		进行包装  重写了其中的getParameter()方法  保证当从正常的渠道取不到对应的参数的信息的时候
	 * 		从HttpPutFormContentFilter封装的map结构中进行参数的获取
	 * @param employee
	 * @return
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public JsonMsg editEmp(Employee employee) {
		logger.info(JSON.toJSONString(employee));
		return employeeService.updateEmp(employee) > 0 ? JsonMsg.success() : JsonMsg.fail();
	}
	
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JsonMsg getEmpWithId(@PathVariable("id") Integer id) {
		Employee emp = employeeService.getEmpWithId(id);
		return null == emp ? JsonMsg.fail() : JsonMsg.success().add("emp", emp);
	}
	
	@RequestMapping(value = "/emp")
	@ResponseBody
	public JsonMsg getEmpWithName(@RequestParam("empName") String empName) {
		long count = employeeService.getEmpWithName(empName);
		return count == 0 ? JsonMsg.success() : JsonMsg.fail();
	}
	
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	@ResponseBody
	public JsonMsg saveEmp(@Valid Employee employee, BindingResult result) {
		if(result.hasErrors()) {
			// 将出错的信息发送到前端页面进行显示
			Map<String, Object> errMap = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError error : errors) {
				errMap.put(error.getField(), error.getDefaultMessage());
			}
			return JsonMsg.fail().add("errorMsg", errMap);
		} else {
			employeeService.save(employee);
			return JsonMsg.success();
		}
	}
	
	@RequestMapping("/emps")
	@ResponseBody
	public JsonMsg getEmps(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize", defaultValue="5") Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Employee> emps = employeeService.getAllEmps();
		PageInfo<Employee> pageInfo = new PageInfo<>(emps, 10);
		return JsonMsg.success().add("pageInfo", pageInfo);
	}

//	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
			Map<String, Object> map) {
		PageHelper.startPage(pageNum, pageSize);
		List<Employee> emps = employeeService.getAllEmps();
		PageInfo<Employee> pageInfo = new PageInfo<>(emps, 10);
		map.put("pageInfo", pageInfo);
		return "list";
	}
}
