package com.soldier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soldier.common.BusinessException;
import com.soldier.dao.DishDao;
import com.soldier.domain.Category;
import com.soldier.dao.CategoryDao;
import com.soldier.domain.Dish;
import com.soldier.domain.Setmeal;
import com.soldier.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soldier.service.IDishService;
import com.soldier.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {
    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;

    @Override
    public boolean remove(Long id) {
        //查询分类是否关联菜品
        LambdaQueryWrapper<Dish> dLQW = new LambdaQueryWrapper<>();
        dLQW.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dLQW);

        if (count1 > 0) {
            throw new BusinessException("存在关联菜品");
        }

        //查询分类是否关联套餐
        LambdaQueryWrapper<Setmeal> sLQW = new LambdaQueryWrapper();
        sLQW.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(sLQW);

        if (count2>0){
            throw new BusinessException("存在关联套餐");
        }

        boolean b = super.removeById(id);
        return b;
    }
}
