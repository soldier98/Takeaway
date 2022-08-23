package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.Result;
import com.soldier.domain.Dish;
import com.soldier.domain.DishFlavor;
import com.soldier.dto.DishDto;
import com.soldier.service.IDishFlavorService;
import com.soldier.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;

    //查询
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize) {
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
            //保存成功，菜品变化，redis中分类查询的信息需清零
            String dishkey="dish_"+dish.getCategoryId()+"_1";
            redisTemplate.delete(dishkey);
            return new Result<String>(1, "bao cun cheng gong", null);
        } else return new Result<String>(0, "", null);
    }

    /**
     * @param
     * @return com.soldier.common.Result<java.util.List>
     * @Author soldier
     * @Description //移动端通过菜品分类catageryId查询
     * @Date 2022/8/4
     **/
    @GetMapping("/list")
    public Result<List<DishDto>> list(DishDto dishDto) {

        List<DishDto> dishDtoes = null;
        //动态获取key，以分类id和在售状态作key
        String dishKey = "dish"+"_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();
        //从redis中获取数据
        dishDtoes = (List<DishDto>) redisTemplate.opsForValue().get(dishKey);
        //如果存在数据，则返回
        if(dishDtoes != null){
            return new Result<>(1,"",dishDtoes);
        }

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(dishDto != null, Dish::getCategoryId, dishDto.getCategoryId());
        //查询菜品是否处于可售卖状态
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getCreateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);


        dishDtoes = list.stream().map(item -> {
            DishDto dishDto1 = new DishDto();
            BeanUtils.copyProperties(item, dishDto1);

            //在把flavor的信息存进去
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto1.getId());
            List<DishFlavor> list1 = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto1.setDishFlavors(list1);

            return dishDto1;
        }).collect(Collectors.toList());

        //将查到的信息存入redis
        redisTemplate.opsForValue().set(dishKey,dishDtoes,60, TimeUnit.MINUTES);

        return new Result<List<DishDto>>(1, "", dishDtoes);

    }
}

