package com.soldier.service;

import com.soldier.common.Result;
import com.soldier.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
public interface IOrdersService extends IService<Orders> {
    //扩展一个添加订单的方法
    Result<String> addOrder(Orders order);
}
