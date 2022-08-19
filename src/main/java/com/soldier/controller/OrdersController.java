package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.BaseCotext;
import com.soldier.common.Result;
import com.soldier.domain.Orders;
import com.soldier.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Action;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;
    /**
     * @param order
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //TODO 提交订单
     * @Date 2022/8/14
     **/

    @Transactional
    @PostMapping("/submit")
    public Result<String> addOrder(@RequestBody Orders order) {
        Result<String> stringResult = ordersService.addOrder(order);
        return stringResult;
    }

    @GetMapping("/userPage")
public Result<Page<Orders>> orderPage(Integer page, Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Long userId = BaseCotext.getId();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);
        return new Result<>(1,"",ordersPage);
}
}

