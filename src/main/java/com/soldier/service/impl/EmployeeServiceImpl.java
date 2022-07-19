package com.soldier.service.impl;

import com.soldier.domain.Employee;
import com.soldier.dao.EmployeeDao;
import com.soldier.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements IEmployeeService {

}
