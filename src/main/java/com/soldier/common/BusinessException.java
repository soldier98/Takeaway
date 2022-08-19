package com.soldier.common;

/**
 * 【请填写功能名称】
 *
 * @author soldier97
 * @date 2022/8/1 22:25
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message){
        super(message);
    }
}
