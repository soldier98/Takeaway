package com.soldier.dao;

import com.soldier.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */

public interface EmployeeDao extends BaseMapper<Employee> {

}
