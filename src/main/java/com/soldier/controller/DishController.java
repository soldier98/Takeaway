package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.Result;
import com.soldier.domain.Dish;
import com.soldier.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.LambdaConversionException;
import java.util.List;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private IDishService dishService;

    //查询
    @GetMapping("/page")
    public Result<Page> select(int page, int pageSize) {
        Page<Dish> dishPage = new Page<>(page, pageSize);

        //按创建时间
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.orderByAsc(Dish::getCreateTime);

        dishService.page(dishPage, lqw);
        return new Result<Page>(1, "", dishPage);

    }

    /*
     *
     * @Author soldier
     * @Description // 通过id查询菜品详情，包括口味
     * @Date  2022/8/3
     * @param id
     * @return com.soldier.common.Result<com.soldier.domain.Dish>
     **/
    @GetMapping("/{id}")
    public Result<Dish> query(@PathVariable("id") Long id) {
        Dish dish = dishService.getById(id);
        log.info(dish.toString());
        if (dish != null) {
            return new Result<Dish>(1, "", dish);
        } else return new Result<Dish>(0, "未查到", null);

    }

    /**
     * @param dish
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //保存新增菜品
     * @Date 2022/8/3
     **/
    @PostMapping
    public Result<String> save(@RequestBody Dish dish) {
        boolean flag = dishService.save(dish);
        if (flag) {
            return new Result<String>(1, "bao cun cheng gong", null);
        } else return new Result<String>(0, "", null);
    }

    /**
     * @param categoryId
     * @return com.soldier.common.Result<java.util.List>
     * @Author soldier
     * @Description //通过菜品分类id查询对应的菜品们
     * @Date 2022/8/4
     **/
    @GetMapping("/list")
    public Result<List> list(Dish dish) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(dish != null, Dish::getCategoryId, dish.getCategoryId());
        //查询菜品是否处于可售卖状态
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);

        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getCreateTime);

        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return new Result<List>(1, "", list);

    }
}

