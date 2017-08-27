package cn.zhang.crud.test;

import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.zhang.crud.bean.Employee;
import cn.zhang.crud.dao.DepartmentMapper;
import cn.zhang.crud.dao.EmployeeMapper;

@SuppressWarnings("all")
@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class SimpleTest {

	private static final Logger logger = LogManager.getLogger(SimpleTest.class.getName());
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Test
	public void testInsert() {
//		departmentMapper.insertSelective(new Department(null, "开发部"));
//		departmentMapper.insertSelective(new Department(null, "人事部"));
//		departmentMapper.insertSelective(new Department(null, "测试部"));
//		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//		for(int i = 0; i < 10000; i++) {
//			String uid = UUID.randomUUID().toString().substring(0, 6);
//			mapper.insert(new Employee(null, uid, "M", uid + "@163.com", 1));
//		}
	}

	@Test
	public void testDruidDataSource() throws SQLException {
		logger.info(dataSource.getConnection());
	}
}
