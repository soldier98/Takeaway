package com.soldier.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 【统一的返回结果】
 *
 * @param <T>
 * @author soldier97
 * @date 2022/7/15 11:57
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;  //编码1成功，其他数字则为失败
    private String msg;
    private T data;

}
