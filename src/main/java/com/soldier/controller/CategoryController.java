package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.Result;
import com.soldier.domain.Category;
import com.soldier.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    /**
     * @param category
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description // 添加分类
     * @Date 2022/8/1
     **/
    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        boolean flag = categoryService.updateById(category);
        if (flag) {
            return new Result<String>(1, "添加成功", null);
        } else
            return new Result<String>(0, "添加失败", null);
    }

    /**
     * @param page
     * @param pageSize
     * @return com.soldier.common.Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @Author soldier
     * @Description //菜品分类查询
     * @Date 2022/8/1
     **/
    @GetMapping("/page")
    public Result<Page> select(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, lqw);

        return new Result<Page>(1, "", pageInfo);
    }

    /**
     * @param ids
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //删除菜品分类
     * @Date 2022/8/1
     **/

    @DeleteMapping
    public Result<String> delete(Long ids) {
        categoryService.removeById(ids);
        return new Result<String>(1, "", null);
    }

    /**
     * @param category
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //修改
     * @Date 2022/8/2
     **/
    @PutMapping
    public Result<String> edit(@RequestBody Category category) {
        boolean flag = categoryService.updateById(category);
        if (flag) {
            return new Result<String>(1, "修改成功", null);
        }
        return new Result<>(0, "修改失败", null);
    }
/**
 *
 * @Author soldier
 * @Description /根据type查询菜品、套餐分类
 * @Date  2022/8/5
 * @param category
 * @return com.soldier.common.Result<java.util.List>
 **/
    @GetMapping("/list")
    public Result<List> getDishList(Category category) {

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!= null,Category::getType, category.getType()).orderByAsc(Category::getSort);
        List<Category> categoryList = categoryService.list(lambdaQueryWrapper);

        return new Result<List>(1, "", categoryList);
    }
}

