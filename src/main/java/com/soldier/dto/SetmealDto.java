package com.soldier.dto;

import com.soldier.domain.Dish;
import com.soldier.domain.Setmeal;
import com.soldier.domain.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 【Setmeal 的DTO,封装套餐信息以及菜品详情】
 *
 * @author soldier97
 * @date 2022/8/4 14:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>();

    //用于套餐界面的分类Name
    private String categoryName;

}
