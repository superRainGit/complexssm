package cn.zhang.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.zhang.crud.bean.Department;
import cn.zhang.crud.bean.JsonMsg;
import cn.zhang.crud.service.DepartmentService;

@Controller
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping("/depts")
	@ResponseBody
	public JsonMsg getDepts() {
		List<Department> depts = departmentService.getAllDepts();
		return JsonMsg.success().add("depts", depts);
	}
	
}
