package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soldier.common.BaseCotext;
import com.soldier.common.Result;
import com.soldier.domain.AddressBook;
import com.soldier.service.IAddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        //使用前面封装的localThread来保持这个数据
        addressBook.setUserId(BaseCotext.getId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return new Result<>(1, "", addressBook);
    }


    /**
     * 根据地址id删除用户地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam("ids") Long id) {

        if (id == null) {
            return new Result<>(0, "请求异常", null);
        }
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, id).eq(AddressBook::getUserId, BaseCotext.getId());
        addressBookService.remove(queryWrapper);
        //addressBookService.removeById(id);  感觉直接使用这个removeById不太严谨.....
        return new Result<>(1, "删除地址成功", null);
    }


    /**
     * 修改收货地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {

        if (addressBook == null) {
            return new Result<>(0, "请求异常", null);
        }
        addressBookService.updateById(addressBook);
        return new Result<>(1, "修改成功", null);
    }


    /**
     * 设置默认地址
     * 1表示默认地址
     */
    @PutMapping("default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseCotext.getId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return new Result<>(1, "", addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return new Result<>(1, "", addressBook);
        } else {
            return new Result<>(0, "没有找到该对象", null);
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public Result<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseCotext.getId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return new Result<>(0, "没有找到该对象", null);
        } else {
            return new Result<>(1, "", addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseCotext.getId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return new Result<>(1, "", addressBookService.list(queryWrapper));
    }
}

