package com.soldier.service;

import com.soldier.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */

public interface ICategoryService extends IService<Category> {
    /**
     *
     * @Author soldier
     * @Description 扩展删除方法，判定是否有关联项
     * @Date  2022/8/1
     * @param id
     * @return boolean
     **/
    public boolean remove(Long id);
}
