package com.soldier.dto;

import com.soldier.domain.Dish;
import com.soldier.domain.DishFlavor;
import lombok.Data;

import java.util.List;

/**
 * 【请填写功能名称】
 *
 * @author soldier97
 * @date 2022/8/4 14:45
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> dishFlavors;
}
