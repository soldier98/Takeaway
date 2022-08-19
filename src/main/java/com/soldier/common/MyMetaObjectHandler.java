package com.soldier.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 【MP 自动填充字段】
 *
 * @author soldier97
 * @date 2022/7/28 20:15
 */
@Slf4j
@Component

public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseCotext.getId());
        metaObject.setValue("updateUser", BaseCotext.getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("MyMetaObjectHandler当前线程id: {}",Thread.currentThread().getId());

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseCotext.getId());


    }
}
