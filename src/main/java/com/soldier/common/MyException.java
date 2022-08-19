package com.soldier.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 【全局异常处理】
 *
 * @author soldier97
 * @date 2022/7/26 12:42
 */
@RestControllerAdvice//(annotations ={RestController.class})
@Slf4j
public class MyException {
    @ExceptionHandler(SQLException.class)
    public Result<String> exceptionHandler(Exception ex){
        log.info("异常信息"+ex.getMessage());
        return new Result<String>(0,"SQLException异常",null);
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> doHandler(CustomException e){
        log.error("消费者异常"+e.getMessage());
        return new Result<String>(0,e.getMessage(),null);
    }

}
