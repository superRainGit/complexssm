package cn.zhang.crud.dao;

import java.util.List;

import cn.zhang.crud.bean.Employee;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer empId);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer empId);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
    
    Employee selectByPrimaryKeyWithDept(Integer empId);
    
    List<Employee> selectAllEmpsWithDept();
    
    List<Employee> getEmpWithName(String empName);
    
}