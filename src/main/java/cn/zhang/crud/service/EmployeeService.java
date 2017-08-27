package cn.zhang.crud.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhang.crud.bean.Employee;
import cn.zhang.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<Employee> getAllEmps() {
		return employeeMapper.selectAllEmpsWithDept();
	}

	public void save(Employee employee) {
		employeeMapper.insertSelective(employee);
	}

	public long getEmpWithName(String empName) {
		return employeeMapper.getEmpWithName(empName).size();
	}

	public Employee getEmpWithId(Integer id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	public int updateEmp(Employee employee) {
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}

	public int delWithId(Integer id) {
		return employeeMapper.deleteByPrimaryKey(id);
	}

	public int delPatchWithId(String ids) {
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		int count = 0;
		for(String id : ids.split(",")) {
			count += mapper.deleteByPrimaryKey(Integer.valueOf(id));
		}
		return count;
	}

}
