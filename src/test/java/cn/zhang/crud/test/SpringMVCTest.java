package cn.zhang.crud.test;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;

import cn.zhang.crud.bean.Employee;

@RunWith(value=SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class SpringMVCTest {

	private static final Logger logger = LogManager.getLogger(SpringMVCTest.class.getName());

	/**
	 * 如果想要直接注入 SpringMVC 自己的IOC容器 需要使用 @WebAppConfiguration 注解
	 */
	@Autowired
	private WebApplicationContext context;
	
	/**
	 * 使用 MockMvc 此对象时SpringMVC提供的一个模拟发送请求的实例
	 */
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	/**
	 * 如果想要使用SpringMVC4的测试环境  需要使用Servlet3.0以上的版本
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testMvc() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/emps").param("pageNum", "1").param("pageSize", "20");
		MvcResult andReturn = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletRequest request = andReturn.getRequest();
		PageInfo<Employee> pi = (PageInfo<Employee>) request.getAttribute("pageInfo");
		logger.info("当前页码--" + pi.getPageNum());
		logger.info("总页码--" + pi.getPages());
		logger.info("总记录数--" + pi.getTotal());
		List<Employee> emps = pi.getList();
		for (Employee emp : emps) {
			logger.info(emp.getEmpId() + "--->" + emp.getEmpName());
		}
		int[] nums = pi.getNavigatepageNums();
		logger.info("当前显示的页码是--" + Arrays.toString(nums));
	}
	
}
