package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soldier.common.CustomException;
import com.soldier.common.Result;
import com.soldier.domain.Dish;
import com.soldier.domain.Setmeal;
import com.soldier.domain.SetmealDish;
import com.soldier.dto.SetmealDto;
import com.soldier.service.ICategoryService;
import com.soldier.service.ISetmealDishService;
import com.soldier.service.ISetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ISetmealDishService isetmealdishService;

    @Autowired
    private ICategoryService categoryService;


    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(int page, int pageSize, String name) {

        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> sLQW = new LambdaQueryWrapper<>();
        sLQW.like(name != null, Setmeal::getName, name);
        //按更新时间降序排
        sLQW.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, sLQW);

        //除却records属性，其他属性都拷贝过去
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        //取出 records属性，将集合中的setmeal都封装成setmealDish
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map(e -> {
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = e.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            BeanUtils.copyProperties(e, setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return new Result<>(1, "", setmealDtoPage);
    }

    /**
     * @param setmealDto
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //保存套餐，同时保存对应的Dish在Setmeal_dish表中
     * @Date 2022/8/4
     **/
    @PostMapping
    public Result<String> saveWithDish(@RequestBody SetmealDto setmealDto) {
        //先把套餐基本信息保存在Seatmeal表中
        setmealService.save(setmealDto);

        //保存对应的菜品明细（关联关系不能丢）
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        Collection<SetmealDish> collection = setmealDishList.stream().map(e -> {
                    e.setSetmealId(setmealDto.getId());
                    return e;
                }
        ).collect(Collectors.toList());

        boolean b = isetmealdishService.saveBatch(collection);
        if (b) {
            return new Result<>(1, "", null);
        } else return new Result<>(0, "", null);

    }

    /**
     * @param id
     * @return com.soldier.common.Result
     * @Author soldier
     * @Description //修改页面查询到详情
     * @Date 2022/8/5
     **/
    @GetMapping("/{id}")
    public Result<SetmealDto> selectById(@PathVariable("id") Long id) {
        Setmeal byId = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = isetmealdishService.list(setmealDishLambdaQueryWrapper);

        BeanUtils.copyProperties(byId, setmealDto);// 将byid中的属性copy到setmealDto

        setmealDto.setSetmealDishes(list);

        return new Result<SetmealDto>(1, "", setmealDto);
    }

    /**
     * @param id
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //删除套餐（包括套餐关联的商品）
     * 因为要保证调用的两个service方法都执行成功才算删除成功，需要添加事物管理
     * ***重要***不推荐在controller中弄事物管理，因为异常没地方抛了，这里是我练习下才这样写，
     * 推荐吧业务写到service层
     * @Date 2022/8/6
     **/
    @Transactional
    @DeleteMapping
    public Result<String> delet(Long id) {

        Setmeal setmeal = setmealService.getById(id);
        //判断套餐是否处于可售状态，可售则不可删除，抛出异常
        if (setmeal.getStatus() == 1) {
            throw new CustomException("套餐处于可售，不可删除");
        }
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);

        setmealService.removeById(setmeal.getId());
        boolean removeflag = isetmealdishService.remove(setmealDishLambdaQueryWrapper);
        if (removeflag) {
            return new Result<>(1, "", null);
        } else return new Result<>(0, "删除失败", null);
    }

    @GetMapping("/list")
    public Result<List> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId())
                                 .eq(setmeal.getStatus()!=null,Setmeal::getStatus,1);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        return new Result<>(1,"套餐list",setmealList);

    }
}

