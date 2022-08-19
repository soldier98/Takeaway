package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.Result;
import com.soldier.domain.Employee;
import com.soldier.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
    //    @Autowired
    @Qualifier("employeeService")
    @Autowired
    private IEmployeeService employeeService;

    //1、将页面提交的密码password进行md5加密处理
    //2、根据页面提交的用户名username查询数据库
    //3、如果没有查询到则返回登录失败结果
    //4、密码比对，如果不一致则返回登录失败结果
    //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
    //6、登录成功，将员工id存入Session并返回登录成功结果


    /**
     * @param request
     * @param employee
     * @return com.soldier.common.Result<com.soldier.domain.Employee>
     * @Author soldier
     * @Description //TODO
     * @Date 2022/7/18
     **/
    @PostMapping("/login")
    public Result<Employee> longIn(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("接收到的登录验证用户信息：{}", employee.toString());

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
        request.getSession().setAttribute("employee", employee1.getId());
//        log.info("登录成功后seesion存上信息验证：{}",request.getSession().getAttribute("employee"));
        return new Result(1, "登陆成功", employee);

    }

    /**
     * @param request
     * @return com.soldier.common.Result
     * @Author soldier
     * @Description //TODO
     * @Date 2022/7/18
     **/
    @RequestMapping("/logout")
    public Result loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return new Result(1, "退出成功", null);
    }

    /**
     * @param employee
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //添加员工
     * @Date 2022/7/22
     **/
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("添加员工的信息:{}", employee.toString());
        //设置个默认密码123456，并且加密后存入数据库
        String pwd = "123456";
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8));
        employee.setPassword(pwd);

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long employeeID = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeID);
        employee.setUpdateUser(employeeID);

        boolean flag = employeeService.save(employee);
        if (flag == true) {
            return new Result<>(1, "已添加", null);
        } else return new Result<>(0, "添加失败", null);
    }

    /**
     * @param page
     * @param pageSize
     * @param name
     * @return com.soldier.common.Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @Author soldier
     * @Description /分页查询员工信息
     * @Date 2022/7/28
     **/
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //新建Page对象
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //查询条件
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        Page<Employee> page1 = employeeService.page(pageInfo, lqw);
        //
        return new Result<Page>(1, "", page1);
    }

    /**
     * @param request
     * @param employee
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //修改状态哦
     * @Date 2022/7/28
     **/
//    @PutMapping
//    public Result<String> doStatus(HttpServletRequest request, @RequestBody Employee employee) {
//        log.info(employee.toString());
//
//        //获得当前会话操作者ID
//        Long editID = (Long) request.getSession().getAttribute("employee");
//
//        employee.setUpdateUser(editID);
//        employee.setUpdateTime(LocalDateTime.now());
//
//        employeeService.updateById(employee);
//        return new Result<String>(1, "更新成功", null);
//    }

    /**
     * @param
     * @return com.soldier.common.Result<com.soldier.domain.Employee>
     * @Author soldier
     * @Description //修改页面反查详情
     * @Date 2022/7/28
     **/
    @GetMapping("/{id}")
    public Result<Employee> selectDetail(@PathVariable Long id) {
        Employee byId = employeeService.getById(id);
        if (byId != null) {
            return new Result<Employee>(1, "", byId);
        } else return new Result<Employee>(0, "查询详情失败", null);
    }

    /**
     * @param employee
     * @return com.soldier.common.Result<com.soldier.domain.Employee>
     * @Author soldier
     * @Description //修改员工信息
     * @Date 2022/7/28
     **/
    @PutMapping
    public Result<String> edit(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("edit当前线程id{}: ",Thread.currentThread().getId());
        Long editID = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(editID);
//        employee.setUpdateTime(LocalDateTime.now());

        boolean b = employeeService.updateById(employee);
        if (b) {
            return new Result<String>(1, "更新成功", "Y");
        } else return new Result<String>(0, "更新失败", "N");

    }
}

