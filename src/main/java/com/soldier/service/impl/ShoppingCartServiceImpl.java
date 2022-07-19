package com.soldier.service.impl;

import com.soldier.domain.ShoppingCart;
import com.soldier.dao.ShoppingCartDao;
import com.soldier.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements IShoppingCartService {

}
