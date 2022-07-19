package com.soldier.service.impl;

import com.soldier.domain.Orders;
import com.soldier.dao.OrdersDao;
import com.soldier.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {

}
