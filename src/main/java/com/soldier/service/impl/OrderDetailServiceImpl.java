package com.soldier.service.impl;

import com.soldier.domain.OrderDetail;
import com.soldier.dao.OrderDetailDao;
import com.soldier.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements IOrderDetailService {

}
