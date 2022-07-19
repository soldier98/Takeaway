package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soldier.common.Result;
import com.soldier.domain.Employee;
import com.soldier.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-13
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    @Qualifier("employeeService")
    private IEmployeeService employeeService;

    //1、将页面提交的密码password进行md5加密处理
    //2、根据页面提交的用户名username查询数据库
    //3、如果没有查询到则返回登录失败结果
    //4、密码比对，如果不一致则返回登录失败结果
    //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
    //6、登录成功，将员工id存入Session并返回登录成功结果


/**
 *
 * @Author soldier
 * @Description //TODO
 * @Date  2022/7/18
 * @param request
 * @param employee
 * @return com.soldier.common.Result<com.soldier.domain.Employee>
 **/
    @PostMapping("/login")
    public Result<Employee> longIn(HttpServletRequest request, @RequestBody Employee employee) {

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee employee1 = employeeService.getOne(lqw);

        //3、如果没有查询到则返回登录失败结果
        if (employee1 == null) {
            return new Result<>(0, "账号未注册", null);
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if (!employee1.getPassword().equals(password)) {
            return new Result<>(0, "密码错误", null);
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (employee1.getStatus() == 0) {
            return new Result<>(0, "该账号已禁用", null);
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", employee.getId());
        return new Result(1, "登陆成功", employee);

    }
    /**
     *
     * @Author soldier
     * @Description //TODO
     * @Date  2022/7/18
     * @param request
     * @return com.soldier.common.Result
     **/
    @RequestMapping("/logout")
    public Result loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return new Result(1,"退出成功",null);
    }

}

