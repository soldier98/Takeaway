package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soldier.common.BaseCotext;
import com.soldier.common.Result;
import com.soldier.domain.ShoppingCart;
import com.soldier.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private IShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("查询购物车");
        Long userId = BaseCotext.getId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);
        return new Result<>(1, "购物车查询ok", shoppingCarts);
    }
/**
 *
 * @Author soldier
 * @Description //TODO 向购物车添加
 * @Date  2022/8/11
 * @param shoppingCart
 * @return com.soldier.common.Result<com.soldier.domain.ShoppingCart>
 **/
    @PostMapping("/add")
    public Result<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车");

        //先获得现在用户的购物车
        Long userId = BaseCotext.getId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        //
        Long dishId = shoppingCart.getDishId();
        if(dishId != null){ //要添加的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {//不是菜品，则是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart);
        }
        //查询用户当前购物车有此item，则在元基础上number+1
        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if(one != null){
            Integer currentNumber = one.getNumber();
            one.setNumber(currentNumber+1);
            shoppingCartService.updateById(one);
        }//无此item，则保存
        shoppingCart.setNumber(1);
        one = shoppingCart;
        shoppingCartService.save(shoppingCart);
        return new Result<ShoppingCart>(1,"购物车添加成功",one);
    }

    @PostMapping
    public Result sub(){
return null;
    }
}

