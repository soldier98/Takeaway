package com.soldier.service.impl;

import com.soldier.domain.Dish;
import com.soldier.dao.DishDao;
import com.soldier.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements IDishService {

}
